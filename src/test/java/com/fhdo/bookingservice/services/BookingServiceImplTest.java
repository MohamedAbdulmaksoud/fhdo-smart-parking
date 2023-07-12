package com.fhdo.bookingservice.services;

import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationRequest;
import com.fhdo.bookingservice.entities.BookingEntity;
import com.fhdo.bookingservice.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@SpringBootTest
class BookingServiceImplTest {

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingRepository bookingRepository;

    BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        bookingEntity = BookingEntity.builder()
                .startTime(OffsetDateTime.now())
                .bookingDuration(Duration.ofHours(1L))
                .userId(UUID.randomUUID())
                .build();
    }

    @Test
    @Transactional
    void cancel() {
        BookingEntity booking = bookingService.newBooking(bookingEntity);
        bookingService.cancel(booking.getBookingId());
        BookingEntity cancelledBooking = bookingRepository.getReferenceById(booking.getBookingId());
        Assertions.assertEquals(BookingState.CANCELLED, cancelledBooking.getState());
    }

    @Test
    @Transactional
    void validate() {
        BookingEntity booking = bookingService.newBooking(bookingEntity);
        BookingConfirmationRequest request = BookingConfirmationRequest.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUserId())
                .parkingLotId(UUID.randomUUID())
                .startTime(OffsetDateTime.now())
                .endTime(OffsetDateTime.now().plusMinutes(30))
                .build();

        bookingService.validate(request);
        BookingEntity pendingBooking = bookingRepository.getReferenceById(booking.getBookingId());
        Assertions.assertEquals(BookingState.PENDING_CONFIRMATION, pendingBooking.getState());

    }
}