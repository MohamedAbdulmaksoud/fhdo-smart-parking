package com.fhdo.bookingservice.domain.response;

import com.fhdo.bookingservice.entities.Address;
import com.fhdo.bookingservice.entities.Geolocation;
import lombok.Data;

import java.util.UUID;

@Data
public class ParkingLotResponse {

    private UUID parkingId;

    private String fullName;

    private Address address;

    private Geolocation geoLocation;
}
