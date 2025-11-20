package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.CarritoItemRequest;
import com.proyecto.coolboxtienda.dto.response.CarritoFullResponse;
import com.proyecto.coolboxtienda.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrito")
@RequiredArgsConstructor

public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/{idColaborador}")
    public ResponseEntity<CarritoFullResponse> getCarrito(@PathVariable Integer idColaborador) {
        return ResponseEntity.ok(carritoService.getCarritoFull(idColaborador));
    }

    @PostMapping("/{idColaborador}/items")
    public ResponseEntity<CarritoFullResponse> addItem(
            @PathVariable Integer idColaborador,
            @Valid @RequestBody CarritoItemRequest request) {
        return ResponseEntity.ok(carritoService.addItem(idColaborador, request));
    }

    @PutMapping("/{idColaborador}/items/{idProducto}")
    public ResponseEntity<CarritoFullResponse> updateItemQuantity(
            @PathVariable Integer idColaborador,
            @PathVariable Integer idProducto,
            @RequestParam Integer idProveedor,
            @RequestParam Integer idSucursal,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.updateItemQuantity(
                idColaborador, idProducto, idProveedor, idSucursal, cantidad));
    }

    @DeleteMapping("/{idColaborador}/items/{idProducto}")
    public ResponseEntity<CarritoFullResponse> removeItem(
            @PathVariable Integer idColaborador,
            @PathVariable Integer idProducto,
            @RequestParam Integer idProveedor,
            @RequestParam Integer idSucursal) {
        return ResponseEntity.ok(carritoService.removeItem(
                idColaborador, idProducto, idProveedor, idSucursal));
    }

    @DeleteMapping("/{idColaborador}")
    public ResponseEntity<Void> clearCarrito(@PathVariable Integer idColaborador) {
        carritoService.clearCarrito(idColaborador);
        return ResponseEntity.ok().build();
    }
}
