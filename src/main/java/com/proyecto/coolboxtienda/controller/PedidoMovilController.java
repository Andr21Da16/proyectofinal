package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.response.PedidoResponse;
import com.proyecto.coolboxtienda.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movil")
@RequiredArgsConstructor
public class PedidoMovilController {

    private final PedidoService pedidoService;

    @GetMapping("/almacen/pedidos")
    public ResponseEntity<List<PedidoResponse>> getPedidosParaAlmacen() {
        return ResponseEntity.ok(pedidoService.getPedidosParaAlmacen());
    }

    @PutMapping("/almacen/pedidos/{id}/preparar")
    public ResponseEntity<PedidoResponse> prepararPedido(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.actualizarEstadoLogistico(id, "EN_PREPARACION"));
    }

    @GetMapping("/transporte/pedidos")
    public ResponseEntity<List<PedidoResponse>> getPedidosEnTransito() {
        return ResponseEntity.ok(pedidoService.getPedidosEnTransito());
    }

    @PutMapping("/transporte/pedidos/{id}/entregar")
    public ResponseEntity<PedidoResponse> entregarPedido(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.entregarPedido(id));
    }
}
