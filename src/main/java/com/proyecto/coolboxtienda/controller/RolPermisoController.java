package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.RolPermisoRequest;
import com.proyecto.coolboxtienda.dto.response.PermisoResponse;
import com.proyecto.coolboxtienda.service.RolPermisoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rol-permisos")
@RequiredArgsConstructor
public class RolPermisoController {

    private final RolPermisoService rolPermisoService;

    @GetMapping("/rol/{idRol}")
    public ResponseEntity<List<PermisoResponse>> getPermisosByRol(@PathVariable Integer idRol) {
        return ResponseEntity.ok(rolPermisoService.getPermisosByRol(idRol));
    }

    @GetMapping("/rol/{idRol}/modulo/{nombreModulo}")
    public ResponseEntity<PermisoResponse> getPermiso(
            @PathVariable Integer idRol,
            @PathVariable String nombreModulo) {
        return ResponseEntity.ok(rolPermisoService.getPermiso(idRol, nombreModulo));
    }

    @PostMapping
    public ResponseEntity<PermisoResponse> createOrUpdatePermiso(@Valid @RequestBody RolPermisoRequest request) {
        return ResponseEntity.ok(rolPermisoService.createOrUpdatePermiso(request));
    }

    @DeleteMapping("/rol/{idRol}/modulo/{nombreModulo}")
    public ResponseEntity<Void> deletePermiso(
            @PathVariable Integer idRol,
            @PathVariable String nombreModulo) {
        rolPermisoService.deletePermiso(idRol, nombreModulo);
        return ResponseEntity.ok().build();
    }
}
