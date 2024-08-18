package com.fhdo.bookingservice.domain.sm.listeners;

import com.fhdo.bookingservice.config.RabbitMqConfiguration;
import com.fhdo.bookingservice.domain.response.BookingConfirmationResponse;
import com.fhdo.bookingservice.services.DefaultBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
/**
 * Listener class for processing booking confirmation results from the RabbitMQ queue.
 *
 * The {@code BookingConfirmationResultListener} class listens for messages from the RabbitMQ queue related to booking
 * confirmation responses. Upon receiving a confirmation result message, it processes the result by delegating to
 * {@link DefaultBookingService}.
 *
 * <p>This class is annotated with {@link Component} to be recognized as a Spring bean and {@link RabbitListener} to
 * listen for messages from the RabbitMQ queue specified in {@link RabbitMqConfiguration#CONFIRM_BOOKING_RESPONSE_QUEUE}. When
 * a {@link BookingConfirmationResponse} is received, it extracts the booking ID and confirmation status, then
 * processes the result using the {@code bookingService}.</p>
 *
 * <p>Key Dependencies:
 * <ul>
 *     <li>{@code DefaultBookingService} - Handles the business logic for processing booking confirmation results.</li>
 *     <li>{@code RabbitMqConfiguration} - Provides the configuration for RabbitMQ queues.</li>
 *     <li>{@code BookingConfirmationResponse} - Represents the message payload for booking confirmation responses.</li>
 * </ul>
 * </p>
 *
 * @see DefaultBookingService
 * @see RabbitMqConfiguration
 * @see BookingConfirmationResponse
 */

@RequiredArgsConstructor
@Component
public class BookingConfirmationResultListener {

    private final DefaultBookingService bookingService;
    @RabbitListener(queues = RabbitMqConfiguration.CONFIRM_BOOKING_RESPONSE_QUEUE)
    public void listen(BookingConfirmationResponse result){
        final UUID bookingId = result.getBookingId();
        bookingService.processConfirmationResult(bookingId, result.getIsConfirmed());
    }
}
