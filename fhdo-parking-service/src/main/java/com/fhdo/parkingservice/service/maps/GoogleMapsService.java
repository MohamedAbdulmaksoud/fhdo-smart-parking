package com.fhdo.parkingservice.service.maps;

import com.fhdo.parkingservice.entities.Geolocation;
import com.fhdo.parkingservice.model.dtos.DistanceServiceResponse;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleMapsService implements GeocodingService, DistanceService {

    private final GeoApiContext geoApiContext;

    @Override
    public Optional<Geolocation> getLocationOfAddress(String address) {
        GeocodingApiRequest request = new GeocodingApiRequest(geoApiContext);
        request.address(address);
        GeocodingResult[] results = request.awaitIgnoreError();

        return Optional.of(results[0])
                .map(result -> result.geometry)
                .map(geometry -> geometry.location)
                .map(latLng -> new Geolocation(latLng.lat, latLng.lng));
    }

    public List<DistanceServiceResponse> getRealTimeTravelDuration(Geolocation origin, Geolocation destination, List<Geolocation> nearbyParkings) {
        return sumDataAndCalculateTotal(nearbyParkings,
                getDistanceMatrixRow(List.of(origin), nearbyParkings, TravelMode.DRIVING),
                getDistanceMatrixRow(nearbyParkings, List.of(destination), TravelMode.WALKING));
    }

    private DistanceMatrixRow[] getDistanceMatrixRow(List<Geolocation> startLocations, List<Geolocation> endLocations, TravelMode mode) {
        LatLng[] origins = startLocations.stream().map(loc -> new LatLng(loc.getLatitude(), loc.getLongitude())).toArray(LatLng[]::new);
        LatLng[] destinations = endLocations.stream().map(loc -> new LatLng(loc.getLatitude(), loc.getLongitude())).toArray(LatLng[]::new);

        DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(geoApiContext)
                .origins(origins)
                .destinations(destinations)
                .mode(mode)
                .departureTime(Instant.now().plus(5, ChronoUnit.MINUTES))
                .awaitIgnoreError();

        return distanceMatrix.rows;

    }

    /**
     * Combine the results from two Google Maps Service.
     *
     * @param parkings:              A list of Geolocation each representing a parking space.
     * @param carToParkings:         Result of calling {@link GoogleMapsService#getRealTimeTravelDuration} from origin to destination's nearby parkings.
     * @param parkingsToDestination: Result of calling {@link GoogleMapsService#getRealTimeTravelDuration} from destination's nearby parkings to destination.
     * @implNote Number of {@link DistanceMatrixRow}s = number of origins. Each {@link DistanceMatrixElement} represents origin-destination pairing.
     */
    private List<DistanceServiceResponse> sumDataAndCalculateTotal(List<Geolocation> parkings, DistanceMatrixRow[] carToParkings, DistanceMatrixRow[] parkingsToDestination) {
        List<DistanceServiceResponse> result = new ArrayList<>();
        final int numberOfParkings = parkings.size();
        final DistanceMatrixElement[] carToParkingsResult = carToParkings[0].elements;

        if (carToParkings.length > 1 || carToParkingsResult.length != numberOfParkings) {
            log.error("There must be a single point of origin. {} origins were supplied", carToParkings.length);
            return result;
        }
        if (parkingsToDestination.length != numberOfParkings) {
            log.error("Number of parkingsToDestination results do not number of parkings");
            return result;
        }

        for (int i = 0; i < numberOfParkings; ++i) {
            final String timeFromOrigin = carToParkingsResult[i].duration.humanReadable;
            final String distanceFromDestination = parkingsToDestination[i].elements[0].distance.humanReadable;
            final String timeToDestination = parkingsToDestination[i].elements[0].duration.humanReadable;
            final Long totalTripTime = carToParkingsResult[i].duration.inSeconds + parkingsToDestination[i].elements[0].duration.inSeconds;

            result.add(new DistanceServiceResponse(parkings.get(i), distanceFromDestination, timeFromOrigin, timeToDestination, totalTripTime));
        }
        return result;
    }

}
