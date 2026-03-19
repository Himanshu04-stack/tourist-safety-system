package com.touristsafety.tourist_safety_system.controller;

import com.touristsafety.tourist_safety_system.model.Alert;
import com.touristsafety.tourist_safety_system.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/open")
    public ResponseEntity<List<Alert>> getOpenAlerts() {
        return ResponseEntity.ok(
                alertService.getOpenAlerts());
    }

    @PatchMapping("/{id}/acknowledge")
    public ResponseEntity<Alert> acknowledge(
            @PathVariable Long id,
            @RequestParam String officerId) {
        return ResponseEntity.ok(
                alertService.acknowledgeAlert(id, officerId));
    }
}