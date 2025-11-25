package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.ClienteLoginRequest;
import com.proyecto.coolboxtienda.dto.request.ClienteRegisterRequest;
import com.proyecto.coolboxtienda.dto.response.AuthResponse;
import com.proyecto.coolboxtienda.dto.response.ClienteResponse;
import com.proyecto.coolboxtienda.entity.Ciudad;
import com.proyecto.coolboxtienda.entity.Cliente;
import com.proyecto.coolboxtienda.repository.CiudadRepository;
import com.proyecto.coolboxtienda.repository.ClienteRepository;
import com.proyecto.coolboxtienda.security.JwtTokenProvider;
import com.proyecto.coolboxtienda.service.ClienteAuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClienteAuthServiceImpl implements ClienteAuthService {

    private final ClienteRepository clienteRepository;
    private final CiudadRepository ciudadRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse register(ClienteRegisterRequest request) {
        // Validar email único
        if (clienteRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Validar documento único si se proporciona
        if (request.getNumeroDocumento() != null &&
                clienteRepository.findByNumeroDocumento(request.getNumeroDocumento()).isPresent()) {
            throw new RuntimeException("El número de documento ya está registrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(request.getNombreCompleto());
        cliente.setEmail(request.getEmail());
        cliente.setPassword(passwordEncoder.encode(request.getPassword()));
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setTipoDocumento(request.getTipoDocumento());
        cliente.setNumeroDocumento(request.getNumeroDocumento());
        cliente.setActivo(true);

        if (request.getIdCiudad() != null) {
            Ciudad ciudad = ciudadRepository.findById(request.getIdCiudad())
                    .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            cliente.setCiudad(ciudad);
        }

        cliente = clienteRepository.save(cliente);

        // Generar token con claim tipoUsuario = CLIENTE
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("tipoUsuario", "CLIENTE");
        extraClaims.put("idCliente", cliente.getIdCliente());

        User userDetails = new User(cliente.getEmail(), cliente.getPassword(), new ArrayList<>());
        String token = jwtTokenProvider.generateTokenWithClaims(userDetails, extraClaims);

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(toClienteResponse(cliente))
                .tipoAcceso("WEB")
                .build();
    }

    @Override
    public AuthResponse login(ClienteLoginRequest request) {
        // Obtener cliente
        Cliente cliente = clienteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // Validar contraseña
        if (!passwordEncoder.matches(request.getPassword(), cliente.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        if (!cliente.getActivo()) {
            throw new RuntimeException("Cliente inactivo");
        }

        // Generar token con claim tipoUsuario = CLIENTE
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("tipoUsuario", "CLIENTE");
        extraClaims.put("idCliente", cliente.getIdCliente());

        User userDetails = new User(cliente.getEmail(), cliente.getPassword(), new ArrayList<>());
        String token = jwtTokenProvider.generateTokenWithClaims(userDetails, extraClaims);

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(toClienteResponse(cliente))
                .tipoAcceso("WEB")
                .build();
    }

    private ClienteResponse toClienteResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .idCliente(cliente.getIdCliente())
                .nombreCompleto(cliente.getNombreCompleto())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .tipoDocumento(cliente.getTipoDocumento())
                .numeroDocumento(cliente.getNumeroDocumento())
                .ciudadNombre(cliente.getCiudad() != null ? cliente.getCiudad().getNombreCiudad() : null)
                .idCiudad(cliente.getCiudad() != null ? cliente.getCiudad().getIdCiudad() : null)
                .fechaRegistro(cliente.getFechaRegistro())
                .activo(cliente.getActivo())
                .build();
    }
}
