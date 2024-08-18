package com.fhdo.bookingservice.domain.sm.actions;

import com.fhdo.bookingservice.config.RabbitMqConfiguration;
import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationMessageRequest;
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
 * Action class for handling the confirmation of bookings within the state machine.
 *
 * The {@code ConfirmBookingAction} class implements the {@link Action} interface and is used to process the confirmation
 * of bookings as part of the state machine workflow. It verifies the availability of the parking spot, updates the booking
 * state, and sends a confirmation request to the RabbitMQ queue.
 *
 * <p>This action is triggered when the state machine transitions to a state that requires booking confirmation. It performs
 * the following steps:
 * <ul>
 *     <li>Extracts the booking ID from the state context message headers.</li>
 *     <li>Retrieves the booking entity from the repository using the booking ID.</li>
 *     <li>Checks if there is any conflicting booking for the same parking spot and time.</li>
 *     <li>Converts the booking entity to a {@link BookingConfirmationMessageRequest} and sends it to the RabbitMQ queue.</li>
 *     <li>Updates the booking state to {@link BookingState#PENDING_CONFIRMATION} if the request is sent successfully, or {@link BookingState#DECLINED} if a conflict is found.</li>
 * </ul>
 * </p>
 *
 * <p>Key Dependencies:
 * <ul>
 *     <li>{@code RabbitTemplate} - Used for sending messages to RabbitMQ queues.</li>
 *     <li>{@code BookingRepository} - Provides access to booking entities in the database and checks for conflicting bookings.</li>
 *     <li>{@code BookingEntityMapper} - Maps booking entities to confirmation request objects.</li>
 * </ul>
 * </p>
 *
 * @see Action
 * @see StateContext
 * @see BookingEvent
 * @see BookingState
 * @see BookingEntity
 * @see BookingConfirmationMessageRequest
 * @see RabbitMqConfiguration
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmBookingAction implements Action<BookingState, BookingEvent> {

    private final RabbitTemplate rabbitTemplate;

    private final BookingRepository repository;

    private final BookingEntityMapper mapper;

    @Override
    public void execute(StateContext<BookingState, BookingEvent> stateContext) {
        UUID bookingId = Optional.ofNullable(stateContext)
                .map(StateContext::getMessage)
                .map(Message::getHeaders)
                .map(messageHeaders -> messageHeaders.get(BOOKING_ID_HEADER, UUID.class))
                .orElseThrow(() -> new RuntimeException("Could not retrieve bookingId from stateContext"));

        BookingEntity booking = repository.getReferenceById(bookingId);
        if (repository.existsByParkingIdAndParkingSpotIdAndStartTimeAfterAndEndTimeBeforeAllIgnoreCase(booking.getParkingId(), booking.getParkingSpotId(), booking.getStartTime(), booking.getEndTime())) {
            log.error("Can not confirm booking, there exists a booking at same time: " + bookingId);
            booking.setState(BookingState.DECLINED);
        }

        BookingConfirmationMessageRequest request = mapper.bookingToConfirmationRequest(booking);

        try {
            rabbitTemplate.convertAndSend(RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE, request);
            log.debug("Sent confirmation request to queue for booking id {}", request.getBookingId());
            booking.setState(BookingState.PENDING_CONFIRMATION);
        } catch (AmqpException e) {
            log.error("Failed to send BookingConfirmationRquest to queue {} for booking id {}", RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE, request.getBookingId());
            throw e;
        } finally {
            repository.saveAndFlush(booking);
        }
    }
}
