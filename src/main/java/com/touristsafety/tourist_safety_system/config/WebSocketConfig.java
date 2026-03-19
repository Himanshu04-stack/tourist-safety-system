package com.touristsafety.tourist_safety_system.config;

import com.touristsafety.tourist_safety_system.websocket.DashboardWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig
        implements WebSocketConfigurer {

    private final DashboardWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(
            WebSocketHandlerRegistry registry) {
        registry
                .addHandler(handler, "/ws/dashboard")
                .setAllowedOrigins("*");
    }
}