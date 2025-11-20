package com.proyecto.coolboxtienda.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gemini")
@Data
public class GeminiConfig {
    private String apiKey;
    private String apiUrl;
    private String model;
    private Double temperature;
    private Integer maxTokens;
}
