package com.fhdo.bookingservice.domain.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCancellationMessageRequest implements BookingMessageRequest {

    @JsonIgnore
    public static final String HEADER_NAME = "cancellation_request";

    private UUID bookingId;
    private UUID parkingId;
    private Integer parkingSpotId;


    @Override
    public String getHeader() {
        return HEADER_NAME;
    }
}
