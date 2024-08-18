package com.fhdo.bookingservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.sql.Timestamp;

@Embeddable
public class Audit {
    @org.hibernate.annotations.CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdOn;

    @Column(updatable = false)
    private String createdBy;

    @org.hibernate.annotations.UpdateTimestamp
    private Timestamp updatedOn;

    private String updatedBy;

}
