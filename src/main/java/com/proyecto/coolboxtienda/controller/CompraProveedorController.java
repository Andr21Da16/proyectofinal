package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.CompraProveedorRequest;
import com.proyecto.coolboxtienda.dto.response.CompraProveedorResponse;
import com.proyecto.coolboxtienda.service.CompraProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras-proveedor")
@RequiredArgsConstructor
public class CompraProveedorController {

    private final CompraProveedorService compraProveedorService;

    @GetMapping
    public ResponseEntity<List<CompraProveedorResponse>> getAllCompras(
            @RequestParam(required = false) Integer idProveedor,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer idSucursal) {
        return ResponseEntity.ok(compraProveedorService.getComprasByFilters(idProveedor, estado, idSucursal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraProveedorResponse> getCompraById(@PathVariable Integer id) {
        return ResponseEntity.ok(compraProveedorService.getCompraById(id));
    }

    @PostMapping
    public ResponseEntity<CompraProveedorResponse> createCompra(@Valid @RequestBody CompraProveedorRequest request) {
        return ResponseEntity.ok(compraProveedorService.createCompra(request));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<CompraProveedorResponse> actualizarEstado(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(compraProveedorService.actualizarEstado(id, nuevoEstado));
    }

    @PutMapping("/{id}/recepcion")
    public ResponseEntity<CompraProveedorResponse> registrarRecepcion(
            @PathVariable Integer id,
            @RequestParam Integer idProducto,
            @RequestParam Integer cantidadRecibida) {
        return ResponseEntity.ok(compraProveedorService.registrarRecepcion(id, idProducto, cantidadRecibida));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarCompra(@PathVariable Integer id) {
        compraProveedorService.cancelarCompra(id);
        return ResponseEntity.ok().build();
    }
}
