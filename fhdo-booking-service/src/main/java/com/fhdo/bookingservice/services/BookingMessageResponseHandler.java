package com.fhdo.bookingservice.services;

import java.util.UUID;
/**
 * Interface for handling booking message responses.
 *
 * The {@code BookingMessageResponseHandler} interface defines methods for processing booking message responses
 * related to booking confirmation and cancellation. Implementations of this interface are responsible for handling
 * the results of booking operations by updating the booking status based on the received responses.
 *
 * <p>This interface provides two key methods:
 * <ul>
 *     <li>{@link #processConfirmationResult(UUID, Boolean)} - Processes the result of a booking confirmation operation.</li>
 *     <li>{@link #processCancellationResult(UUID, Boolean)} - Processes the result of a booking cancellation operation.</li>
 * </ul>
 * </p>
 *
 * @see com.fhdo.bookingservice.domain.response.BookingConfirmationResponse
 * @see com.fhdo.bookingservice.domain.response.BookingCancellationMessageResponse
 */

public interface BookingMessageResponseHandler {

    void processConfirmationResult(UUID bookingId, Boolean isConfirmed);
    void processCancellationResult(UUID bookingId, Boolean isCancelled);
}
