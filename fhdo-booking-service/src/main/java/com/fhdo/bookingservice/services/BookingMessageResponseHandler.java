package com.fhdo.bookingservice.services;

import java.util.UUID;

public interface BookingMessageResponseHandler {

    void processConfirmationResult(UUID bookingId, Boolean isConfirmed);
    void processCancellationResult(UUID bookingId, Boolean isCancelled);
}
