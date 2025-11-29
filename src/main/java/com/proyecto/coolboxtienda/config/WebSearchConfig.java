package com.proyecto.coolboxtienda.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google.search")
@Data
public class WebSearchConfig {
    private String key;
    private String cx;
}
