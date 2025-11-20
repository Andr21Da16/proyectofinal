package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.DepartamentoRequest;
import com.proyecto.coolboxtienda.dto.response.DepartamentoResponse;

import java.util.List;

public interface DepartamentoService {
    DepartamentoResponse createDepartamento(DepartamentoRequest request);

    DepartamentoResponse updateDepartamento(Integer id, DepartamentoRequest request);

    void deleteDepartamento(Integer id);

    DepartamentoResponse getDepartamentoById(Integer id);

    List<DepartamentoResponse> getAllDepartamentos();
}
