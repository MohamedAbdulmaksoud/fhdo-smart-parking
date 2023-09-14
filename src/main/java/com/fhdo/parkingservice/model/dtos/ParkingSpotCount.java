package com.fhdo.parkingservice.model.dtos;

import com.fhdo.parkingservice.model.ParkingSpotType;
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
