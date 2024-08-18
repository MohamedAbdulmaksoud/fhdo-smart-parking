package com.fhdo.bookingservice.services;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingCreationRequest;
import com.fhdo.bookingservice.domain.request.BookingMessageRequest;
import com.fhdo.bookingservice.domain.response.BookingBaseResponse;
import com.fhdo.bookingservice.domain.response.BookingFullResponse;
import com.fhdo.bookingservice.entities.BookingEntity;
import com.fhdo.bookingservice.mappers.MapStructMapper;
import com.fhdo.bookingservice.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
/**
 * Implementation of {@link BookingService} and {@link BookingMessageResponseHandler} that handles booking operations
 * and state transitions using a state machine.
 *
 * <p>{@code DefaultBookingService} provides methods for creating, confirming, cancelling, retrieving, and deleting
 * bookings. It also handles the processing of booking confirmation and cancellation results by interacting with
 * the state machine and updating the booking entity's state in the repository.</p>
 *
 * <p>This service uses a state machine to manage the lifecycle of bookings, with various events triggering state transitions.
 * It integrates with {@link BookingRepository} to persist and retrieve booking entities and uses {@link MapStructMapper}
 * to map between domain objects and entity representations.</p>
 *
 * @see BookingService
 * @see BookingMessageResponseHandler
 * @see BookingEntity
 * @see BookingRepository
 * @see MapStructMapper
 * @see StateMachine
 */
@RequiredArgsConstructor
@Service
public class DefaultBookingService implements BookingService, BookingMessageResponseHandler {
    public static final String BOOKING_ID_HEADER = "booking_id";
    private final BookingRepository bookingRepository;
    private final StateMachineFactory<BookingState, BookingEvent> stateMachineFactory;
    private final BookingStateChangeInterceptor bookingStateChangeInterceptor;
    private final MapStructMapper mapper;

    @Override
    @Transactional
    public BookingBaseResponse newBooking(BookingCreationRequest request) {
        BookingEntity entity = mapper.bookingCreationRequestToBookingEntity(request);
        return mapper.bookingEntityToBaseResponse(bookingRepository.saveAndFlush(entity));
    }

    @Override
    @Transactional
    public void confirmBooking(UUID bookingId) {
        sendEvent(bookingId, BookingEvent.BOOK_PARKING_SLOT);
    }

    @Override
    public BookingFullResponse getBooking(UUID bookingId) {
        return mapper.bookingEntityToFullResponse(bookingRepository.getReferenceById(bookingId));
    }

    @Override
    @Transactional
    public void cancel(UUID bookingId) {
        sendEvent(bookingId, BookingEvent.BOOKING_CANCELLED);
    }

    @Override
    @Transactional
    public void delete(UUID bookingId) {
        BookingEntity entity = Optional.of(bookingId)
                .map(bookingRepository::getReferenceById)
                .orElseThrow(() -> new RuntimeException("Booking: %s could not be found.".formatted(bookingId)));

        BookingState bookingState = entity.getState();

        if (BookingState.DELETABLE_STATES.contains(bookingState)) {
            bookingRepository.deleteById(bookingId);
        } else {
            throw new RuntimeException("Booking %s could not be deleted. It is not in an allowed state: %s".formatted(bookingId, bookingState));
        }
    }

    @Override
    @Transactional
    public void processConfirmationResult(UUID bookingId, Boolean isConfirmed) {
        if (isConfirmed) {
            bookingRepository.updateStateByBookingId(BookingState.CONFIRMED, bookingId);
        } else {
            bookingRepository.updateStateByBookingId(BookingState.DECLINED, bookingId);
        }
    }

    @Override
    @Transactional
    public void processCancellationResult(UUID bookingId, Boolean isConfirmed) {
        if (isConfirmed) {
            bookingRepository.updateStateByBookingId(BookingState.CANCELLED, bookingId);
        } else {
            throw new RuntimeException("Booking %s could not be cancelled. Failed to un-reserve parking spot".formatted(bookingId));
        }
    }

    private void sendEvent(UUID bookingId, BookingEvent event) {
        StateMachine<BookingState, BookingEvent> sm = build(bookingId);
        Message<BookingEvent> msg = MessageBuilder.withPayload(event)
                .setHeader(BOOKING_ID_HEADER, bookingId)
                .build();

        sm.sendEvent(Mono.just(msg)).subscribe();

    }

    private void sendEventWithRequest(UUID bookingId, BookingEvent bookingEvent, BookingMessageRequest request) {
        StateMachine<BookingState, BookingEvent> sm = build(bookingId);
        Message<BookingEvent> msg = MessageBuilder.withPayload(bookingEvent)
                .setHeader(BOOKING_ID_HEADER, bookingId)
                .setHeader(request.getHeader(), request)
                .build();

        sm.sendEvent(Mono.just(msg)).subscribe();
    }

    /*
     * Build and populate a state machine from database entity
     * */

    private StateMachine<BookingState, BookingEvent> build(UUID bookingId) {
        BookingEntity booking = bookingRepository.getReferenceById(bookingId);

        StateMachine<BookingState, BookingEvent> sm = stateMachineFactory.getStateMachine();
        sm.stopReactively().block();

        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(bookingStateChangeInterceptor);
            sma.resetStateMachineReactively(new DefaultStateMachineContext<>(booking.getState(), null, null, null)).block();
        });

        sm.startReactively().block();
        return sm;
    }
}
