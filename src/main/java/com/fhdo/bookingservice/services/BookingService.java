package com.fhdo.bookingservice.services;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.entities.BookingEntity;
import org.springframework.statemachine.StateMachine;

import java.util.UUID;

public interface BookingService {

    BookingEntity newBooking(BookingEntity booking);

    StateMachine<BookingState, BookingEvent> confirm(UUID bookingId);

    StateMachine<BookingState, BookingEvent> cancel(UUID bookingId);
}
