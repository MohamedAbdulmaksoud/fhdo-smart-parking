package com.fhdo.parkingservice.model.dtos;

import com.fhdo.parkingservice.entities.Geolocation;

public record NearbyParkingRequest(Geolocation origin, Geolocation destination, String address, Double distanceInMeters) {
}
