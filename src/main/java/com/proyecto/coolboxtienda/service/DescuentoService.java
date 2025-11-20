package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.DescuentoRequest;
import com.proyecto.coolboxtienda.dto.response.DescuentoResponse;

import java.util.List;

public interface DescuentoService {
    DescuentoResponse createDescuento(DescuentoRequest request);

    DescuentoResponse updateDescuento(Integer id, DescuentoRequest request);

    void deleteDescuento(Integer id);

    DescuentoResponse getDescuentoById(Integer id);

    List<DescuentoResponse> getDescuentosActivos();

    DescuentoResponse getDescuentoByProducto(Integer idProducto);
}
