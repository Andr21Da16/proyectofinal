package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.ColaboradorUpdateRequest;
import com.proyecto.coolboxtienda.dto.response.ColaboradorResponse;
import com.proyecto.coolboxtienda.entity.Colaborador;
import com.proyecto.coolboxtienda.entity.Rol;
import com.proyecto.coolboxtienda.entity.Sucursal;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.repository.ColaboradorRepository;
import com.proyecto.coolboxtienda.repository.RolRepository;
import com.proyecto.coolboxtienda.repository.SucursalRepository;
import com.proyecto.coolboxtienda.service.ColaboradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColaboradorServiceImpl implements ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final RolRepository rolRepository;
    private final SucursalRepository sucursalRepository;
    private final EntityMapper entityMapper;

    @Override
    @Transactional
    public ColaboradorResponse updateColaborador(Integer id, ColaboradorUpdateRequest request) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

        Rol rol = null;
        if (request.getIdRol() != null) {
            rol = rolRepository.findById(request.getIdRol())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        }

        Sucursal sucursal = null;
        if (request.getIdSucursal() != null) {
            sucursal = sucursalRepository.findById(request.getIdSucursal())
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));
        }

        entityMapper.updateColaboradorEntity(colaborador, request, rol, sucursal);
        colaborador = colaboradorRepository.save(colaborador);

        return entityMapper.toColaboradorResponse(colaborador);
    }

    @Override
    @Transactional
    public void deleteColaborador(Integer id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

        colaboradorRepository.delete(colaborador);
    }

    @Override
    @Transactional(readOnly = true)
    public ColaboradorResponse getColaboradorById(Integer id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));
        return entityMapper.toColaboradorResponse(colaborador);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColaboradorResponse> getAllColaboradores() {
        return colaboradorRepository.findByActivoTrue().stream()
                .map(entityMapper::toColaboradorResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColaboradorResponse> getColaboradoresBySucursal(Integer idSucursal) {
        return colaboradorRepository.findBySucursal_IdSucursalAndActivoTrue(idSucursal).stream()
                .map(entityMapper::toColaboradorResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColaboradorResponse> getColaboradoresByRol(Integer idRol) {
        return colaboradorRepository.findByRol_IdRolAndActivoTrue(idRol).stream()
                .map(entityMapper::toColaboradorResponse)
                .collect(Collectors.toList());
    }
}
