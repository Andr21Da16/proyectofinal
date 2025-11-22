package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.PedidoRequest;
import com.proyecto.coolboxtienda.dto.response.PedidoResponse;
import com.proyecto.coolboxtienda.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> getAllPedidos(
            @RequestParam(required = false) Integer idCliente,
            @RequestParam(required = false) String estadoLogistico,
            @RequestParam(required = false) String estadoPago,
            @RequestParam(required = false) Integer idSucursal) {
        return ResponseEntity.ok(pedidoService.getPedidosByFilters(idCliente, estadoLogistico, estadoPago, idSucursal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> getPedidoById(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.getPedidoById(id));
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> createPedido(@Valid @RequestBody PedidoRequest request) {
        return ResponseEntity.ok(pedidoService.createPedido(request));
    }

    @PutMapping("/{id}/estado-logistico")
    public ResponseEntity<PedidoResponse> actualizarEstadoLogistico(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(pedidoService.actualizarEstadoLogistico(id, nuevoEstado));
    }

    @PutMapping("/{id}/estado-pago")
    public ResponseEntity<PedidoResponse> actualizarEstadoPago(
            @PathVariable Integer id,
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(pedidoService.actualizarEstadoPago(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Integer id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.ok().build();
    }
}
