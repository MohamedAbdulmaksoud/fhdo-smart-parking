package com.fhdo.bookingservice.domain.sm.listeners;

import com.fhdo.bookingservice.config.RabbitMqConfiguration;
import com.fhdo.bookingservice.domain.response.BookingCancellationMessageResponse;
import com.fhdo.bookingservice.services.BookingMessageResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
