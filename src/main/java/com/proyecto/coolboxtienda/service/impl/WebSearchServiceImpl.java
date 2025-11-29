package com.proyecto.coolboxtienda.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.coolboxtienda.config.WebSearchConfig;
import com.proyecto.coolboxtienda.service.WebSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WebSearchServiceImpl implements WebSearchService {

    private final WebSearchConfig webSearchConfig;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    @Override
    public List<Map<String, String>> searchProducts(String query) {
        List<Map<String, String>> results = new ArrayList<>();

        try {
            // Si no hay API Key configurada, devolvemos simulación o error
            if (webSearchConfig.getKey() == null || webSearchConfig.getKey().isEmpty() ||
                    webSearchConfig.getKey().contains("TU_API_KEY")) {
                return getSimulatedResults(query);
            }

            String jsonResponse = webClientBuilder.build().get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("www.googleapis.com")
                            .path("/customsearch/v1")
                            .queryParam("key", webSearchConfig.getKey())
                            .queryParam("cx", webSearchConfig.getCx())
                            .queryParam("q", query)
                            .queryParam("num", 5) // Top 5 resultados
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("items");

            if (items.isArray()) {
                for (JsonNode item : items) {
                    Map<String, String> res = new HashMap<>();
                    res.put("title", item.path("title").asText());
                    res.put("snippet", item.path("snippet").asText());
                    res.put("source", item.path("displayLink").asText());

                    // Intentar extraer precio del snippet o metadatos (es complejo en búsqueda
                    // general)
                    // Por ahora, lo dejamos como "Ver enlace" o extraemos si hay formato moneda
                    String snippet = item.path("snippet").asText();
                    String price = extractPrice(snippet);
                    res.put("price", price);

                    results.add(res);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Fallback a simulación en caso de error
            return getSimulatedResults(query);
        }

        return results;
    }

    private String extractPrice(String text) {
        // Lógica simple para encontrar precios (S/ o $)
        if (text.contains("S/")) {
            int idx = text.indexOf("S/");
            int end = text.indexOf(" ", idx + 4);
            if (end == -1)
                end = text.length();
            return text.substring(idx, Math.min(end, idx + 15)); // Captura S/ 123.00
        }
        return "Consultar web";
    }

    @Override
    public Map<String, Object> compareWithMarket(String productName, Double internalPrice, String features) {
        List<Map<String, String>> marketResults = searchProducts(productName);

        Map<String, Object> comparison = new HashMap<>();
        comparison.put("internalProduct", Map.of("name", productName, "price", internalPrice, "features", features));
        comparison.put("marketResults", marketResults);

        // El análisis lo hará la IA con estos datos
        return comparison;
    }

    private List<Map<String, String>> getSimulatedResults(String query) {
        List<Map<String, String>> results = new ArrayList<>();
        Map<String, String> res = new HashMap<>();
        res.put("title", "Resultado Simulado (Falta API Key): " + query);
        res.put("price", "S/ 0.00");
        res.put("source", "Sistema Interno");
        res.put("snippet",
                "Por favor configura google.search.key y google.search.cx en application.properties para resultados reales.");
        results.add(res);
        return results;
    }
}
