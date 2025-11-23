package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.CampaniaRequest;
import com.proyecto.coolboxtienda.dto.response.CampaniaResponse;
import com.proyecto.coolboxtienda.service.CampaniaMarketingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campanias")
@RequiredArgsConstructor
public class CampaniaMarketingController {

    private final CampaniaMarketingService campaniaMarketingService;

    @GetMapping
    public ResponseEntity<List<CampaniaResponse>> getAllCampanias() {
        return ResponseEntity.ok(campaniaMarketingService.getAllCampanias());
    }

    @GetMapping("/activas")
    public ResponseEntity<List<CampaniaResponse>> getCampaniasActivas() {
        return ResponseEntity.ok(campaniaMarketingService.getCampaniasActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaniaResponse> getCampaniaById(@PathVariable Integer id) {
        return ResponseEntity.ok(campaniaMarketingService.getCampaniaById(id));
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<CampaniaResponse>> getCampaniasByProducto(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(campaniaMarketingService.getCampaniasByProducto(idProducto));
    }

    @PostMapping
    public ResponseEntity<CampaniaResponse> createCampania(@Valid @RequestBody CampaniaRequest request) {
        return ResponseEntity.ok(campaniaMarketingService.createCampania(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaniaResponse> updateCampania(@PathVariable Integer id,
            @Valid @RequestBody CampaniaRequest request) {
        return ResponseEntity.ok(campaniaMarketingService.updateCampania(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampania(@PathVariable Integer id) {
        campaniaMarketingService.deleteCampania(id);
        return ResponseEntity.ok().build();
    }
}
