package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.GarantiaUpdateRequest;
import com.proyecto.coolboxtienda.dto.response.GarantiaResponse;
import com.proyecto.coolboxtienda.service.GarantiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movil/garantias")
@RequiredArgsConstructor
public class GarantiaMovilController {

    private final GarantiaService garantiaService;

    @GetMapping("/asignadas")
    public ResponseEntity<List<GarantiaResponse>> getGarantiasAsignadas() {
        // Get current user ID from security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(garantiaService.getGarantiasAsignadasByUsername(username));
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<GarantiaResponse> actualizarGarantia(
            @PathVariable Integer id,
            @Valid @RequestBody GarantiaUpdateRequest request) {
        return ResponseEntity.ok(garantiaService.actualizarEstado(id, request));
    }
}
