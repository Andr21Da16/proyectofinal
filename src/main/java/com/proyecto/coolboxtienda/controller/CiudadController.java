package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.CiudadRequest;
import com.proyecto.coolboxtienda.dto.response.CiudadResponse;
import com.proyecto.coolboxtienda.service.CiudadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ciudades")
@RequiredArgsConstructor

public class CiudadController {

    private final CiudadService ciudadService;

    @PostMapping
    public ResponseEntity<CiudadResponse> createCiudad(@Valid @RequestBody CiudadRequest request) {
        return ResponseEntity.ok(ciudadService.createCiudad(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CiudadResponse> updateCiudad(
            @PathVariable Integer id,
            @Valid @RequestBody CiudadRequest request) {
        return ResponseEntity.ok(ciudadService.updateCiudad(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCiudad(@PathVariable Integer id) {
        ciudadService.deleteCiudad(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CiudadResponse> getCiudadById(@PathVariable Integer id) {
        return ResponseEntity.ok(ciudadService.getCiudadById(id));
    }

    @GetMapping
    public ResponseEntity<List<CiudadResponse>> getAllCiudades() {
        return ResponseEntity.ok(ciudadService.getAllCiudades());
    }

    @GetMapping("/departamento/{id}")
    public ResponseEntity<List<CiudadResponse>> getCiudadesByDepartamento(@PathVariable Integer id) {
        return ResponseEntity.ok(ciudadService.getCiudadesByDepartamento(id));
    }
}
