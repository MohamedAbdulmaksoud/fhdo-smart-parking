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
