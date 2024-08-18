package com.fhdo.bookingservice.domain.sm.listeners;

import com.fhdo.bookingservice.config.RabbitMqConfiguration;
import com.fhdo.bookingservice.domain.response.BookingCancellationMessageResponse;
import com.fhdo.bookingservice.services.BookingMessageResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
/**
 * Listener class for processing booking cancellation results from the RabbitMQ queue.
 *
 * The {@code BookingCancellationResultListener} class listens to messages from the RabbitMQ queue related to booking
 * cancellation responses. Upon receiving a cancellation result message, it processes the result by delegating to
 * {@link BookingMessageResponseHandler}.
 *
 * <p>This class is annotated with {@link Component} to be recognized as a Spring bean and {@link RabbitListener} to
 * listen for messages from the RabbitMQ queue specified in {@link RabbitMqConfiguration#CANCEL_BOOKING_RESPONSE}. When
 * a {@link BookingCancellationMessageResponse} is received, it extracts the booking ID and cancellation status, then
 * processes the result using the {@code bookingService}.</p>
 *
 * <p>Key Dependencies:
 * <ul>
 *     <li>{@code BookingMessageResponseHandler} - Handles the business logic for processing booking cancellation results.</li>
 *     <li>{@code RabbitMqConfiguration} - Provides the configuration for RabbitMQ queues.</li>
 *     <li>{@code BookingCancellationMessageResponse} - Represents the message payload for booking cancellation responses.</li>
 * </ul>
 * </p>
 *
 * @see BookingMessageResponseHandler
 * @see RabbitMqConfiguration
 * @see BookingCancellationMessageResponse
 */

@RequiredArgsConstructor
@Component
public class BookingCancellationResultListener {

    private final BookingMessageResponseHandler bookingService;
    @RabbitListener(queues = RabbitMqConfiguration.CANCEL_BOOKING_RESPONSE)
    public void listen(BookingCancellationMessageResponse result){
        final UUID bookingId = result.getBookingId();
        bookingService.processCancellationResult(bookingId, result.getIsCancelled());
    }
}
