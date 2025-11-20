package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.RolRequest;
import com.proyecto.coolboxtienda.dto.response.RolResponse;

import java.util.List;

public interface RolService {
    RolResponse createRol(RolRequest request);

    RolResponse updateRol(Integer id, RolRequest request);

    void deleteRol(Integer id);

    RolResponse getRolById(Integer id);

    List<RolResponse> getAllRoles();
}
