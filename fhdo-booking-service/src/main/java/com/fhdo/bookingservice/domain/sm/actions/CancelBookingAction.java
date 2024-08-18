package com.fhdo.bookingservice.domain.sm.actions;

import com.fhdo.bookingservice.config.RabbitMqConfiguration;
import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingCancellationMessageRequest;
import com.fhdo.bookingservice.entities.BookingEntity;
import com.fhdo.bookingservice.mappers.BookingEntityMapper;
import com.fhdo.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.fhdo.bookingservice.services.DefaultBookingService.BOOKING_ID_HEADER;

/**
 * Action class for handling the cancellation of bookings within the state machine.
 *
 * The {@code CancelBookingAction} class implements the {@link Action} interface and is used to process the cancellation
 * of bookings as part of the state machine workflow. It retrieves the booking entity from the repository, checks if the
 * booking is in a cancellable state, and then sends a cancellation request to the RabbitMQ queue.
 *
 * <p>This action is triggered when the state machine transitions to a state that requires booking cancellation. It performs
 * the following steps:
 * <ul>
 *     <li>Extracts the booking ID from the state context message headers.</li>
 *     <li>Fetches the booking entity from the repository using the booking ID.</li>
 *     <li>Checks if the booking is in a valid state for cancellation.</li>
 *     <li>Converts the booking entity to a {@link BookingCancellationMessageRequest} and sends it to the RabbitMQ queue.</li>
 * </ul>
 * </p>
 *
 * <p>Key Dependencies:
 * <ul>
 *     <li>{@code RabbitTemplate} - Used for sending messages to RabbitMQ queues.</li>
 *     <li>{@code BookingRepository} - Provides access to booking entities in the database.</li>
 *     <li>{@code BookingEntityMapper} - Maps booking entities to cancellation request objects.</li>
 * </ul>
 * </p>
 *
 * @see Action
 * @see StateContext
 * @see BookingEvent
 * @see BookingState
 * @see BookingEntity
 * @see BookingCancellationMessageRequest
 * @see RabbitMqConfiguration
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelBookingAction implements Action<BookingState, BookingEvent> {

    private final RabbitTemplate rabbitTemplate;

    private final BookingRepository repository;

    private final BookingEntityMapper mapper;

    @Override
    public void execute(StateContext<BookingState, BookingEvent> stateContext) {
        BookingEntity booking = Optional.ofNullable(stateContext)
                .map(StateContext::getMessage)
                .map(Message::getHeaders)
                .map(messageHeaders -> messageHeaders.get(BOOKING_ID_HEADER, UUID.class))
                .map(repository::getReferenceById)
                .orElseThrow(() -> new RuntimeException("Could not fetch booking from cancellation request header"));

        //check if booking is in valid state to be cancelled
        if (BookingState.CANCELLABLE_STATES.contains(booking.getState())) {
            try {
                BookingCancellationMessageRequest request = mapper.bookingToCancellationRequest(booking);

                rabbitTemplate.convertAndSend(RabbitMqConfiguration.CANCEL_BOOKING_QUEUE, request);
            } catch (AmqpException e) {
                log.error("Failed to send BookingCancellationMessageRequest to queue {} for booking id {}", RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE, booking.getBookingId());
                throw e;
            }
        }

        log.debug("Sent cancellation request to queue for booking id {}", booking.getBookingId());
    }
}
