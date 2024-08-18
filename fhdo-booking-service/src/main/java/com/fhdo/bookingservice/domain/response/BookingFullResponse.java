package com.fhdo.bookingservice.domain.response;

import com.fhdo.bookingservice.entities.Audit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BookingFullResponse extends BookingBaseResponse{

    private UUID parkingId;

    private ParkingLotResponse parkingLot;

    private Integer parkingSpotId;

    private UUID vehicleId;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    private Duration bookingDuration;

    private BigDecimal baseCost;

    private BigDecimal totalPenalties;

    private BigDecimal totalCost;

    private Audit metadata;

}
