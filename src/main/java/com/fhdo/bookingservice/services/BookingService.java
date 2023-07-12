package com.fhdo.bookingservice.services;

import com.fhdo.bookingservice.domain.request.BookingRequest;
import com.fhdo.bookingservice.entities.BookingEntity;

import java.util.UUID;

public interface BookingService {

    BookingEntity newBooking(BookingEntity booking);

    void validate(BookingRequest request);

    void cancel(UUID bookingId);

    void processConfirmationResult(UUID bookingId, Boolean isValid);

}
