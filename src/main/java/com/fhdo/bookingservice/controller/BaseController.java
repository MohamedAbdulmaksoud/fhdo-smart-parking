package com.fhdo.bookingservice.controller;

import com.fhdo.bookingservice.domain.request.BookingCreationRequest;
import com.fhdo.bookingservice.domain.response.BookingBaseResponse;
import com.fhdo.bookingservice.domain.response.BookingFullResponse;
import com.fhdo.bookingservice.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
