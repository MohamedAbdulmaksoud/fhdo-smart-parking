package com.fhdo.bookingservice.entities;

import com.fhdo.bookingservice.domain.BookingState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID bookingId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingState state;

    @Column(nullable = false, updatable = false)
    private UUID userId;

    private UUID parkingLotId;

    private UUID parkingSpotId;

    @OneToMany(mappedBy = "booking")
    private Set<BookingExtensionEntity> bookingExtensions;

    // TODO: 24.06.23: Parking violations to be imported from another service

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Transient
    private Duration bookingDuration;

    //original cost of the booking
    private BigDecimal baseCost;

    @Transient
    private BigDecimal totalPenalties;

    //total cost incl. extensions and penalties
    private BigDecimal totalCost;

    @Embedded
    private Audit metadata;

}
