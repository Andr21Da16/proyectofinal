package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.ClienteRequest;
import com.proyecto.coolboxtienda.dto.response.ClienteAnalyticsResponse;
import com.proyecto.coolboxtienda.dto.response.ClienteResponse;
import com.proyecto.coolboxtienda.dto.response.VentaResponse;
import com.proyecto.coolboxtienda.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> getAllClientes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String numeroDocumento,
            @RequestParam(required = false) Boolean activo) {

        if (nombre != null || email != null || numeroDocumento != null || activo != null) {
            return ResponseEntity.ok(clienteService.getClientesByFilters(nombre, email, numeroDocumento, activo));
        }
        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getClienteById(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.getClienteById(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> createCliente(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.createCliente(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> updateCliente(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.updateCliente(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Integer id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/historial-compras")
    public ResponseEntity<List<VentaResponse>> getHistorialCompras(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.getHistorialCompras(id));
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<ClienteAnalyticsResponse> getClienteAnalytics(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.getClienteAnalytics(id));
    }
}
