package com.fhdo.parkingservice.model.dtos;

import com.fhdo.parkingservice.entities.Geolocation;

public record DistanceServiceResponse(Geolocation location, String distanceFromDestination, String timeFromOrigin,
                                      String timeToDestination, Long totalTripTime) {
}
