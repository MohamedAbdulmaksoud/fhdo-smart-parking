package com.fhdo.bookingservice.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BookingValidationResponse(@NotNull UUID bookingId, @NotNull Boolean isValid, @NotNull UUID parkingLotId, @NotNull UUID parkingSpotId) {
}
