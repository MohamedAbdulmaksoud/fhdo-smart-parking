package com.fhdo.bookingservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCancellationMessageResponse {

    private UUID bookingId;

    private Boolean isCancelled;
}
