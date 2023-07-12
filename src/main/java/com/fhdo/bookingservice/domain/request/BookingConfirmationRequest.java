package com.fhdo.bookingservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingConfirmationRequest implements BookingRequest, Serializable {

    public static final String HEADER_NAME = "confirmation_request";

    static final long serialVersionUID = -7142764179621490243L;

    private UUID bookingId;
    private UUID userId;
    private UUID parkingLotId;
    private UUID parkingSpotId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

    @Override
    public String getHeader() {
        return HEADER_NAME;
    }
}
