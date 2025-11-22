package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.ProductoRequest;
import com.proyecto.coolboxtienda.dto.response.PageResponse;
import com.proyecto.coolboxtienda.dto.response.ProductoDetailResponse;
import com.proyecto.coolboxtienda.dto.response.ProductoResponse;
import com.proyecto.coolboxtienda.service.ProductoService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor

public class ProductoController {

    private final ProductoService productoService;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoResponse> createProducto(
            @RequestPart("producto") String productoStr,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {
        ProductoRequest request = objectMapper.readValue(productoStr, ProductoRequest.class);
        return ResponseEntity.ok(productoService.createProducto(request, imagen));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoResponse> updateProducto(
            @PathVariable Integer id,
            @RequestPart("producto") String productoStr,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {
        ProductoRequest request = objectMapper.readValue(productoStr, ProductoRequest.class);
        return ResponseEntity.ok(productoService.updateProducto(id, request, imagen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        productoService.deleteProducto(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDetailResponse> getProductoById(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.getProductoById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductoResponse>> getAllProductos(Pageable pageable) {
        return ResponseEntity.ok(productoService.getAllProductos(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProductoResponse>> searchProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Integer idCategoria,
            Pageable pageable) {
        return ResponseEntity.ok(productoService.searchProductos(nombre, marca, idCategoria, pageable));
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<ProductoResponse>> getProductosByCategoria(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.getProductosByCategoria(id));
    }

    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<ProductoResponse>> getProductosByMarca(@PathVariable String marca) {
        return ResponseEntity.ok(productoService.getProductosByMarca(marca));
    }

    @GetMapping("/all-branches")
    public ResponseEntity<List<ProductoResponse>> getAllProductosAllBranches() {
        return ResponseEntity.ok(productoService.getAllProductosAllBranches());
    }
}
