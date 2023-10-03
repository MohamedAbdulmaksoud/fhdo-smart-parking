package com.fhdo.bookingservice;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationMessageRequest;
import com.fhdo.bookingservice.domain.request.BookingCreationRequest;
import com.fhdo.bookingservice.domain.request.BookingMessageRequest;
import com.fhdo.bookingservice.entities.BookingEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class BuilderUtils {

    public static BookingConfirmationMessageRequest bookingConfirmationRequest(){
        return BookingConfirmationMessageRequest.builder()
                .bookingId(UUID.randomUUID())
                .userId(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"))
                .parkingId(UUID.fromString("b2ba6718-68d4-4524-aa14-5416eff38664"))
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusMinutes(30))
                .build();
    }

    public static BookingCreationRequest bookingCreationRequest(){
        return BookingCreationRequest.builder()
                .userId(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"))
                .parkingId(UUID.fromString("b2ba6718-68d4-4524-aa14-5416eff38664"))
                .parkingSpotId(1)
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusMinutes(30))
                .baseCost(BigDecimal.ONE)
                .build();
    }

    public static BookingEntity bookingEntity(UUID bookingId){
        return BookingEntity.builder()
                .bookingId(bookingId)
                .userId(UUID.fromString("2d7bb435-ce39-4bbd-9fd8-44377a4680dd"))
                .parkingId(UUID.fromString("b2ba6718-68d4-4524-aa14-5416eff38664"))
                .parkingSpotId(1)
                .vehicleId(UUID.randomUUID())
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusMinutes(30))
                .baseCost(BigDecimal.ONE)
                .state(BookingState.NEW)
                .build();
    }

    public static Message<BookingEvent> eventMessage(BookingMessageRequest request, BookingEvent event){
        return MessageBuilder.withPayload(event)
                .setHeader("booking_id", request.getBookingId())
                .setHeader(request.getHeader(), request)
                .build();
    }

}
