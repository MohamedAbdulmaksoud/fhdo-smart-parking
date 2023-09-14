package com.fhdo.parkingservice.model.dtos;

import java.util.List;

public record AvailableParkingSpacesResponse(String parkingId, Long totalOccupancy, List<ParkingSpotCount> availableSpots) {

}
