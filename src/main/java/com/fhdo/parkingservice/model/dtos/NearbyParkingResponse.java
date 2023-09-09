package com.fhdo.parkingservice.model.dtos;

import com.fhdo.parkingservice.entities.Address;
import com.fhdo.parkingservice.model.ParkingSpotType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class NearbyParkingResponse {

    private String placeId;

    private String fullName;

    private Address address;

    private Integer totalCapacity;

    private Map<ParkingSpotType, BigDecimal> parkingHourlyRate;

    private Double distanceInMeters;

    private Boolean isOpen;

}
