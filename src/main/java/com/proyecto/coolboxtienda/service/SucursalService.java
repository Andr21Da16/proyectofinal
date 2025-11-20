package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.SucursalRequest;
import com.proyecto.coolboxtienda.dto.response.SucursalResponse;

import java.util.List;

public interface SucursalService {
    SucursalResponse createSucursal(SucursalRequest request);

    SucursalResponse updateSucursal(Integer id, SucursalRequest request);

    void deleteSucursal(Integer id);

    SucursalResponse getSucursalById(Integer id);

    List<SucursalResponse> getAllSucursales();

    List<SucursalResponse> getSucursalesByDepartamento(Integer idDepartamento);
}
