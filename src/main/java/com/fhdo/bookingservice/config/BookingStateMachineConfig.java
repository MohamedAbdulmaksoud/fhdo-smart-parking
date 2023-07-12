package com.fhdo.bookingservice.config;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationRequest;
import com.fhdo.bookingservice.domain.sm.actions.ConfirmBookingAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
/*
* https://docs.spring.io/spring-statemachine/docs/3.2.x/reference/#sm-config
* */
@Slf4j
@RequiredArgsConstructor
@EnableStateMachineFactory
@Configuration
public class BookingStateMachineConfig extends EnumStateMachineConfigurerAdapter<BookingState, BookingEvent> {

    private final ConfirmBookingAction confirmBookingAction;
    @Override
    public void configure(StateMachineStateConfigurer<BookingState, BookingEvent> states) throws Exception {
        states.withStates()
                .initial(BookingState.NEW)
                .states(EnumSet.allOf(BookingState.class))
                .end(BookingState.CANCELLED)
                .end(BookingState.COMPLETED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<BookingState, BookingEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(BookingState.NEW)
                .target(BookingState.PENDING_CONFIRMATION)
                .event(BookingEvent.CHECK_VALIDITY)
                .guard((stateContext -> stateContext.getMessageHeaders().containsKey(BookingConfirmationRequest.HEADER_NAME)))
                .action(confirmBookingAction)
                .and()

                .withExternal()
                .source(BookingState.PENDING_CONFIRMATION)
                .target(BookingState.PENDING_CONFIRMATION)
                .event(BookingEvent.BOOK_PARKING_SLOT)
                .guard((stateContext -> stateContext.getMessageHeaders().containsKey(BookingConfirmationRequest.HEADER_NAME)))
                .action(confirmBookingAction)
                .and()

                .withExternal()
                .source(BookingState.PENDING_CONFIRMATION)
                .target(BookingState.CONFIRMED)
                .event(BookingEvent.BOOKING_CONFIRMED)
                .and()

                .withExternal()
                .source(BookingState.PENDING_CONFIRMATION)
                .target(BookingState.DECLINED)
                .event(BookingEvent.BOOKING_FAILED)
                .and()

                .withExternal()
                .source(BookingState.NEW)
                .target(BookingState.CANCELLED)
                .event(BookingEvent.BOOKING_CANCELLED)
                .and()

                .withExternal()
                .source(BookingState.CONFIRMED)
                .target(BookingState.ACTIVE)
                .event(BookingEvent.VEHICLE_PARKED)
                .and()

                .withExternal()
                .source(BookingState.CONFIRMED)
                .target(BookingState.CANCELLED)
                .event(BookingEvent.BOOKING_CANCELLED)
                .and()

                .withExternal()
                .source(BookingState.ACTIVE)
                .target(BookingState.OVERSTAY)
                .event(BookingEvent.OVERSTAY_OCCURRED)
                .and()

                .withExternal()
                .source(BookingState.ACTIVE)
                .target(BookingState.PENDING_EXTENSION)
                .event(BookingEvent.EXTENSION_REQUESTED)
                .and()

                .withExternal()
                .source(BookingState.ACTIVE)
                .target(BookingState.COMPLETED)
                .event(BookingEvent.BOOKING_COMPLETED)
                .and()

                .withExternal()
                .source(BookingState.PENDING_EXTENSION)
                .target(BookingState.ACTIVE)
                .event(BookingEvent.EXTENSION_CONFIRMED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<BookingState, BookingEvent> config) throws Exception {
        StateMachineListenerAdapter<BookingState, BookingEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<BookingState, BookingEvent> from, State<BookingState, BookingEvent> to) {
                log.info("stateChange from: {} to {}", from.getId().toString(), to.getId().toString());
            }
        };
        config.withConfiguration().listener(adapter);

    }
}
