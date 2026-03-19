package com.touristsafety.tourist_safety_system.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tourists")
@Data
public class Tourist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String documentNumber;

    @Column(nullable = false)
    private String nationality;

    private String phoneNumber;
    private String email;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String plannedItinerary;

    @Column(nullable = false)
    private LocalDate visitStartDate;

    @Column(nullable = false)
    private LocalDate visitEndDate;

    private String blockchainTxHash;
    @Column(columnDefinition = "TEXT")
    private String digitalIdQrCode;
    private double safetyScore = 100.0;

    private LocalDateTime registeredAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private TouristStatus status = TouristStatus.ACTIVE;

    public enum TouristStatus {
        ACTIVE, COMPLETED, FLAGGED, MISSING
    }
}