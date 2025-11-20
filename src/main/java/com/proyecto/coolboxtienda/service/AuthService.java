package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.ChangePasswordRequest;
import com.proyecto.coolboxtienda.dto.request.LoginRequest;
import com.proyecto.coolboxtienda.dto.request.RegisterRequest;
import com.proyecto.coolboxtienda.dto.response.AuthResponse;
import com.proyecto.coolboxtienda.dto.response.ColaboradorResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void changePassword(ChangePasswordRequest request);

    ColaboradorResponse getCurrentUser();
}
