package com.fhdo.parkingservice.model;

import java.util.UUID;

public record BookingCancellationMessageResponse(UUID bookingId, Boolean isCancelled) {
}
