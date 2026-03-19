package com.touristsafety.tourist_safety_system.ai;

import com.touristsafety.tourist_safety_system.model.LocationPing;
import com.touristsafety.tourist_safety_system.model.Tourist;
import com.touristsafety.tourist_safety_system.repository.LocationPingRepository;
import com.touristsafety.tourist_safety_system.repository.TouristRepository;
import com.touristsafety.tourist_safety_system.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnomalyDetectionEngine {

    private final TouristRepository touristRepo;
    private final LocationPingRepository pingRepo;
    private final AlertService alertService;

    @Value("${app.ai.inactivity-threshold-minutes}")
    private int inactivityThreshold;

    // Runs every 5 minutes automatically — no human trigger needed
    @Scheduled(fixedRate = 300000)
    public void runScan() {
        log.info("AI scan running at {}",
                LocalDateTime.now());

        List<Tourist> active = touristRepo
                .findByStatus(Tourist.TouristStatus.ACTIVE);

        log.info("Scanning {} active tourists", active.size());

        for (Tourist tourist : active) {
            checkLocationDropOff(tourist);
            checkProlongedInactivity(tourist);
        }
    }

    // Check 1: has the tourist's phone gone silent?
    private void checkLocationDropOff(Tourist tourist) {
        List<LocationPing> recent = pingRepo
                .findTop1ByTouristIdOrderByRecordedAtDesc(
                        tourist.getId());

        if (recent.isEmpty()) return;

        LocationPing last = recent.get(0);
        long minutes = ChronoUnit.MINUTES.between(
                last.getRecordedAt(), LocalDateTime.now());

        if (minutes > inactivityThreshold) {
            log.warn(
                    "DROP-OFF: {} — silent for {} minutes",
                    tourist.getFullName(), minutes);
            alertService.createAnomalyAlert(
                    tourist,
                    "LOCATION_DROP_OFF",
                    "No location update for "
                            + minutes + " minutes",
                    last.getLatitude(),
                    last.getLongitude()
            );
        }
    }

    // Check 2: has the tourist not moved at all?
    private void checkProlongedInactivity(Tourist tourist) {
        List<LocationPing> pings = pingRepo
                .findTop10ByTouristIdOrderByRecordedAtDesc(
                        tourist.getId());

        if (pings.size() < 5) return;

        double latVar = calculateVariance(
                pings.stream()
                        .mapToDouble(LocationPing::getLatitude)
                        .toArray());

        double lonVar = calculateVariance(
                pings.stream()
                        .mapToDouble(LocationPing::getLongitude)
                        .toArray());

        // Variance near zero = tourist hasn't moved
        if (latVar < 0.0000001 && lonVar < 0.0000001) {
            log.warn(
                    "INACTIVITY: {} appears stationary",
                    tourist.getFullName());
            alertService.createAnomalyAlert(
                    tourist,
                    "PROLONGED_INACTIVITY",
                    "Tourist appears stationary — possible distress",
                    pings.get(0).getLatitude(),
                    pings.get(0).getLongitude()
            );
        }
    }

    private double calculateVariance(double[] values) {
        double mean = 0;
        for (double v : values) mean += v;
        mean /= values.length;
        double variance = 0;
        for (double v : values)
            variance += Math.pow(v - mean, 2);
        return variance / values.length;
    }
}