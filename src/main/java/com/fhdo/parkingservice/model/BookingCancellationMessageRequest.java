package com.fhdo.parkingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCancellationMessageRequest {

    public static final String HEADER_NAME = "cancellation_request";

    private UUID bookingId;
    private UUID parkingId;
    private Integer parkingSpotId;

}
