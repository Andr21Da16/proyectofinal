package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.SucursalRequest;
import com.proyecto.coolboxtienda.dto.response.SucursalResponse;
import com.proyecto.coolboxtienda.service.SucursalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sucursales")
@RequiredArgsConstructor

public class SucursalController {

    private final SucursalService sucursalService;

    @PostMapping
    public ResponseEntity<SucursalResponse> createSucursal(@Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.ok(sucursalService.createSucursal(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponse> updateSucursal(
            @PathVariable Integer id,
            @Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.ok(sucursalService.updateSucursal(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSucursal(@PathVariable Integer id) {
        sucursalService.deleteSucursal(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<SucursalResponse>> getAllSucursales() {
        return ResponseEntity.ok(sucursalService.getAllSucursales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponse> getSucursalById(@PathVariable Integer id) {
        return ResponseEntity.ok(sucursalService.getSucursalById(id));
    }
}
