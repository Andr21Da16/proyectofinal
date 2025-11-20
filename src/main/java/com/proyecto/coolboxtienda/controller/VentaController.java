package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.VentaRequest;
import com.proyecto.coolboxtienda.dto.response.PageResponse;
import com.proyecto.coolboxtienda.dto.response.VentaResponse;
import com.proyecto.coolboxtienda.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor

public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponse> createVenta(@Valid @RequestBody VentaRequest request) {
        return ResponseEntity.ok(ventaService.createVenta(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> getVentaById(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.getVentaById(id));
    }

    @GetMapping("/colaborador/{id}")
    public ResponseEntity<PageResponse<VentaResponse>> getVentasByColaborador(
            @PathVariable Integer id,
            Pageable pageable) {
        return ResponseEntity.ok(ventaService.getVentasByColaborador(id, pageable));
    }

    @GetMapping("/sucursal/{id}")
    public ResponseEntity<PageResponse<VentaResponse>> getVentasBySucursal(
            @PathVariable Integer id,
            Pageable pageable) {
        return ResponseEntity.ok(ventaService.getVentasBySucursal(id, pageable));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<VentaResponse> updateEstadoVenta(
            @PathVariable Integer id,
            @RequestParam Integer idEstado) {
        return ResponseEntity.ok(ventaService.updateEstadoVenta(id, idEstado));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelVenta(@PathVariable Integer id) {
        ventaService.cancelVenta(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filtros")
    public ResponseEntity<PageResponse<VentaResponse>> getVentasByFiltros(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(required = false) Integer idSucursal,
            @RequestParam(required = false) Integer idColaborador,
            @RequestParam(required = false) Integer idEstado,
            Pageable pageable) {
        return ResponseEntity.ok(
                ventaService.getVentasByFiltros(fechaInicio, fechaFin, idSucursal, idColaborador, idEstado, pageable));
    }
}
