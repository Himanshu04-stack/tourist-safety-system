package com.touristsafety.tourist_safety_system.controller;

import com.touristsafety.tourist_safety_system.model.LocationPing;
import com.touristsafety.tourist_safety_system.service.GeoFenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {

    private final GeoFenceService geoFenceService;

    // Mobile app calls this every 60 seconds
    @PostMapping("/ping")
    public ResponseEntity<LocationPing> receivePing(
            @RequestBody LocationPing ping) {
        return ResponseEntity.ok(
                geoFenceService.processLocationUpdate(ping));
    }

    // Get full location history for a tourist
    @GetMapping("/history/{touristId}")
    public ResponseEntity<List<LocationPing>> getHistory(
            @PathVariable String touristId) {
        return ResponseEntity.ok(
                geoFenceService.getHistory(touristId));
    }
}