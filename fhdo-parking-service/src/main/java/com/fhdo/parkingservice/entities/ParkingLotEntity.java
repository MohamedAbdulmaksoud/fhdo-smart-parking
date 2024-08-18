package com.fhdo.parkingservice.entities;

import com.fhdo.parkingservice.model.ParkingLotOnwershipType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking_lot", schema = "shared")

@SqlResultSetMapping(
        name = "ParkingLotEntityMapping",
        entities = @EntityResult(
                entityClass = ParkingLotEntity.class,
                fields = {
                        @FieldResult(name = "parkingId", column = "parking_id"),
                        @FieldResult(name = "placeId", column = "place_id"),
                        @FieldResult(name = "fullName", column = "full_name"),
                        @FieldResult(name = "totalCapacity", column = "total_capacity"),
                        @FieldResult(name = "openingTime", column = "opening_time"),
                        @FieldResult(name = "closingTime", column = "closing_time"),
                        @FieldResult(name = "hourlyRates", column = "hourly_rates"),
                }),
        columns = @ColumnResult(name = "distance")
)
public class ParkingLotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID parkingId;

    @Column(unique = true)
    private String placeId;

    private String fullName;

    @Embedded
    private Address address;

    @Embedded
    private Geolocation geoLocation;

    @OneToMany(mappedBy = "parkingSpace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingSpaceEntity> parkingSpaces;

    private Integer totalCapacity;

    private LocalTime openingTime;

    private LocalTime closingTime;

    @Enumerated(EnumType.STRING)
    private ParkingLotOnwershipType ownershipType;

    @Type(PostgreSQLHStoreType.class)
    @Column(name = "hourly_rates")
    private Map<String, BigDecimal> hourlyRates = new HashMap<>();

    @Transient
    private Double distance;
}
