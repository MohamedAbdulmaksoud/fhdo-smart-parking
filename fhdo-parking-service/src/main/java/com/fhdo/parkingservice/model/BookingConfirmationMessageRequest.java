package com.fhdo.parkingservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingConfirmationMessageRequest {

    private UUID bookingId;
    private UUID userId;
    private UUID parkingId;
    private Integer parkingSpotId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

}
