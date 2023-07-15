package com.fhdo.bookingservice.config;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@SpringBootTest
class BookingStateMachineConfigTest {

    @Autowired
    StateMachineFactory<BookingState, BookingEvent> factory;

    @Test
    void testBookingStateMachine() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        BookingConfirmationRequest request = BookingConfirmationRequest.builder()
                .bookingId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .parkingLotId(UUID.randomUUID())
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusMinutes(30))
                .build();

        sm.startReactively().subscribe();

        Message<BookingEvent> msg = MessageBuilder.withPayload(BookingEvent.CHECK_VALIDITY)
                .setHeader("booking_id", request.getBookingId())
                .setHeader(request.getHeader(),request)
                .build();

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.PENDING_CONFIRMATION, sm.getState().getId());

    }

}