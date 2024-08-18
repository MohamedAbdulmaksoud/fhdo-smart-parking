package com.fhdo.parkingservice.service;

import com.fhdo.parkingservice.model.dtos.AvailableParkingSpacesResponse;
import com.fhdo.parkingservice.model.dtos.ParkingSpotCount;
import com.fhdo.parkingservice.repositories.ParkingSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSpaceService {

    private final ParkingSpaceRepository repository;

    public AvailableParkingSpacesResponse getAvailableSpaces(String parkingId) {
        List<ParkingSpotCount> spotCounts = repository.getAvailableSpaces(parkingId);
        List<Integer> internalIds = repository.findByIsReservedFalseAndIsOccupiedFalse(parkingId);
        return new AvailableParkingSpacesResponse(parkingId, internalIds.size(), spotCounts, internalIds);
    }
}
