package com.fhdo.bookingservice.domain.response;

import com.fhdo.bookingservice.domain.BookingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingBaseResponse {

    private UUID bookingId;

    private UUID userId;

    private BookingState bookingState;
}
