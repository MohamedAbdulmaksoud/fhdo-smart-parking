package com.fhdo.parkingservice.model.dtos;

import com.fhdo.parkingservice.entities.Address;
import com.fhdo.parkingservice.entities.Geolocation;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class NearbyParkingResponse {

    private String placeId;

    private Geolocation geoLocation;

    private String fullName;

    private Address address;

    private Integer totalCapacity;

    private Map<String, BigDecimal> hourlyRates;

    private String distance;

    private String timeFromOrigin;

    private String timeToDestination;

    private Long totalTripTime;

    private Boolean isOpen;

}
