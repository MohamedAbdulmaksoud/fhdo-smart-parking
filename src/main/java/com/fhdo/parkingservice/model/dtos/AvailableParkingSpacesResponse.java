package com.fhdo.parkingservice.model.dtos;

import java.util.List;

public record AvailableParkingSpacesResponse(String parkingId, Integer totalOccupancy, List<ParkingSpotCount> availableSpots, List<Integer> availableSpotsIds) {

}
