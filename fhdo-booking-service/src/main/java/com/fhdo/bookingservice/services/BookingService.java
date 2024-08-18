package com.fhdo.bookingservice.services;

import com.fhdo.bookingservice.domain.request.BookingCreationRequest;
import com.fhdo.bookingservice.domain.response.BookingBaseResponse;
import com.fhdo.bookingservice.domain.response.BookingFullResponse;

import java.util.UUID;
/**
 * Service interface for managing booking operations.
 *
 * The {@code BookingService} interface defines methods for handling various booking-related operations,
 * including creating, confirming, retrieving, canceling, and deleting bookings. Implementations of this interface
 * are responsible for managing the lifecycle of a booking and interacting with the underlying data store.
 *
 * <p>This interface provides the following key methods:
 * <ul>
 *     <li>{@link #newBooking(BookingCreationRequest)} - Creates a new booking based on the provided {@code BookingCreationRequest}.</li>
 *     <li>{@link #confirmBooking(UUID)} - Confirms a booking identified by the provided booking ID.</li>
 *     <li>{@link #getBooking(UUID)} - Retrieves detailed information about a booking identified by the provided booking ID.</li>
 *     <li>{@link #cancel(UUID)} - Cancels a booking identified by the provided booking ID.</li>
 *     <li>{@link #delete(UUID)} - Deletes a booking identified by the provided booking ID.</li>
 * </ul>
 * </p>
 *
 * @see com.fhdo.bookingservice.domain.request.BookingCreationRequest
 * @see com.fhdo.bookingservice.domain.response.BookingBaseResponse
 * @see com.fhdo.bookingservice.domain.response.BookingFullResponse
 */

public interface BookingService {

    BookingBaseResponse newBooking(BookingCreationRequest request);


    void confirmBooking(UUID bookingId);

    BookingFullResponse getBooking(UUID bookingId);

    void cancel(UUID bookingId);

    void delete(UUID bookingId);
}
