package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.SucursalRequest;
import com.proyecto.coolboxtienda.dto.response.SucursalResponse;
import com.proyecto.coolboxtienda.entity.Ciudad;
import com.proyecto.coolboxtienda.entity.Sucursal;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.repository.CiudadRepository;
import com.proyecto.coolboxtienda.repository.SucursalRepository;
import com.proyecto.coolboxtienda.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;
    private final CiudadRepository ciudadRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public SucursalResponse createSucursal(SucursalRequest request) {
        Ciudad ciudad = ciudadRepository.findById(request.getIdCiudad())
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));

        Sucursal sucursal = entityMapper.toSucursalEntity(request, ciudad);
        sucursal = sucursalRepository.save(sucursal);

        return entityMapper.toSucursalResponse(sucursal);
    }

    @Override
    @Transactional
    public SucursalResponse updateSucursal(Integer id, SucursalRequest request) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        Ciudad ciudad = ciudadRepository.findById(request.getIdCiudad())
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));

        entityMapper.updateSucursalEntity(sucursal, request, ciudad);
        sucursal = sucursalRepository.save(sucursal);

        return entityMapper.toSucursalResponse(sucursal);
    }

    @Override
    @Transactional
    public void deleteSucursal(Integer id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        sucursalRepository.delete(sucursal);
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse getSucursalById(Integer id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));
        return entityMapper.toSucursalResponse(sucursal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> getAllSucursales() {
        return sucursalRepository.findByActivoTrue().stream()
                .map(entityMapper::toSucursalResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> getSucursalesByDepartamento(Integer idDepartamento) {
        return sucursalRepository.findByCiudad_Departamento_IdDepartamentoAndActivoTrue(idDepartamento).stream()
                .map(entityMapper::toSucursalResponse)
                .collect(Collectors.toList());
    }
}
