package com.touristsafety.tourist_safety_system.repository;

import com.touristsafety.tourist_safety_system.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository
        extends JpaRepository<Alert, Long> {

    List<Alert> findByStatus(Alert.AlertStatus status);

    List<Alert> findByTouristId(String touristId);
}