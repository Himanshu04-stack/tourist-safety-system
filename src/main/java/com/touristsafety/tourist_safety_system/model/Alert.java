package com.touristsafety.tourist_safety_system.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String touristId;

    @Column(nullable = false)
    private String alertType;

    private String description;
    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    @Enumerated(EnumType.STRING)
    private AlertStatus status = AlertStatus.OPEN;

    private String assignedOfficerId;
    private String resolutionNote;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime resolvedAt;

    public enum AlertSeverity {
        CRITICAL, HIGH, MEDIUM, LOW
    }

    public enum AlertStatus {
        OPEN, ACKNOWLEDGED, RESOLVED
    }
}