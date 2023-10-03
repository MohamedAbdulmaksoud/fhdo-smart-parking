package com.fhdo.bookingservice.domain.sm.actions;

import com.fhdo.bookingservice.config.RabbitMqConfiguration;
import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationMessageRequest;
import com.fhdo.bookingservice.entities.BookingEntity;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmBookingAction implements Action<BookingState, BookingEvent> {

    private final RabbitTemplate rabbitTemplate;

    private final BookingRepository repository;

    @Override
    public void execute(StateContext<BookingState, BookingEvent> stateContext) {
        BookingConfirmationMessageRequest request = Optional.ofNullable(stateContext)
                .map(StateContext::getMessage)
                .map(Message::getHeaders)
                .map(messageHeaders -> messageHeaders.get(BookingConfirmationMessageRequest.HEADER_NAME, BookingConfirmationMessageRequest.class))
                .orElseThrow(() -> new RuntimeException("Could not extract confirmation request from headers"));

        //check if an active booking already exists -> return
        UUID bookingId = Optional.of(request).map(BookingConfirmationMessageRequest::getBookingId)
                .orElseThrow(() -> new RuntimeException("Could not retrieve bookingId from BookingConfirmationRequest"));

        BookingEntity booking = repository.getReferenceById(bookingId);
        if (booking == null) {
            throw new RuntimeException("Could not find a booking with id: " + bookingId);
        }


        try {
            // send to Parking service to reserve parking spot
            rabbitTemplate.convertAndSend(RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE, request);
            log.debug("Sent confirmation request to queue for booking id {}", request.getBookingId());
            booking.setState(BookingState.PENDING_CONFIRMATION);
        } catch (AmqpException e) {
            log.error("Failed to send BookingConfirmationRquest to queue {} for booking id {}", RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE, request.getBookingId());
            booking.setState(BookingState.DECLINED);
            throw e;
        } finally {
            repository.saveAndFlush(booking);
        }
    }
}
