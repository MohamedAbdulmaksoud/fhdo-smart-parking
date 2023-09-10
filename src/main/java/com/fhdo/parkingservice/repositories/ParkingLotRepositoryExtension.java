package com.fhdo.parkingservice.repositories;

import com.fhdo.parkingservice.entities.ParkingLotEntity;

import java.util.List;

public interface ParkingLotRepositoryExtension {

    List<ParkingLotEntity> findNearbyParking(double longitude, double latitude, double distanceInMeters);
}
