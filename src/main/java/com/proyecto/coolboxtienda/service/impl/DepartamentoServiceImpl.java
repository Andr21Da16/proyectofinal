package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.DepartamentoRequest;
import com.proyecto.coolboxtienda.dto.response.DepartamentoResponse;
import com.proyecto.coolboxtienda.entity.Departamento;
import com.proyecto.coolboxtienda.repository.DepartamentoRepository;
import com.proyecto.coolboxtienda.service.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    @Override
    @Transactional
    public DepartamentoResponse createDepartamento(DepartamentoRequest request) {
        Departamento departamento = new Departamento();
        departamento.setNombreDepartamento(request.getNombreDepartamento());

        departamento = departamentoRepository.save(departamento);
        return mapToResponse(departamento);
    }

    @Override
    @Transactional
    public DepartamentoResponse updateDepartamento(Integer id, DepartamentoRequest request) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));

        departamento.setNombreDepartamento(request.getNombreDepartamento());

        departamento = departamentoRepository.save(departamento);
        return mapToResponse(departamento);
    }

    @Override
    @Transactional
    public void deleteDepartamento(Integer id) {
        if (!departamentoRepository.existsById(id)) {
            throw new RuntimeException("Departamento no encontrado");
        }
        departamentoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartamentoResponse getDepartamentoById(Integer id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
        return mapToResponse(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartamentoResponse> getAllDepartamentos() {
        return departamentoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private DepartamentoResponse mapToResponse(Departamento departamento) {
        return DepartamentoResponse.builder()
                .idDepartamento(departamento.getIdDepartamento())
                .nombreDepartamento(departamento.getNombreDepartamento())
                .build();
    }
}
