package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.ClienteLoginRequest;
import com.proyecto.coolboxtienda.dto.request.ClienteRegisterRequest;
import com.proyecto.coolboxtienda.dto.response.AuthResponse;
import com.proyecto.coolboxtienda.service.ClienteAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteAuthController {

    private final ClienteAuthService clienteAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody ClienteRegisterRequest request) {
        return ResponseEntity.ok(clienteAuthService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody ClienteLoginRequest request) {
        return ResponseEntity.ok(clienteAuthService.login(request));
    }
}
