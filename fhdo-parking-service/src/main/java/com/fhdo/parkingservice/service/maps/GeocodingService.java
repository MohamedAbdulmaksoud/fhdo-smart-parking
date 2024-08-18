package com.fhdo.parkingservice.service.maps;

import com.fhdo.parkingservice.entities.Geolocation;

import java.util.Optional;

public interface GeocodingService {
    Optional<Geolocation> getLocationOfAddress(String address);
}
