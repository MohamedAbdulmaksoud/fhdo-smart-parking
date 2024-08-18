package com.fhdo.bookingservice.services;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.entities.BookingEntity;
import com.fhdo.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
/**
 * An interceptor for handling state changes in the booking state machine.
 *
 * {@code BookingStateChangeInterceptor} is responsible for persisting state changes of the booking entity
 * into the database. It extends {@link StateMachineInterceptorAdapter} and provides a custom implementation
 * to update the booking state in the repository whenever the state machine transitions to a new state.
 *
 * <p>The interceptor overrides the {@code postStateChange} method to:
 * <ul>
 *     <li>Retrieve the booking ID from the state machine message headers.</li>
 *     <li>Fetch the corresponding {@link BookingEntity} from the repository.</li>
 *     <li>Update the state of the booking entity.</li>
 *     <li>Save the updated booking entity back to the repository.</li>
 * </ul>
 *
 * <p>This interceptor is automatically registered with the state machine and gets invoked during state transitions.
 * It ensures that the state changes are accurately reflected in the database.
 *
 * @see com.fhdo.bookingservice.domain.BookingEvent
 * @see com.fhdo.bookingservice.domain.BookingState
 * @see com.fhdo.bookingservice.entities.BookingEntity
 * @see com.fhdo.bookingservice.repository.BookingRepository
 * @see org.springframework.statemachine.StateMachine
 */
@RequiredArgsConstructor
@Component
public class BookingStateChangeInterceptor extends StateMachineInterceptorAdapter<BookingState, BookingEvent> {

    private final BookingRepository bookingRepository;

    /*
     * Persist state change in database
     * */
    @Override
    public void postStateChange(State<BookingState, BookingEvent> state, Message<BookingEvent> message, Transition<BookingState, BookingEvent> transition, StateMachine<BookingState, BookingEvent> stateMachine, StateMachine<BookingState, BookingEvent> rootStateMachine) {
        Optional.ofNullable(message).flatMap(msg -> Optional.ofNullable((UUID) msg.getHeaders().getOrDefault(DefaultBookingService.BOOKING_ID_HEADER, -1L))).ifPresent(bookingId -> {
            BookingEntity booking = bookingRepository.getReferenceById(bookingId);
            booking.setState(state.getId());
            bookingRepository.saveAndFlush(booking);
        });
    }
}
