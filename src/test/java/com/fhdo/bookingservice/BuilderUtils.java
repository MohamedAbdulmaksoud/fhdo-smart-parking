package com.fhdo.bookingservice;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.request.BookingConfirmationRequest;
import com.fhdo.bookingservice.domain.request.BookingRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

public class BuilderUtils {

    public static BookingConfirmationRequest bookingConfirmationRequest(){
        return BookingConfirmationRequest.builder()
                .bookingId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .parkingLotId(UUID.randomUUID())
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusMinutes(30))
                .build();
    }

    public static Message<BookingEvent> eventMessage(BookingRequest request, BookingEvent event){
        return MessageBuilder.withPayload(event)
                .setHeader("booking_id", request.getBookingId())
                .setHeader(request.getHeader(), request)
                .build();
    }

}
