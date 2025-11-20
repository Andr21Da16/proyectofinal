package com.proyecto.coolboxtienda.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final GeminiConfig geminiConfig;

    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl(geminiConfig.getApiUrl())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
