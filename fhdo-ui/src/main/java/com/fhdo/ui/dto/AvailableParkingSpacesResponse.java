package com.fhdo.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public final class AvailableParkingSpacesResponse {
    private String parkingId;
    private int totalOccupancy;
    private List<ParkingSpotCount> availableSpots;
    private List<Integer> availableSpotsIds;

}
