package com.fhdo.ui.service;

import com.fhdo.ui.config.DevConfig;
import com.fhdo.ui.dto.AvailableParkingSpacesResponse;
import com.fhdo.ui.dto.ParkingSpaceEntity;
import com.fhdo.ui.dto.ParkingSpotCount;
import com.fhdo.ui.dto.ParkingSpotType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Profile("dev")
public class DevParkingSpaceService implements ParkingSpaceService {

    private final DevConfig devConfig;

    public DevParkingSpaceService(DevConfig devConfig) {
        this.devConfig = devConfig;
    }

    @Override
    public AvailableParkingSpacesResponse getAvailableSpaces(String parkingId) {
        // Filter parking spaces by parkingId and availability
        List<ParkingSpaceEntity> parkingSpaces = devConfig.getParkingSpaces().stream()
                .filter(space -> space.getParkingId().toString().equals(parkingId) && !space.getIsOccupied())
                .toList();

        // Group by ParkingSpotType and count
        Map<ParkingSpotType, Long> spotCountMap = parkingSpaces.stream()
                .collect(Collectors.groupingBy(ParkingSpaceEntity::getParkingSpotType, Collectors.counting()));

        // Create ParkingSpotCount list
        List<ParkingSpotCount> spotCounts = spotCountMap.entrySet().stream()
                .map(entry -> new ParkingSpotCount(entry.getKey(), entry.getValue()))
                .toList();

        // Extract available spot IDs
        List<Integer> availableSpotIds = parkingSpaces.stream()
                .filter(space -> !space.getIsReserved())
                .map(ParkingSpaceEntity::getInternalId)
                .toList();

        // Construct AvailableParkingSpacesResponse
        return new AvailableParkingSpacesResponse(parkingId, parkingSpaces.size(), spotCounts, availableSpotIds);
    }
}
