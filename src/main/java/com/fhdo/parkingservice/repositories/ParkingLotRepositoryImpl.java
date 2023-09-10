package com.fhdo.parkingservice.repositories;

import com.fhdo.parkingservice.entities.ParkingLotEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ParkingLotRepositoryImpl implements ParkingLotRepositoryExtension {

    private final EntityManager em;

    @Override
    public List<ParkingLotEntity> findNearbyParking(double longitude, double latitude, double distanceInMeters) {
        final var query = em.createNativeQuery("""
                        with user_location as (
                            select cast(st_makepoint(:longitude, :latitude) as geography) as user_point
                        )
                        select p.*, st_distance(p.geo_point, ul.user_point) as distance
                        from shared.parking_lot p
                        cross join user_location ul
                        where st_dwithin(p.geo_point, ul.user_point, :distanceInMeters)
                        order by distance
                        limit 10
                        """, "ParkingLotEntityMapping")
                .setParameter("longitude", longitude)
                .setParameter("latitude", latitude)
                .setParameter("distanceInMeters", distanceInMeters);
        List<Object []> resultSet = query.getResultList();
        for (Object[] result: resultSet) {
            ParkingLotEntity entity = (ParkingLotEntity) result[0];
            entity.setDistance((Double) result[1]);
        }

        return resultSet.stream().map(objects -> (ParkingLotEntity) objects[0]).toList();
    }
}
