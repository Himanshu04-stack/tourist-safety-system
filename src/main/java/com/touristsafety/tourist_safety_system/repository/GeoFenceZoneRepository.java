package com.touristsafety.tourist_safety_system.repository;

import com.touristsafety.tourist_safety_system.model.GeoFenceZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoFenceZoneRepository
        extends JpaRepository<GeoFenceZone, Long> {
}