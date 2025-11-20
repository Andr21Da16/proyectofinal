package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.ProveedorRequest;
import com.proyecto.coolboxtienda.dto.response.ProveedorResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProveedorService {
    ProveedorResponse createProveedor(ProveedorRequest request);

    ProveedorResponse updateProveedor(Integer id, ProveedorRequest request);

    void deleteProveedor(Integer id);

    ProveedorResponse getProveedorById(Integer id);

    List<ProveedorResponse> getAllProveedores();

    List<ProveedorResponse> getProveedoresByProducto(Integer idProducto);

    void assignProductoToProveedor(Integer idProducto, Integer idProveedor, BigDecimal precioCompra,
            Integer stockInicial);
}
