package com.fhdo.parkingservice.repositories;

import com.fhdo.parkingservice.entities.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, UUID>, ParkingLotRepositoryExtension {

}
