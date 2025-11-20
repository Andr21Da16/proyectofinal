package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.response.ProductoMasVendidoDTO;
import com.proyecto.coolboxtienda.dto.response.VentasPorSucursalDTO;
import com.proyecto.coolboxtienda.dto.response.VentasPorVendedorDTO;
import com.proyecto.coolboxtienda.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor

public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ventas/periodo")
    public ResponseEntity<BigDecimal> getVentasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.getVentasPorPeriodo(fechaInicio, fechaFin));
    }

    @GetMapping("/ventas/vendedor")
    public ResponseEntity<List<VentasPorVendedorDTO>> getVentasPorVendedor(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.getVentasPorVendedor(fechaInicio, fechaFin));
    }

    @GetMapping("/ventas/sucursal")
    public ResponseEntity<List<VentasPorSucursalDTO>> getVentasPorSucursal(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.getVentasPorSucursal(fechaInicio, fechaFin));
    }

    @GetMapping("/productos/top")
    public ResponseEntity<List<ProductoMasVendidoDTO>> getProductosMasVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(reporteService.getProductosMasVendidos(fechaInicio, fechaFin, limit));
    }

    @GetMapping("/ventas/metodo-pago")
    public ResponseEntity<Map<String, BigDecimal>> getVentasPorMetodoPago(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(reporteService.getVentasPorMetodoPago(fechaInicio, fechaFin));
    }
}
