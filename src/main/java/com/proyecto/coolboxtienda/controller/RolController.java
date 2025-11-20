package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.RolRequest;
import com.proyecto.coolboxtienda.dto.response.RolResponse;
import com.proyecto.coolboxtienda.service.RolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor

public class RolController {

    private final RolService rolService;

    @PostMapping
    public ResponseEntity<RolResponse> createRol(@Valid @RequestBody RolRequest request) {
        return ResponseEntity.ok(rolService.createRol(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> updateRol(
            @PathVariable Integer id,
            @Valid @RequestBody RolRequest request) {
        return ResponseEntity.ok(rolService.updateRol(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable Integer id) {
        rolService.deleteRol(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> getRolById(@PathVariable Integer id) {
        return ResponseEntity.ok(rolService.getRolById(id));
    }

    @GetMapping
    public ResponseEntity<List<RolResponse>> getAllRoles() {
        return ResponseEntity.ok(rolService.getAllRoles());
    }
}
