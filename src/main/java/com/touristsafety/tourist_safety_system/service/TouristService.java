package com.touristsafety.tourist_safety_system.service;

import com.touristsafety.tourist_safety_system.model.Tourist;
import com.touristsafety.tourist_safety_system.repository.TouristRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TouristService {

    private final TouristRepository touristRepository;
    private final QRCodeService qrCodeService;

    public Tourist registerTourist(Tourist tourist) {
        if (touristRepository.existsByDocumentNumber(
                tourist.getDocumentNumber())) {
            throw new RuntimeException(
                    "Tourist already registered");
        }
        String qr = qrCodeService.generateQR(
                tourist.getDocumentNumber());
        tourist.setDigitalIdQrCode(qr);
        tourist.setBlockchainTxHash(
                "0x" + UUID.randomUUID()
                        .toString().replace("-", ""));
        return touristRepository.save(tourist);
    }

    public Tourist getTouristById(String id) {
        return touristRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Tourist not found: " + id));
    }
}