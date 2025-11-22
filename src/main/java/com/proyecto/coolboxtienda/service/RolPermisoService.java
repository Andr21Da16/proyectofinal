package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.RolPermisoRequest;
import com.proyecto.coolboxtienda.dto.response.PermisoResponse;

import java.util.List;

public interface RolPermisoService {
    List<PermisoResponse> getPermisosByRol(Integer idRol);

    PermisoResponse createOrUpdatePermiso(RolPermisoRequest request);

    void deletePermiso(Integer idRol, String nombreModulo);

    PermisoResponse getPermiso(Integer idRol, String nombreModulo);
}
