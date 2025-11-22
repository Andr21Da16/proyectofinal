package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.CuponRequest;
import com.proyecto.coolboxtienda.dto.response.CuponResponse;
import com.proyecto.coolboxtienda.service.CuponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cupones")
@RequiredArgsConstructor
public class CuponController {

    private final CuponService cuponService;

    @GetMapping
    public ResponseEntity<List<CuponResponse>> getAllCupones() {
        return ResponseEntity.ok(cuponService.getAllCupones());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<CuponResponse>> getCuponesActivos() {
        return ResponseEntity.ok(cuponService.getCuponesActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuponResponse> getCuponById(@PathVariable Integer id) {
        return ResponseEntity.ok(cuponService.getCuponById(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CuponResponse> getCuponByCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(cuponService.getCuponByCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<CuponResponse> createCupon(@Valid @RequestBody CuponRequest request) {
        return ResponseEntity.ok(cuponService.createCupon(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuponResponse> updateCupon(@PathVariable Integer id,
            @Valid @RequestBody CuponRequest request) {
        return ResponseEntity.ok(cuponService.updateCupon(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCupon(@PathVariable Integer id) {
        cuponService.deleteCupon(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/validar/{codigo}")
    public ResponseEntity<Boolean> validarCupon(@PathVariable String codigo, @RequestParam BigDecimal montoCompra) {
        return ResponseEntity.ok(cuponService.validarCupon(codigo, montoCompra));
    }
}
