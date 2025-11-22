package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.PedidoRequest;
import com.proyecto.coolboxtienda.dto.response.PedidoResponse;

import java.util.List;

public interface PedidoService {
    List<PedidoResponse> getAllPedidos();

    List<PedidoResponse> getPedidosByFilters(Integer idCliente, String estadoLogistico, String estadoPago,
            Integer idSucursal);

    PedidoResponse getPedidoById(Integer id);

    PedidoResponse createPedido(PedidoRequest request);

    PedidoResponse actualizarEstadoLogistico(Integer id, String nuevoEstado);

    PedidoResponse actualizarEstadoPago(Integer id, String nuevoEstado);

    PedidoResponse entregarPedido(Integer id); // Actualiza a ENTREGADO y genera venta

    List<PedidoResponse> getPedidosParaAlmacen();

    List<PedidoResponse> getPedidosEnTransito();

    void cancelarPedido(Integer id);
}
