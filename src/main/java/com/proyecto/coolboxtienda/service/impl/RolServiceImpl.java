package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.RolRequest;
import com.proyecto.coolboxtienda.dto.response.RolResponse;
import com.proyecto.coolboxtienda.entity.Rol;
import com.proyecto.coolboxtienda.repository.RolRepository;
import com.proyecto.coolboxtienda.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    @Transactional
    public RolResponse createRol(RolRequest request) {
        Rol rol = new Rol();
        rol.setNombreRol(request.getNombreRol());
        rol.setDescripcionRol(request.getDescripcionRol());

        rol = rolRepository.save(rol);
        return mapToResponse(rol);
    }

    @Override
    @Transactional
    public RolResponse updateRol(Integer id, RolRequest request) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        rol.setNombreRol(request.getNombreRol());
        rol.setDescripcionRol(request.getDescripcionRol());

        rol = rolRepository.save(rol);
        return mapToResponse(rol);
    }

    @Override
    @Transactional
    public void deleteRol(Integer id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        rolRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public RolResponse getRolById(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return mapToResponse(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolResponse> getAllRoles() {
        return rolRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private RolResponse mapToResponse(Rol rol) {
        return RolResponse.builder()
                .idRol(rol.getIdRol())
                .nombreRol(rol.getNombreRol())
                .descripcion(rol.getDescripcionRol())
                .build();
    }
}
