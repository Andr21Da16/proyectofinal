package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.GarantiaUpdateRequest;
import com.proyecto.coolboxtienda.dto.response.GarantiaResponse;
import com.proyecto.coolboxtienda.service.GarantiaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/garantias")
@RequiredArgsConstructor
public class GarantiaController {

    private final GarantiaService garantiaService;

    @GetMapping
    public ResponseEntity<List<GarantiaResponse>> getAllGarantias(
            @RequestParam(required = false) Integer idCliente,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer idProducto) {
        return ResponseEntity.ok(garantiaService.getGarantiasByFilters(idCliente, estado, idProducto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarantiaResponse> getGarantiaById(@PathVariable Integer id) {
        return ResponseEntity.ok(garantiaService.getGarantiaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarantiaResponse> actualizarEstado(
            @PathVariable Integer id,
            @Valid @RequestBody GarantiaUpdateRequest request) {
        return ResponseEntity.ok(garantiaService.actualizarEstado(id, request));
    }
}
