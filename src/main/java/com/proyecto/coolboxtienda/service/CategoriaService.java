package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.CategoriaRequest;
import com.proyecto.coolboxtienda.dto.response.CategoriaResponse;

import java.util.List;

public interface CategoriaService {
    CategoriaResponse createCategoria(CategoriaRequest request);

    CategoriaResponse updateCategoria(Integer id, CategoriaRequest request);

    void deleteCategoria(Integer id);

    CategoriaResponse getCategoriaById(Integer id);

    List<CategoriaResponse> getAllCategorias();
}
