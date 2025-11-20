package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.CiudadRequest;
import com.proyecto.coolboxtienda.dto.response.CiudadResponse;

import java.util.List;

public interface CiudadService {
    CiudadResponse createCiudad(CiudadRequest request);

    CiudadResponse updateCiudad(Integer id, CiudadRequest request);

    void deleteCiudad(Integer id);

    CiudadResponse getCiudadById(Integer id);

    List<CiudadResponse> getAllCiudades();

    List<CiudadResponse> getCiudadesByDepartamento(Integer idDepartamento);
}
