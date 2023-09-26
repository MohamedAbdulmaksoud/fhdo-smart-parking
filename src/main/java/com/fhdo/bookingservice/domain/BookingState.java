package com.fhdo.bookingservice.domain;

/**
 * Enum class representing the possible states of a booking in a smart parking system.
 *
 * <p>Each state represents a specific stage in the lifecycle of a booking.
 *
 * <p>Possible states include:
 * <ul>
 *   <li>{@link BookingState#NEW}: The booking request has been received and a draft booking is created.</li>
 *   <li>{@link BookingState#PENDING_CONFIRMATION}: The user has confirmed his booking intention, booking is pending check with parking service.</li>
 *   <li>{@link BookingState#CONFIRMED}: The booking has been successfully confirmed with parking service.</li>
 *   <li>{@link BookingState#DECLINED}: The booking failed to get confirmation with parking service, the user has to request new booking</li>
 *   <li>{@link BookingState#ACTIVE}: The booking is currently active, and the user has parked their vehicle.</li>
 *   <li>{@link BookingState#OVERSTAY}: The user has exceeded the allowed booking duration, and additional charges or penalties may apply.</li>
 *   <li>{@link BookingState#COMPLETED}: The booking has been successfully completed, and the user has left the parking spot.</li>
 *   <li>{@link BookingState#CANCELLED}: The booking has been canceled by the user or the system.</li>
 * </ul>
 */
public enum BookingState {
    NEW,
    PENDING_CONFIRMATION, // Sending the reservation message to the parking service to set isReserved on a parkingSpot to true
    CONFIRMED,
    DECLINED,            // If a parking spot has an existing booking
    ACTIVE,             // parking spot has ongoing booking and parking spot is occupied
    OVERSTAY,           // Booking exceeding end time without leaving parking spot
    COMPLETED,
    CANCELLED
}
