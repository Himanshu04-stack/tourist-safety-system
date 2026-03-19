package com.touristsafety.tourist_safety_system.repository;

import com.touristsafety.tourist_safety_system.model.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TouristRepository
        extends JpaRepository<Tourist, String> {

    Optional<Tourist> findByDocumentNumber(String doc);

    boolean existsByDocumentNumber(String doc);

    List<Tourist> findByStatus(Tourist.TouristStatus status);
}