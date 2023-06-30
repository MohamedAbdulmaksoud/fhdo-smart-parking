package com.fhdo.bookingservice.config;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Slf4j
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<BookingState, BookingEvent> {
    @Override
    public void configure(StateMachineStateConfigurer<BookingState, BookingEvent> states) throws Exception {
        states.withStates()
                .initial(BookingState.NEW)
                .states(EnumSet.allOf(BookingState.class))
                .end(BookingState.CANCELLED)
                .end(BookingState.PAID);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<BookingState, BookingEvent> transitions) throws Exception {
        transitions.withExternal().source(BookingState.NEW).target(BookingState.CONFIRMED).event(BookingEvent.CONFIRMATION_APPROVED)
                .and()
                .withExternal().source(BookingState.NEW).target(BookingState.DECLINED).event(BookingEvent.CONFIRMATION_DECLINED)
                .and()
                .withExternal().source(BookingState.NEW).target(BookingState.CANCELLED).event(BookingEvent.BOOKING_CANCELLED)
                .and()
                .withExternal().source(BookingState.CONFIRMED).target(BookingState.ACTIVE).event(BookingEvent.VEHICLE_PARKED)
                .and()
                .withExternal().source(BookingState.CONFIRMED).target(BookingState.CANCELLED).event(BookingEvent.BOOKING_CANCELLED)
                .and()
                .withExternal().source(BookingState.ACTIVE).target(BookingState.OVERSTAY).event(BookingEvent.OVERSTAY_OCCURRED)
                .and()
                .withExternal().source(BookingState.ACTIVE).target(BookingState.PENDING_EXTENSION).event(BookingEvent.EXTENSION_REQUESTED)
                .and()
                .withExternal().source(BookingState.ACTIVE).target(BookingState.COMPLETED).event(BookingEvent.BOOKING_COMPLETED)
                .and()
                .withExternal().source(BookingState.PENDING_EXTENSION).target(BookingState.ACTIVE).event(BookingEvent.EXTENSION_CONFIRMED)
                .and()
                .withExternal().source(BookingState.COMPLETED).target(BookingState.PAID).event(BookingEvent.PAYMENT_SUCCESSFUL);
    }
}
