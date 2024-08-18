package com.fhdo.bookingservice.config;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.sm.actions.CancelBookingAction;
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

/**
 * Configuration class for managing the lifecycle of parking slot bookings using a state machine.
 *
 * The {@code BookingStateMachineConfig} class defines the states that a booking can transition through,
 * including {@code NEW}, {@code PENDING_CONFIRMATION}, {@code CONFIRMED}, {@code ACTIVE}, {@code OVERSTAY},
 * {@code CANCELLED}, and {@code COMPLETED}. It specifies the events that trigger transitions between these states,
 * such as booking confirmation, cancellation, and completion. Additionally, it configures actions to be executed
 * during these transitions, including {@code confirmBookingAction} and {@code cancelBookingAction}.
 *
 * By leveraging Spring State Machine, this class ensures that the booking process is managed in a consistent
 * and efficient manner, facilitating smooth transitions and handling of various booking scenarios.
 *
 * <p>Key methods:
 * <ul>
 *     <li>{@code configure(StateMachineStateConfigurer<BookingState, BookingEvent> states)} - Configures the states and their end states.</li>
 *     <li>{@code configure(StateMachineTransitionConfigurer<BookingState, BookingEvent> transitions)} - Defines state transitions and associated events.</li>
 *     <li>{@code configure(StateMachineConfigurationConfigurer<BookingState, BookingEvent> config)} - Sets up the state machine's configuration and logging.</li>
 * </ul>
 * </p>
 * <a href="https://docs.spring.io/spring-statemachine/docs/3.2.x/reference/#sm-config">...</a>
 */

@Slf4j
@RequiredArgsConstructor
@EnableStateMachineFactory
@Configuration
public class BookingStateMachineConfig extends EnumStateMachineConfigurerAdapter<BookingState, BookingEvent> {

    private final ConfirmBookingAction confirmBookingAction;
    private final CancelBookingAction cancelBookingAction;
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
                .action(confirmBookingAction)
                .and()

                .withExternal()
                .source(BookingState.NEW)
                .target(BookingState.NEW)
                .event(BookingEvent.BOOKING_CANCELLED)
                .action(cancelBookingAction)
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
                .source(BookingState.PENDING_CONFIRMATION)
                .target(BookingState.PENDING_CONFIRMATION)
                .event(BookingEvent.BOOKING_CANCELLED)
                .action(cancelBookingAction)
                .and()
                // TODO: RabbitMQListener receive an event that the reserved parking spot is now occupied (e.g through SUMO)
                .withExternal()
                .source(BookingState.CONFIRMED)
                .target(BookingState.ACTIVE)
                .event(BookingEvent.VEHICLE_PARKED)
                .and()

                .withExternal()
                .source(BookingState.CONFIRMED)
                .target(BookingState.CONFIRMED)
                .action(cancelBookingAction)
                .event(BookingEvent.BOOKING_CANCELLED)
                .and()
                // active booking (parking spot still occupied) exceeds allocated time
                .withExternal()
                .source(BookingState.ACTIVE)
                .target(BookingState.OVERSTAY)
                .event(BookingEvent.OVERSTAY_OCCURRED)
                .and()
                // TODO: RabbitMQListener receive an event that the reserved parking spot is now available (e.g through SUMO). If overstay, calculate penalties
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
