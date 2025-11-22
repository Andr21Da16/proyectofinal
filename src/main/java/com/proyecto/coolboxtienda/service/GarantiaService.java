package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.GarantiaUpdateRequest;
import com.proyecto.coolboxtienda.dto.response.GarantiaResponse;

import java.util.List;

public interface GarantiaService {
    List<GarantiaResponse> getAllGarantias();

    List<GarantiaResponse> getGarantiasByFilters(Integer idCliente, String estado, Integer idProducto);

    GarantiaResponse getGarantiaById(Integer id);

    GarantiaResponse actualizarEstado(Integer id, GarantiaUpdateRequest request);

    List<GarantiaResponse> getGarantiasAsignadasByUsername(String username);
}
