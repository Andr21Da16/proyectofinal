package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.CiudadRequest;
import com.proyecto.coolboxtienda.dto.response.CiudadResponse;
import com.proyecto.coolboxtienda.entity.Ciudad;
import com.proyecto.coolboxtienda.entity.Departamento;
import com.proyecto.coolboxtienda.repository.CiudadRepository;
import com.proyecto.coolboxtienda.repository.DepartamentoRepository;
import com.proyecto.coolboxtienda.service.CiudadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CiudadServiceImpl implements CiudadService {

    private final CiudadRepository ciudadRepository;
    private final DepartamentoRepository departamentoRepository;

    @Override
    @Transactional
    public CiudadResponse createCiudad(CiudadRequest request) {
        Departamento departamento = departamentoRepository.findById(request.getIdDepartamento())
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));

        Ciudad ciudad = new Ciudad();
        ciudad.setNombreCiudad(request.getNombreCiudad());
        ciudad.setDepartamento(departamento);

        ciudad = ciudadRepository.save(ciudad);
        return mapToResponse(ciudad);
    }

    @Override
    @Transactional
    public CiudadResponse updateCiudad(Integer id, CiudadRequest request) {
        Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));

        Departamento departamento = departamentoRepository.findById(request.getIdDepartamento())
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));

        ciudad.setNombreCiudad(request.getNombreCiudad());
        ciudad.setDepartamento(departamento);

        ciudad = ciudadRepository.save(ciudad);
        return mapToResponse(ciudad);
    }

    @Override
    @Transactional
    public void deleteCiudad(Integer id) {
        if (!ciudadRepository.existsById(id)) {
            throw new RuntimeException("Ciudad no encontrada");
        }
        ciudadRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CiudadResponse getCiudadById(Integer id) {
        Ciudad ciudad = ciudadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
        return mapToResponse(ciudad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CiudadResponse> getAllCiudades() {
        return ciudadRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CiudadResponse> getCiudadesByDepartamento(Integer idDepartamento) {
        return ciudadRepository.findByDepartamento_IdDepartamento(idDepartamento).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CiudadResponse mapToResponse(Ciudad ciudad) {
        return CiudadResponse.builder()
                .idCiudad(ciudad.getIdCiudad())
                .nombreCiudad(ciudad.getNombreCiudad())
                .idDepartamento(ciudad.getDepartamento().getIdDepartamento())
                .nombreDepartamento(ciudad.getDepartamento().getNombreDepartamento())
                .build();
    }
}
