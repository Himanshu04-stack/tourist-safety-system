package com.touristsafety.tourist_safety_system.service;

import com.touristsafety.tourist_safety_system.model.Alert;
import com.touristsafety.tourist_safety_system.model.Tourist;
import com.touristsafety.tourist_safety_system.repository.AlertRepository;
import com.touristsafety.tourist_safety_system.websocket.DashboardWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepo;
    private final DashboardWebSocketHandler wsHandler;

    public Alert createAnomalyAlert(
            Tourist tourist, String type,
            String description,
            double lat, double lon) {

        Alert alert = new Alert();
        alert.setTouristId(tourist.getId());
        alert.setAlertType(type);
        alert.setDescription(description);
        alert.setSeverity(Alert.AlertSeverity.HIGH);
        alert.setLatitude(lat);
        alert.setLongitude(lon);

        Alert saved = alertRepo.save(alert);
        wsHandler.broadcastAlert(saved);
        log.warn("ALERT [{}]: {} — Tourist: {}",
                type, description, tourist.getFullName());
        return saved;
    }

    public Alert createPanicAlert(
            String touristId,
            double lat, double lon) {

        Alert alert = new Alert();
        alert.setTouristId(touristId);
        alert.setAlertType("PANIC");
        alert.setDescription(
                "Tourist pressed PANIC button");
        alert.setSeverity(
                Alert.AlertSeverity.CRITICAL);
        alert.setLatitude(lat);
        alert.setLongitude(lon);

        Alert saved = alertRepo.save(alert);
        wsHandler.broadcastAlert(saved);
        log.error(
                "PANIC ALERT: touristId={} lat={} lon={}",
                touristId, lat, lon);
        return saved;
    }

    public List<Alert> getOpenAlerts() {
        return alertRepo.findByStatus(
                Alert.AlertStatus.OPEN);
    }

    public Alert acknowledgeAlert(
            Long alertId, String officerId) {
        Alert alert = alertRepo.findById(alertId)
                .orElseThrow(() ->
                        new RuntimeException("Alert not found"));
        alert.setStatus(Alert.AlertStatus.ACKNOWLEDGED);
        alert.setAssignedOfficerId(officerId);
        return alertRepo.save(alert);
    }
}