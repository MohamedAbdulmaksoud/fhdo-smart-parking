package com.fhdo.parkingservice.service;

import com.fhdo.parkingservice.model.BookingCancellationMessageRequest;
import com.fhdo.parkingservice.model.BookingCancellationMessageResponse;
import com.fhdo.parkingservice.model.BookingConfirmationMessageRequest;
import com.fhdo.parkingservice.model.BookingConfirmationResponse;

public interface BookingMessageRequestHandler {

    BookingConfirmationResponse processConfirmationRequest(BookingConfirmationMessageRequest request);
    BookingCancellationMessageResponse processCancellationRequest(BookingCancellationMessageRequest request);
}
