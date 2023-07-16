package com.fhdo.bookingservice.domain.sm.listeners;

import com.fhdo.bookingservice.config.RabbitMqConfiguration;
import com.fhdo.bookingservice.domain.response.BookingConfirmationResponse;
import com.fhdo.bookingservice.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BookingConfirmationResultListener {

    private final BookingService bookingService;
    @RabbitListener(queues = RabbitMqConfiguration.CONFIRM_ORDER_RESPONSE_QUEUE)
    public void listen(BookingConfirmationResponse result){
        final UUID bookingId = result.getBookingId();
        bookingService.processConfirmationResult(bookingId, result.getIsConfirmed());
    }
}
