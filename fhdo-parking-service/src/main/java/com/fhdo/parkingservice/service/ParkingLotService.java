package com.fhdo.parkingservice.service;

import com.fhdo.parkingservice.entities.Geolocation;
import com.fhdo.parkingservice.entities.ParkingLotEntity;
import com.fhdo.parkingservice.mapper.MapStructMapper;
import com.fhdo.parkingservice.model.SortingPreference;
import com.fhdo.parkingservice.model.dtos.DistanceServiceResponse;
import com.fhdo.parkingservice.model.dtos.NearbyParkingRequest;
import com.fhdo.parkingservice.model.dtos.NearbyParkingResponse;
import com.fhdo.parkingservice.repositories.ParkingLotRepository;
import com.fhdo.parkingservice.service.maps.DistanceConverter;
import com.fhdo.parkingservice.service.maps.DistanceService;
import com.fhdo.parkingservice.service.maps.GeocodingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository repository;

    private final GeocodingService geocodingService;

    private final DistanceService distanceService;

    private final MapStructMapper mapper;

    public List<NearbyParkingResponse> findNearbyParking(NearbyParkingRequest request) {
        log.info("received parking request: {}", request.toString());
        List<ParkingLotEntity> parkingLotEntities = repository.findNearbyParking(request.destination().getLongitude(), request.destination().getLatitude(), request.distanceInMeters());
        List<Geolocation> nearbyParkings = parkingLotEntities.stream().map(ParkingLotEntity::getGeoLocation).toList();

        List<NearbyParkingResponse> nearbyParkingResponses = mapper.parkingLotEntityListToNearbyParkingResponseList(parkingLotEntities);
        if (nearbyParkingResponses.isEmpty()) {
            return nearbyParkingResponses;
        }
        List<DistanceServiceResponse> distanceResponses = distanceService.getRealTimeTravelDuration(request.origin(), request.destination(), nearbyParkings);

        List<NearbyParkingResponse> mergedResponses = mergeParkingRepsonse(nearbyParkingResponses, distanceResponses);

        SortingPreference preference = request.preference();

        switch (preference) {
            case LEAST_WALKING_DISTANCE ->
                    mergedResponses.sort(Comparator.comparingLong(response -> DistanceConverter.convertToMeters(response.getDistance())));
            case LEAST_TRIP_TIME ->
                    mergedResponses.sort(Comparator.comparingLong(NearbyParkingResponse::getTotalTripTime));
            default -> throw new IllegalArgumentException("Unsupported SortingPreference: " + preference);
        }
        return mergedResponses;
    }

    public List<NearbyParkingResponse> findNearbyParking(String address, double distanceInMeters) {
        Optional<Geolocation> geolocation = geocodingService.getLocationOfAddress(address);
        if (geolocation.isPresent()) {
            double longitude = geolocation.get().getLongitude();
            double latitude = geolocation.get().getLatitude();
            return mapper.parkingLotEntityListToNearbyParkingResponseList(repository.findNearbyParking(longitude, latitude, distanceInMeters));
        }
        log.error("Could not process the geocoding request for given address: {}", address);
        throw new RuntimeException("Could not process the geocoding request");
    }

    private List<NearbyParkingResponse> mergeParkingRepsonse(List<NearbyParkingResponse> nearbyParkingResponse, List<DistanceServiceResponse> distanceServiceResponse) {
        Map<Geolocation, NearbyParkingResponse> parkingResponseMap = nearbyParkingResponse.stream()
                .collect(Collectors.toMap(NearbyParkingResponse::getGeoLocation, Function.identity()));

        // Merge distanceResponses into parkingResponses based on Geolocation
        distanceServiceResponse.forEach(distanceResponse -> {
            Geolocation geolocation = distanceResponse.location();
            if (parkingResponseMap.containsKey(geolocation)) {
                NearbyParkingResponse parkingResponse = parkingResponseMap.get(geolocation);
                parkingResponse.setDistance(distanceResponse.distanceFromDestination());
                parkingResponse.setTimeFromOrigin(distanceResponse.timeFromOrigin());
                parkingResponse.setTimeToDestination(distanceResponse.timeToDestination());
                parkingResponse.setTotalTripTime(distanceResponse.totalTripTime());
            }
        });

        List<NearbyParkingResponse> response = new ArrayList<>(parkingResponseMap.values());
        response.sort(Comparator.comparingLong(NearbyParkingResponse::getTotalTripTime));
        return response;

    }
}
