package com.touristsafety.tourist_safety_system.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QRCodeService {

    public String generateQR(String touristId) {
        try {
            String content = "TOURIST_ID:" + touristId;
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(content, BarcodeFormat.QR_CODE,
                            300, 300);
            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();
            MatrixToImageWriter
                    .writeToStream(matrix, "PNG", out);
            return Base64.getEncoder()
                    .encodeToString(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(
                    "QR generation failed", e);
        }
    }
}