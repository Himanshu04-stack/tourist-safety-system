package com.touristsafety.tourist_safety_system.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "location_pings")
@Data
public class LocationPing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String touristId;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    private double accuracy;
    private double batteryLevel;

    @Column(nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();
}