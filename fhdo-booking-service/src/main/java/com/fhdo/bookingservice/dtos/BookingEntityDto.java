package com.fhdo.bookingservice.dtos;

import com.fhdo.bookingservice.domain.BookingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.fhdo.bookingservice.entities.BookingEntity}
 */
@Data
@AllArgsConstructor
@Builder
public class BookingEntityDto implements Serializable {
    UUID bookingId;
    BookingState state;
    UUID userId;
    UUID parkingLotId;
    UUID parkingSpotId;
    OffsetDateTime startTime;
    OffsetDateTime endTime;
    BigDecimal baseCost;
    BigDecimal totalCost;
    Timestamp metadataCreatedOn;
    String metadataCreatedBy;
    Timestamp metadataUpdatedOn;
    String metadataUpdatedBy;
}