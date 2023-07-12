package com.fhdo.bookingservice.domain.sm.listeners;

import com.fhdo.bookingservice.config.JmsConfig;
import com.fhdo.bookingservice.domain.response.BookingConfirmationResponse;
import com.fhdo.bookingservice.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BookingConfirmationResultListener {

    private final BookingService bookingService;
    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE)
    public void listen(BookingConfirmationResponse result){
        final UUID bookingId = result.bookingId();
        bookingService.processConfirmationResult(bookingId, result.isConfirmed());
    }
}
