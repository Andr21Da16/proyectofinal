package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.DescuentoRequest;
import com.proyecto.coolboxtienda.dto.response.DescuentoResponse;
import com.proyecto.coolboxtienda.service.DescuentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/descuentos")
@RequiredArgsConstructor

public class DescuentoController {

    private final DescuentoService descuentoService;

    @PostMapping
    public ResponseEntity<DescuentoResponse> createDescuento(@Valid @RequestBody DescuentoRequest request) {
        return ResponseEntity.ok(descuentoService.createDescuento(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DescuentoResponse> updateDescuento(
            @PathVariable Integer id,
            @Valid @RequestBody DescuentoRequest request) {
        return ResponseEntity.ok(descuentoService.updateDescuento(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDescuento(@PathVariable Integer id) {
        descuentoService.deleteDescuento(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<List<DescuentoResponse>> getDescuentosActivos() {
        return ResponseEntity.ok(descuentoService.getDescuentosActivos());
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<DescuentoResponse> getDescuentoByProducto(@PathVariable Integer id) {
        return ResponseEntity.ok(descuentoService.getDescuentoByProducto(id));
    }
}
