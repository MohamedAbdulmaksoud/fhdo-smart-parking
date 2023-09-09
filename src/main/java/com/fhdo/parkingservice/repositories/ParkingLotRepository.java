package com.fhdo.parkingservice.repositories;

import com.fhdo.parkingservice.entities.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, UUID> {

    @Query(value = """
            with user_location as (
                select cast(st_makepoint(:longitude, :latitude) as geography) as user_point
            )
            select p.*, st_distance(p.geo_point, ul.user_point) as distance
            from shared.parking_lot p
            cross join user_location ul
            where st_dwithin(p.geo_point, ul.user_point, :distanceInMeters)
            order by distance
            limit 10
            """,
            nativeQuery = true)
    List<ParkingLotEntity> findNearbyParking(double longitude, double latitude, double distanceInMeters);

}
