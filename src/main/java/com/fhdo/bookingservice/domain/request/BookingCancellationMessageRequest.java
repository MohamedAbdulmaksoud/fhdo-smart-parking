package com.fhdo.bookingservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCancellationMessageRequest implements BookingMessageRequest, Serializable {

    public static final String HEADER_NAME = "cancellation_request";

    static final long serialVersionUID = -609552143769850745L;

    private UUID bookingId;
    private UUID parkingId;
    private Integer parkingSpotId;


    @Override
    public String getHeader() {
        return HEADER_NAME;
    }
}
