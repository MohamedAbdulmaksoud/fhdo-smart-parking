package com.fhdo.bookingservice.config;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import reactor.core.publisher.Mono;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<BookingState, BookingEvent> factory;

    @Test
    void testBookingStateMachine() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        sm.startReactively().subscribe();
        sm.sendEvent(Mono.just(MessageBuilder
                .withPayload(BookingEvent.CONFIRMATION_APPROVED)
                .build()))
                .subscribe();
        Assertions.assertEquals(BookingState.CONFIRMED, sm.getState().getId());

    }

}