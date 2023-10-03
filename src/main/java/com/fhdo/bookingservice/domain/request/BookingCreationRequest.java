package com.fhdo.bookingservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreationRequest {
    private UUID userId;
    private UUID parkingId;
    private Integer parkingSpotId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private BigDecimal baseCost;
}
