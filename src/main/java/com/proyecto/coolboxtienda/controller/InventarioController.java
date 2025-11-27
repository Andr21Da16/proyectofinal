package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.response.MovimientoInventarioResponse;
import com.proyecto.coolboxtienda.dto.request.InventarioRequest;
import com.proyecto.coolboxtienda.dto.request.TransferenciaRequest;
import com.proyecto.coolboxtienda.dto.response.SucursalProductoResponse;
import com.proyecto.coolboxtienda.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor

public class InventarioController {

    private final InventarioService inventarioService;

    @PostMapping("/add")
    public ResponseEntity<Void> addStock(@Valid @RequestBody InventarioRequest request) {
        inventarioService.addStock(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateStock(@Valid @RequestBody InventarioRequest request) {
        inventarioService.updateStock(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferStock(@Valid @RequestBody TransferenciaRequest request) {
        inventarioService.transferStock(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sucursal/{id}")
    public ResponseEntity<List<SucursalProductoResponse>> getInventoryBySucursal(@PathVariable Integer id) {
        return ResponseEntity.ok(inventarioService.getInventoryBySucursal(id));
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<List<SucursalProductoResponse>> getInventoryByProducto(@PathVariable Integer id) {
        return ResponseEntity.ok(inventarioService.getInventoryByProducto(id));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<SucursalProductoResponse>> getLowStockProducts(@RequestParam Integer threshold) {
        return ResponseEntity.ok(inventarioService.getLowStockProducts(threshold));
    }

    @GetMapping("/movimientos")
    public ResponseEntity<List<MovimientoInventarioResponse>> getRecentMovements(
            @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(inventarioService.getRecentMovements(limit));
    }
}
