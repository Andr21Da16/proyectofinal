package com.proyecto.coolboxtienda.controller;

import com.proyecto.coolboxtienda.dto.request.ChangePasswordRequest;
import com.proyecto.coolboxtienda.dto.request.LoginRequest;
import com.proyecto.coolboxtienda.dto.request.RegisterRequest;
import com.proyecto.coolboxtienda.dto.response.AuthResponse;
import com.proyecto.coolboxtienda.dto.response.ColaboradorResponse;
import com.proyecto.coolboxtienda.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<ColaboradorResponse> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}
