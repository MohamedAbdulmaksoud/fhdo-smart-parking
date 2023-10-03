package com.fhdo.parkingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingConfirmationMessageRequest {

    public static final String HEADER_NAME = "confirmation_request";

    private UUID bookingId;
    private UUID userId;
    private UUID parkingId;
    private Integer parkingSpotId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

}
