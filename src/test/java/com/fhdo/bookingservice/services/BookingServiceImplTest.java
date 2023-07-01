package com.fhdo.bookingservice.services;

import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.entities.BookingEntity;
import com.fhdo.bookingservice.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
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
                .startTime(LocalDateTime.now())
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
}