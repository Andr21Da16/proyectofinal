package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.ClienteLoginRequest;
import com.proyecto.coolboxtienda.dto.request.ClienteRegisterRequest;
import com.proyecto.coolboxtienda.dto.response.AuthResponse;

public interface ClienteAuthService {
    AuthResponse register(ClienteRegisterRequest request);

    AuthResponse login(ClienteLoginRequest request);
}
