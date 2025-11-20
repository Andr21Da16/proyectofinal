package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.CategoriaRequest;
import com.proyecto.coolboxtienda.dto.response.CategoriaResponse;
import com.proyecto.coolboxtienda.entity.Categoria;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.repository.CategoriaRepository;
import com.proyecto.coolboxtienda.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public CategoriaResponse createCategoria(CategoriaRequest request) {
        Categoria categoria = entityMapper.toCategoriaEntity(request);
        categoria = categoriaRepository.save(categoria);
        return entityMapper.toCategoriaResponse(categoria);
    }

    @Override
    @Transactional
    public CategoriaResponse updateCategoria(Integer id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        entityMapper.updateCategoriaEntity(categoria, request);
        categoria = categoriaRepository.save(categoria);

        return entityMapper.toCategoriaResponse(categoria);
    }

    @Override
    @Transactional
    public void deleteCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse getCategoriaById(Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return entityMapper.toCategoriaResponse(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> getAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(entityMapper::toCategoriaResponse)
                .collect(Collectors.toList());
    }
}
