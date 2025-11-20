package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.DepartamentoRequest;
import com.proyecto.coolboxtienda.dto.response.DepartamentoResponse;
import com.proyecto.coolboxtienda.service.DepartamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
@RequiredArgsConstructor

public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @PostMapping
    public ResponseEntity<DepartamentoResponse> createDepartamento(@Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.ok(departamentoService.createDepartamento(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> updateDepartamento(
            @PathVariable Integer id,
            @Valid @RequestBody DepartamentoRequest request) {
        return ResponseEntity.ok(departamentoService.updateDepartamento(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartamento(@PathVariable Integer id) {
        departamentoService.deleteDepartamento(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> getDepartamentoById(@PathVariable Integer id) {
        return ResponseEntity.ok(departamentoService.getDepartamentoById(id));
    }

    @GetMapping
    public ResponseEntity<List<DepartamentoResponse>> getAllDepartamentos() {
        return ResponseEntity.ok(departamentoService.getAllDepartamentos());
    }
}
