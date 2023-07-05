package com.fhdo.bookingservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.fhdo.bookingservice.entities.BookingEntity}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingValidationRequest implements Serializable {

    static final long serialVersionUID = 3870498450574080649L;
    private UUID bookingId;
    private UUID userId;
    private UUID parkingLotId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
}