package com.fhdo.ui.dto;

import lombok.Data;

@Data
public class ParkingSpotCount {
    private ParkingSpotType type;
    private Long availableSpots;

    public ParkingSpotCount(ParkingSpotType type, Long availableSpots) {
        this.type = type;
        this.availableSpots = availableSpots;
    }
}
