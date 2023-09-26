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
import java.util.Optional;

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
                // user chooses a parking lot to book a parking slot
                .withExternal()
                .source(BookingState.NEW)
                .target(BookingState.PENDING_CONFIRMATION)
                .event(BookingEvent.BOOK_PARKING_SLOT)
                .guard((stateContext -> stateContext.getMessageHeaders().containsKey(BookingConfirmationRequest.HEADER_NAME)))
                .action(confirmBookingAction)
                .and()
                // parking service successfully reserves the parking slot
                .withExternal()
                .source(BookingState.PENDING_CONFIRMATION)
                .target(BookingState.CONFIRMED)
                .event(BookingEvent.BOOKING_CONFIRMED)
                .and()
                // any exception thrown during confirmBookingAction or received from parking service
                .withExternal()
                .source(BookingState.PENDING_CONFIRMATION)
                .target(BookingState.DECLINED)
                .event(BookingEvent.BOOKING_FAILED)
                .and()
                // user request to cancel his booking
                .withExternal()
                .source(BookingState.NEW)
                .target(BookingState.CANCELLED)
                .event(BookingEvent.BOOKING_CANCELLED)
                .and()
                // receive an event that the reserved parking spot is now occupied
                .withExternal()
                .source(BookingState.CONFIRMED)
                .target(BookingState.ACTIVE)
                .event(BookingEvent.VEHICLE_PARKED)
                .and()
                // user cancels his booking
                .withExternal()
                .source(BookingState.CONFIRMED)
                .target(BookingState.CANCELLED)
                .event(BookingEvent.BOOKING_CANCELLED)
                .and()
                // active booking (parking spot still occupied) exceeds allocated time
                .withExternal()
                .source(BookingState.ACTIVE)
                .target(BookingState.OVERSTAY)
                .event(BookingEvent.OVERSTAY_OCCURRED)
                .and()
                // when parking spot isOccupied -> available. If overstay, calculate penalties
                .withExternal()
                .source(BookingState.ACTIVE)
                .target(BookingState.COMPLETED)
                .event(BookingEvent.BOOKING_COMPLETED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<BookingState, BookingEvent> config) throws Exception {
        StateMachineListenerAdapter<BookingState, BookingEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<BookingState, BookingEvent> from, State<BookingState, BookingEvent> to) {
                Optional.ofNullable(from).ifPresent((sm) ->
                        log.info("stateChange from: {} to {}", from.getId().toString(), to.getId().toString())
                );
            }
        };
        config.withConfiguration().listener(adapter);

    }
}
