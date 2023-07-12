package com.fhdo.bookingservice.services;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingRequest;
import com.fhdo.bookingservice.entities.BookingEntity;
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

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    public static final String BOOKING_ID_HEADER = "booking_id";
    private final BookingRepository bookingRepository;
    private final StateMachineFactory<BookingState, BookingEvent> stateMachineFactory;
    private final BookingStateChangeInterceptor bookingStateChangeInterceptor;

    @Override
    @Transactional
    public BookingEntity newBooking(BookingEntity booking) {
        booking.setState(BookingState.NEW);
        return bookingRepository.saveAndFlush(booking);
    }

    @Override
    @Transactional
    public void validate(BookingRequest request) {
        sendEventWithRequest(request.getBookingId(), BookingEvent.CHECK_VALIDITY, request);
    }

    @Override
    @Transactional
    public void cancel(UUID bookingId) {
        sendEvent(bookingId, BookingEvent.BOOKING_CANCELLED);
    }

    @Override
    public void processConfirmationResult(UUID bookingId, Boolean isConfirmed) {
        if (isConfirmed) {
            sendEvent(bookingId, BookingEvent.BOOKING_CONFIRMED);
        } else {
            sendEvent(bookingId, BookingEvent.BOOKING_FAILED);
        }
    }

    private void sendEvent(UUID bookingId, BookingEvent event) {
        StateMachine<BookingState, BookingEvent> sm = build(bookingId);
        Message<BookingEvent> msg = MessageBuilder.withPayload(event)
                .setHeader(BOOKING_ID_HEADER, bookingId)
                .build();

        sm.sendEvent(Mono.just(msg)).subscribe();

    }

    private void sendEventWithRequest(UUID bookingId, BookingEvent bookingEvent, BookingRequest request) {
        StateMachine<BookingState, BookingEvent> sm = build(bookingId);
        Message<BookingEvent> msg = MessageBuilder.withPayload(bookingEvent)
                .setHeader(BOOKING_ID_HEADER, bookingId)
                .setHeader(request.getHeader(),request)
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
