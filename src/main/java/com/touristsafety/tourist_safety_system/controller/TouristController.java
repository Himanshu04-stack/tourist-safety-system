package com.touristsafety.tourist_safety_system.controller;

import com.touristsafety.tourist_safety_system.model.Alert;
import com.touristsafety.tourist_safety_system.model.Tourist;
import com.touristsafety.tourist_safety_system.service.AlertService;
import com.touristsafety.tourist_safety_system.service.TouristService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tourists")
@RequiredArgsConstructor
public class TouristController {

    private final TouristService touristService;
    private final AlertService alertService;

    @PostMapping("/register")
    public ResponseEntity<Tourist> register(
            @RequestBody Tourist tourist) {
        return ResponseEntity.ok(
                touristService.registerTourist(tourist));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tourist> getById(
            @PathVariable String id) {
        return ResponseEntity.ok(
                touristService.getTouristById(id));
    }

    // Tourist presses the panic button on their phone
    @PostMapping("/{id}/panic")
    public ResponseEntity<Alert> panic(
            @PathVariable String id,
            @RequestBody Map<String, Double> body) {

        double lat = body.getOrDefault("latitude", 0.0);
        double lon = body.getOrDefault("longitude", 0.0);

        Alert alert = alertService
                .createPanicAlert(id, lat, lon);
        return ResponseEntity.ok(alert);
    }
}