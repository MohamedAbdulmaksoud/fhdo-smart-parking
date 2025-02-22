package com.fhdo.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ParkingLotEntity {

    private UUID parkingId;

    private String placeId;

    private String fullName;

    private Address address;

    private Geolocation geoLocation;

    private List<ParkingSpaceEntity> parkingSpaces;

    private Integer totalCapacity;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private ParkingLotOnwershipType ownershipType;

    private Map<String, BigDecimal> hourlyRates = new HashMap<>();

    private Double distance;
}
