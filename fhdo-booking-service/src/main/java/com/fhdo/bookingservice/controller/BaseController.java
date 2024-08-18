package com.fhdo.bookingservice.controller;

import com.fhdo.bookingservice.domain.request.BookingCreationRequest;
import com.fhdo.bookingservice.domain.response.BookingBaseResponse;
import com.fhdo.bookingservice.domain.response.BookingFullResponse;
import com.fhdo.bookingservice.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
/**
 * REST controller for managing bookings in the Booking Service.
 *
 * The {@code BaseController} class provides endpoints for creating, confirming, canceling, retrieving, and deleting
 * bookings. It interacts with the {@link BookingService} to perform booking operations and respond to client requests.
 *
 * <p>Endpoints:
 * <ul>
 *     <li>{@code POST /api/v1/bookings/create} - Creates a new booking based on the provided {@link BookingCreationRequest}.</li>
 *     <li>{@code POST /api/v1/bookings/{bookingId}/book} - Confirms a booking with the specified {@code bookingId}.</li>
 *     <li>{@code POST /api/v1/bookings/{bookingId}/cancel} - Cancels a booking with the specified {@code bookingId}.</li>
 *     <li>{@code GET /api/v1/bookings/{bookingId}} - Retrieves detailed information about a booking with the specified {@code bookingId}.</li>
 *     <li>{@code DELETE /api/v1/bookings/{bookingId}} - Deletes a booking with the specified {@code bookingId}.</li>
 * </ul>
 * </p>
 *
 * @see BookingService
 * @see BookingCreationRequest
 * @see BookingBaseResponse
 * @see BookingFullResponse
 */

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Slf4j
public class BaseController {

    private final BookingService bookingService;

    @PostMapping("/create")
    public BookingBaseResponse createBooking (@RequestBody BookingCreationRequest request){
        return bookingService.newBooking(request);
    }

    @PostMapping("/{bookingId}/book")
    public void confirmBooking (@PathVariable("bookingId") UUID bookingId){
        bookingService.confirmBooking(bookingId);
    }

    @PostMapping("/{bookingId}/cancel")
    public void cancelBooking (@PathVariable("bookingId") UUID bookingId){
        bookingService.cancel(bookingId);
    }

    @GetMapping("/{bookingId}")
    public BookingFullResponse getBooking (@PathVariable("bookingId") UUID bookingId){
        return bookingService.getBooking(bookingId);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteBooking(@PathVariable("bookingId") UUID bookingId){
         bookingService.delete(bookingId);
    }
}
