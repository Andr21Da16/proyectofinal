package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.ProveedorRequest;
import com.proyecto.coolboxtienda.dto.response.ProductoProveedorResponse;
import com.proyecto.coolboxtienda.dto.response.ProveedorResponse;
import com.proyecto.coolboxtienda.service.ProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/proveedores")
@RequiredArgsConstructor

public class ProveedorController {

    private final ProveedorService proveedorService;

    @PostMapping
    public ResponseEntity<ProveedorResponse> createProveedor(@Valid @RequestBody ProveedorRequest request) {
        return ResponseEntity.ok(proveedorService.createProveedor(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponse> updateProveedor(
            @PathVariable Integer id,
            @Valid @RequestBody ProveedorRequest request) {
        return ResponseEntity.ok(proveedorService.updateProveedor(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Integer id) {
        proveedorService.deleteProveedor(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ProveedorResponse>> getAllProveedores() {
        return ResponseEntity.ok(proveedorService.getAllProveedores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponse> getProveedorById(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.getProveedorById(id));
    }

    @PostMapping("/asignar-producto")
    public ResponseEntity<Void> assignProductoToProveedor(
            @RequestParam Integer idProducto,
            @RequestParam Integer idProveedor,
            @RequestParam BigDecimal precioCompra,
            @RequestParam Integer stockInicial) {
        proveedorService.assignProductoToProveedor(idProducto, idProveedor, precioCompra, stockInicial);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/productos")
    public ResponseEntity<List<ProductoProveedorResponse>> getProductosByProveedor(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.getProductosByProveedor(id));
    }

    @PutMapping("/actualizar-producto")
    public ResponseEntity<Void> updateProductoProveedor(
            @RequestParam Integer idProducto,
            @RequestParam Integer idProveedor,
            @RequestParam(required = false) BigDecimal nuevoPrecio,
            @RequestParam(required = false) Integer nuevoStock) {
        proveedorService.updateProductoProveedor(idProducto, idProveedor, nuevoPrecio, nuevoStock);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remover-producto")
    public ResponseEntity<Void> removeProductoFromProveedor(
            @RequestParam Integer idProducto,
            @RequestParam Integer idProveedor) {
        proveedorService.removeProductoFromProveedor(idProducto, idProveedor);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<ProveedorResponse>> getProveedoresByProducto(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(proveedorService.getProveedoresByProducto(idProducto));
    }
}
