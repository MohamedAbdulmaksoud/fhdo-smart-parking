package com.fhdo.bookingservice.domain.request;

import java.util.UUID;

public interface BookingRequest {
    String getHeader();

    UUID getBookingId();

    UUID getUserId();
}
