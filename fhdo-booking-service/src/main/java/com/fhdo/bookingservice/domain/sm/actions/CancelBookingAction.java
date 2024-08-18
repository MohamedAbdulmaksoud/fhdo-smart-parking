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
