package com.proyecto.coolboxtienda.service;

import java.util.List;
import java.util.Map;

public interface WebSearchService {
    /**
     * Realiza una búsqueda en internet y devuelve resultados relevantes.
     */
    List<Map<String, String>> searchProducts(String query);

    /**
     * Compara un producto interno con resultados de internet.
     */
    Map<String, Object> compareWithMarket(String productName, Double internalPrice, String features);
}
