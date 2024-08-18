package com.fhdo.bookingservice.domain.request;

import java.util.UUID;

public interface BookingMessageRequest {
    String getHeader();

    UUID getBookingId();
}
