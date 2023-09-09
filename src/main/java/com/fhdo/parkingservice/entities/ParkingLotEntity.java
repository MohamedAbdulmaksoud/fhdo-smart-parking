package com.fhdo.parkingservice.entities;

import com.fhdo.parkingservice.model.ParkingLotOnwershipType;
import com.fhdo.parkingservice.model.ParkingSpotType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    @CollectionTable(name = "parking_lot_hourly_rate", joinColumns = @JoinColumn(name = "parking_id"))
    @MapKeyColumn(name = "spot_type")
    @Column(name = "hourly_rate")
    private Map<ParkingSpotType, BigDecimal> spotPrices = new HashMap<>();


    @Transient
    private Double distanceInMeters;
}
