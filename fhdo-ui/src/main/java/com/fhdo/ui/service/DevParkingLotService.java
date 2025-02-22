package com.fhdo.ui.service;

import com.fhdo.ui.config.DevConfig;
import com.fhdo.ui.dto.NearbyParkingRequest;
import com.fhdo.ui.dto.NearbyParkingResponse;
import com.fhdo.ui.dto.ParkingLotEntity;
import com.fhdo.ui.dto.Geolocation;
import com.fhdo.ui.dto.SortingPreference;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("dev")
public class DevParkingLotService implements ParkingLotService {

    private final DevConfig devConfig;

    public DevParkingLotService(DevConfig devConfig) {
        this.devConfig = devConfig;
    }

    @Override
    public List<NearbyParkingResponse> findNearbyParking(NearbyParkingRequest request) {
        List<ParkingLotEntity> parkingLots = devConfig.getParkingLots();
        Geolocation destination = request.getDestination();

        // Filter and map the nearby parking lots
        List<NearbyParkingResponse> responses = parkingLots.stream()
                .filter(lot -> calculateDistance(lot.getGeoLocation(), destination) <= request.getDistanceInMeters())
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        // Sort based on the preference
        if (request.getPreference() == SortingPreference.LEAST_WALKING_DISTANCE) {
            responses.sort(Comparator.comparingDouble(response -> calculateDistance(response.getGeoLocation(), destination)));
        } else if (request.getPreference() == SortingPreference.LEAST_TRIP_TIME) {
            responses.sort(Comparator.comparingLong(NearbyParkingResponse::getTotalTripTime));
        }

        return responses;
    }

    private double calculateDistance(Geolocation a, Geolocation b) {
        double latDiff = a.getLatitude() - b.getLatitude();
        double lonDiff = a.getLongitude() - b.getLongitude();
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff) * 111_000; // Approximation in meters
    }

    private NearbyParkingResponse mapToResponse(ParkingLotEntity entity) {
        NearbyParkingResponse response = new NearbyParkingResponse();
        response.setPlaceId(entity.getPlaceId());
        response.setFullName(entity.getFullName());
        response.setGeoLocation(entity.getGeoLocation());
        response.setTotalCapacity(entity.getTotalCapacity());
        response.setIsOpen(isOpen(entity));
        return response;
    }

    private boolean isOpen(ParkingLotEntity entity) {
        var now = java.time.LocalTime.now();
        return now.isAfter(entity.getOpeningTime()) && now.isBefore(entity.getClosingTime());
    }
}
