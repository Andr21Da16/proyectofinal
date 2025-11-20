package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.ColaboradorUpdateRequest;
import com.proyecto.coolboxtienda.dto.response.ColaboradorResponse;

import java.util.List;

public interface ColaboradorService {
    ColaboradorResponse updateColaborador(Integer id, ColaboradorUpdateRequest request);

    void deleteColaborador(Integer id);

    ColaboradorResponse getColaboradorById(Integer id);

    List<ColaboradorResponse> getAllColaboradores();

    List<ColaboradorResponse> getColaboradoresBySucursal(Integer idSucursal);

    List<ColaboradorResponse> getColaboradoresByRol(Integer idRol);
}
