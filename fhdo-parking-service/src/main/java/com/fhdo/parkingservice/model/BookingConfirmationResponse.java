package com.fhdo.parkingservice.model;

import java.util.UUID;

public record BookingConfirmationResponse(UUID bookingId, Boolean isConfirmed) {
}
