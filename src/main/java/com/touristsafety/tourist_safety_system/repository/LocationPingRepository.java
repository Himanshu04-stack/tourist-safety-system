package com.touristsafety.tourist_safety_system.repository;

import com.touristsafety.tourist_safety_system.model.LocationPing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocationPingRepository
        extends JpaRepository<LocationPing, Long> {

    List<LocationPing> findTop1ByTouristIdOrderByRecordedAtDesc(
            String touristId);

    List<LocationPing> findTop10ByTouristIdOrderByRecordedAtDesc(
            String touristId);

    List<LocationPing> findByTouristIdOrderByRecordedAtDesc(
            String touristId);
}