package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.AIQueryRequest;
import com.proyecto.coolboxtienda.dto.response.AIResponse;
import com.proyecto.coolboxtienda.service.GeminiAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor

public class GeminiAIController {

    private final GeminiAIService geminiAIService;

    @PostMapping("/query")
    public ResponseEntity<AIResponse> query(@Valid @RequestBody AIQueryRequest request) {
        return ResponseEntity.ok(geminiAIService.query(request));
    }

    @GetMapping("/recommendations/{idColaborador}")
    public ResponseEntity<AIResponse> getProductRecommendations(@PathVariable Integer idColaborador) {
        return ResponseEntity.ok(geminiAIService.getProductRecommendations(idColaborador));
    }

    @GetMapping("/analysis/{idSucursal}")
    public ResponseEntity<AIResponse> analyzeSalesData(@PathVariable Integer idSucursal) {
        return ResponseEntity.ok(geminiAIService.analyzeSalesData(idSucursal));
    }
}
