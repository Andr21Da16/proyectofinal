package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.AIQueryRequest;
import com.proyecto.coolboxtienda.dto.response.AIResponse;

public interface GeminiAIService {
    AIResponse query(AIQueryRequest request);

    AIResponse getProductRecommendations(Integer idColaborador);

    AIResponse analyzeSalesData(Integer idSucursal);
}
