package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.RolPermisoRequest;
import com.proyecto.coolboxtienda.dto.response.PermisoResponse;
import com.proyecto.coolboxtienda.entity.Rol;
import com.proyecto.coolboxtienda.entity.RolPermiso;
import com.proyecto.coolboxtienda.repository.RolPermisoRepository;
import com.proyecto.coolboxtienda.repository.RolRepository;
import com.proyecto.coolboxtienda.service.RolPermisoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolPermisoServiceImpl implements RolPermisoService {

    private final RolPermisoRepository rolPermisoRepository;
    private final RolRepository rolRepository;

    @Override
    public List<PermisoResponse> getPermisosByRol(Integer idRol) {
        List<RolPermiso> permisos = rolPermisoRepository.findAllByRol_IdRol(idRol);
        return permisos.stream()
                .map(this::toPermisoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PermisoResponse createOrUpdatePermiso(RolPermisoRequest request) {
        // Verificar que el rol existe
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Buscar permiso existente
        RolPermiso rolPermiso = rolPermisoRepository
                .findByRol_IdRolAndNombreModulo(request.getIdRol(), request.getNombreModulo())
                .orElse(new RolPermiso());

        rolPermiso.setRol(rol);
        rolPermiso.setNombreModulo(request.getNombreModulo());
        rolPermiso.setPuedeVer(request.getPuedeVer());
        rolPermiso.setPuedeEditar(request.getPuedeEditar());
        rolPermiso.setPuedeCrear(request.getPuedeCrear());
        rolPermiso.setPuedeEliminar(request.getPuedeEliminar());

        rolPermiso = rolPermisoRepository.save(rolPermiso);
        return toPermisoResponse(rolPermiso);
    }

    @Override
    @Transactional
    public void deletePermiso(Integer idRol, String nombreModulo) {
        RolPermiso rolPermiso = rolPermisoRepository
                .findByRol_IdRolAndNombreModulo(idRol, nombreModulo)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        rolPermisoRepository.delete(rolPermiso);
    }

    @Override
    public PermisoResponse getPermiso(Integer idRol, String nombreModulo) {
        RolPermiso rolPermiso = rolPermisoRepository
                .findByRol_IdRolAndNombreModulo(idRol, nombreModulo)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        return toPermisoResponse(rolPermiso);
    }

    private PermisoResponse toPermisoResponse(RolPermiso rolPermiso) {
        return PermisoResponse.builder()
                .nombreModulo(rolPermiso.getNombreModulo())
                .puedeVer(rolPermiso.getPuedeVer())
                .puedeEditar(rolPermiso.getPuedeEditar())
                .puedeCrear(rolPermiso.getPuedeCrear())
                .puedeEliminar(rolPermiso.getPuedeEliminar())
                .build();
    }
}
