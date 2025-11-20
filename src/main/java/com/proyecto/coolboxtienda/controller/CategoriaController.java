package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.CategoriaRequest;
import com.proyecto.coolboxtienda.dto.response.CategoriaResponse;
import com.proyecto.coolboxtienda.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor

public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponse> createCategoria(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.createCategoria(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> updateCategoria(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.updateCategoria(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Integer id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> getCategoriaById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.getCategoriaById(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }
}
