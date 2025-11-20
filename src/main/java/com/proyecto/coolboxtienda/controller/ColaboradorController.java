package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.ColaboradorUpdateRequest;
import com.proyecto.coolboxtienda.dto.response.ColaboradorResponse;
import com.proyecto.coolboxtienda.service.ColaboradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
@RequiredArgsConstructor

public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> updateColaborador(
            @PathVariable Integer id,
            @Valid @RequestBody ColaboradorUpdateRequest request) {
        return ResponseEntity.ok(colaboradorService.updateColaborador(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColaborador(@PathVariable Integer id) {
        colaboradorService.deleteColaborador(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ColaboradorResponse>> getAllColaboradores() {
        return ResponseEntity.ok(colaboradorService.getAllColaboradores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> getColaboradorById(@PathVariable Integer id) {
        return ResponseEntity.ok(colaboradorService.getColaboradorById(id));
    }

    @GetMapping("/sucursal/{id}")
    public ResponseEntity<List<ColaboradorResponse>> getColaboradoresBySucursal(@PathVariable Integer id) {
        return ResponseEntity.ok(colaboradorService.getColaboradoresBySucursal(id));
    }
}
