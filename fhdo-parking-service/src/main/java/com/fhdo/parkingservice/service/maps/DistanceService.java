package com.fhdo.parkingservice.service.maps;

import com.fhdo.parkingservice.entities.Geolocation;
import com.fhdo.parkingservice.model.dtos.DistanceServiceResponse;

import java.util.List;

public interface DistanceService {
    List<DistanceServiceResponse> getRealTimeTravelDuration(Geolocation origin, Geolocation destination, List<Geolocation> nearbyParkings);
}
