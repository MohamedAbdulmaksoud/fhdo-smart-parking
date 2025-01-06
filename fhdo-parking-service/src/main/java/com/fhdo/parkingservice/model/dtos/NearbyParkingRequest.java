package com.fhdo.parkingservice.model.dtos;

import com.fhdo.parkingservice.entities.Geolocation;
import com.fhdo.parkingservice.model.SortingPreference;

public record NearbyParkingRequest(Geolocation origin, Geolocation destination, String address, Double distanceInMeters, SortingPreference preference) {
}
