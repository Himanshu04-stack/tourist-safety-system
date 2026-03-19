package com.touristsafety.tourist_safety_system.service;

import com.touristsafety.tourist_safety_system.model.GeoFenceZone;
import com.touristsafety.tourist_safety_system.model.LocationPing;
import com.touristsafety.tourist_safety_system.repository.GeoFenceZoneRepository;
import com.touristsafety.tourist_safety_system.repository.LocationPingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoFenceService {

    private final GeoFenceZoneRepository zoneRepo;
    private final LocationPingRepository pingRepo;

    public LocationPing processLocationUpdate(
            LocationPing ping) {

        // Save the raw ping first
        LocationPing saved = pingRepo.save(ping);

        // Check every geo-fence zone
        List<GeoFenceZone> allZones = zoneRepo.findAll();
        List<String> breachedZones = new ArrayList<>();

        for (GeoFenceZone zone : allZones) {
            double distance = haversineDistance(
                    ping.getLatitude(),
                    ping.getLongitude(),
                    zone.getCenterLatitude(),
                    zone.getCenterLongitude()
            );
            if (distance <= zone.getRadiusMeters()) {
                log.warn(
                        "GEOFENCE BREACH: Tourist {} entered zone: {}",
                        ping.getTouristId(),
                        zone.getZoneName()
                );
                breachedZones.add(zone.getZoneName());
            }
        }
        return saved;
    }

    // Haversine formula — calculates real-world distance
    // between two GPS coordinates in metres
    private double haversineDistance(
            double lat1, double lon1,
            double lat2, double lon2) {

        final int R = 6371000; // Earth radius in metres
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(
                Math.sqrt(a), Math.sqrt(1 - a));
    }

    public List<LocationPing> getHistory(String touristId) {
        return pingRepo
                .findByTouristIdOrderByRecordedAtDesc(touristId);
    }
}