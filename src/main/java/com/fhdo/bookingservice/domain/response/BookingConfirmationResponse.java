package com.fhdo.bookingservice.domain.response;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record BookingConfirmationResponse(@NotNull UUID bookingId,
                                          @NotNull UUID parkingLotId,
                                          @NotNull UUID parkingSpotId,
                                          @NotNull UUID vehicleId,
                                          @NotNull OffsetDateTime startTime,
                                          @NotNull OffsetDateTime endTime,
                                          @NotNull Boolean isConfirmed) {
}
