package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.CompraProveedorRequest;
import com.proyecto.coolboxtienda.dto.response.CompraProveedorResponse;

import java.util.List;

public interface CompraProveedorService {
    List<CompraProveedorResponse> getAllCompras();

    List<CompraProveedorResponse> getComprasByFilters(Integer idProveedor, String estado, Integer idSucursal);

    CompraProveedorResponse getCompraById(Integer id);

    CompraProveedorResponse createCompra(CompraProveedorRequest request);

    CompraProveedorResponse actualizarEstado(Integer id, String nuevoEstado);

    CompraProveedorResponse registrarRecepcion(Integer id, Integer idProducto, Integer cantidadRecibida);

    void cancelarCompra(Integer id);
}
