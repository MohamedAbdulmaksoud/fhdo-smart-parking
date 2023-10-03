package com.fhdo.bookingservice.services;

import com.fhdo.bookingservice.domain.request.BookingCreationRequest;
import com.fhdo.bookingservice.domain.response.BookingBaseResponse;
import com.fhdo.bookingservice.domain.response.BookingFullResponse;

import java.util.UUID;

public interface BookingService {

    BookingBaseResponse newBooking(BookingCreationRequest request);


    void confirmBooking(UUID bookingId);

    BookingFullResponse getBooking(UUID bookingId);

    void cancel(UUID bookingId);

    void delete(UUID bookingId);
}
