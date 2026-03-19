package com.touristsafety.tourist_safety_system.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.touristsafety.tourist_safety_system.model.Alert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
public class DashboardWebSocketHandler
        extends TextWebSocketHandler {

    // Thread-safe set of all connected dashboard browsers
    private final CopyOnWriteArraySet<WebSocketSession>
            sessions = new CopyOnWriteArraySet<>();

    private final ObjectMapper objectMapper;

    public DashboardWebSocketHandler() {
        this.objectMapper = new ObjectMapper();
        // Needed to handle Java date/time types in JSON
        this.objectMapper.registerModule(
                new JavaTimeModule());
    }

    @Override
    public void afterConnectionEstablished(
            WebSocketSession session) {
        sessions.add(session);
        log.info("Dashboard connected: {} — total: {}",
                session.getId(), sessions.size());
    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status) {
        sessions.remove(session);
        log.info("Dashboard disconnected: {} — total: {}",
                session.getId(), sessions.size());
    }

    // Push alert to ALL connected police dashboards instantly
    public void broadcastAlert(Alert alert) {
        if (sessions.isEmpty()) {
            log.info("No dashboards connected — alert saved to DB only");
            return;
        }
        try {
            String json = objectMapper
                    .writeValueAsString(alert);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(
                            new TextMessage(json));
                }
            }
            log.info("Alert broadcast to {} dashboard(s)",
                    sessions.size());
        } catch (Exception e) {
            log.error("WebSocket broadcast failed", e);
        }
    }
}