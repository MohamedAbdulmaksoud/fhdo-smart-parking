package com.fhdo.bookingservice.domain.response;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class BookingConfirmationResponse extends BookingBaseResponse {

    private UUID parkingLotId;

    private UUID parkingSpotId;

    private UUID vehicleId;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    private Boolean isConfirmed;

}
