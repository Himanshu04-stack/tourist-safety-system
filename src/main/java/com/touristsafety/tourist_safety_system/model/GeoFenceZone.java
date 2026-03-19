package com.touristsafety.tourist_safety_system.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "geofence_zones")
@Data
public class GeoFenceZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zoneName;
    private double centerLatitude;
    private double centerLongitude;
    private double radiusMeters;

    @Enumerated(EnumType.STRING)
    private ZoneType type;

    private String alertMessage;

    public enum ZoneType {
        HIGH_RISK, RESTRICTED, SAFE, POPULAR
    }
}