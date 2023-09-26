package com.fhdo.bookingservice.domain.sm.actions;

import com.fhdo.bookingservice.config.RabbitMqConfiguration;
import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationRequest;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmBookingAction implements Action<BookingState, BookingEvent> {

    private final RabbitTemplate rabbitTemplate;

    private final BookingRepository repository;

    @Override
    public void execute(StateContext<BookingState, BookingEvent> stateContext) {
        BookingConfirmationRequest request = Optional.ofNullable(stateContext)
                .map(StateContext::getMessage)
                .map(Message::getHeaders)
                .map(messageHeaders -> messageHeaders.get(BookingConfirmationRequest.HEADER_NAME, BookingConfirmationRequest.class))
                .orElseThrow(() -> new RuntimeException("Could not extract confirmation request from headers"));

        //check if an active booking already exists -> return

        //confirm booking: set to RESERVED. ACTIVE is based on listener receiving OCCUPIED message from the Parking Spot Queue.

        //build the response and send over the message queue to the parking system to reserve and user service to notify.

        try {
            rabbitTemplate.convertAndSend(RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE, request);
        } catch (AmqpException e) {
            log.error("Failed to send BookingConfirmationRquest to queue {} for booking id {}", RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE, request.getBookingId());
            throw e;
        }

        log.debug("Sent confirmation request to queue for booking id {}", request.getBookingId());
    }
}
