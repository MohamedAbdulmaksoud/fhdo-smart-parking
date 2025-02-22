package com.fhdo.ui.dto;

import lombok.Data;

@Data
public final class NearbyParkingRequest {
    private Geolocation origin;
    private Geolocation destination;
    private String address;
    private Double distanceInMeters;
    private SortingPreference preference;


}
