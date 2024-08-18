package com.fhdo.parkingservice.repositories;

import com.fhdo.parkingservice.entities.ParkingSpaceEntity;
import com.fhdo.parkingservice.model.dtos.ParkingSpotCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpaceEntity, UUID> {
    @Query("""
    select new com.fhdo.parkingservice.model.dtos.ParkingSpotCount(p.parkingSpotType, count(p))
    from ParkingSpaceEntity p
    where p.parkingSpace.placeId = :parkingId and p.isOccupied = false
    group by p.parkingSpotType
    """)
    List<ParkingSpotCount> getAvailableSpaces(String parkingId);

    @Query("select p.internalId from ParkingSpaceEntity p where p.parkingSpace.placeId = :parkingId and p.isReserved = false and p.isOccupied = false")
    List<Integer> findByIsReservedFalseAndIsOccupiedFalse(String parkingId);

    Optional<ParkingSpaceEntity> findParkingSpaceEntityByParkingIdAndInternalId(UUID parkingSpaceId, Integer internalId);

}
