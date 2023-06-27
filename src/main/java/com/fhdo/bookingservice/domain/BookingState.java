package com.fhdo.bookingservice.domain;

/**
 * Enum class representing the possible states of a booking in a smart parking system.
 *
 * <p>Each state represents a specific stage in the lifecycle of a booking.
 *
 * <p>Possible states include:
 * <ul>
 *   <li>{@link BookingState#NEW}: The booking request has been received but not processed yet.</li>
 *   <li>{@link BookingState#CONFIRMED}: The booking has been successfully confirmed after payment.</li>
 *   <li>{@link BookingState#ACTIVE}: The booking is currently active, and the user has parked their vehicle.</li>
 *   <li>{@link BookingState#PENDING_EXTENSION}: The user has requested to extend the booking duration, and it is pending approval.</li>
 *   <li>{@link BookingState#OVERSTAY}: The user has exceeded the allowed booking duration, and additional charges or penalties may apply.</li>
 *   <li>{@link BookingState#COMPLETED}: The booking has been successfully completed, and the user has left the parking spot.</li>
 *   <li>{@link BookingState#CANCELLED}: The booking has been canceled by the user or the system.</li>
 *   <li>{@link BookingState#PAID}: The booking was paid by user and is considered in a closed state.</li>
 * </ul>
 */
public enum BookingState {
    NEW,
    CONFIRMED,
    ACTIVE,
    PENDING_EXTENSION,
    OVERSTAY,
    COMPLETED,
    CANCELLED,
    PAID
}
