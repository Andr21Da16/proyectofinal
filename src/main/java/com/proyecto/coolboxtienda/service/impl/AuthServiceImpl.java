package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.ChangePasswordRequest;
import com.proyecto.coolboxtienda.dto.request.LoginRequest;
import com.proyecto.coolboxtienda.dto.request.RegisterRequest;
import com.proyecto.coolboxtienda.dto.response.AuthResponse;
import com.proyecto.coolboxtienda.dto.response.ColaboradorResponse;
import com.proyecto.coolboxtienda.dto.response.PermisoResponse;
import com.proyecto.coolboxtienda.entity.Colaborador;
import com.proyecto.coolboxtienda.entity.Rol;
import com.proyecto.coolboxtienda.entity.RolPermiso;
import com.proyecto.coolboxtienda.entity.Sucursal;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.repository.ColaboradorRepository;
import com.proyecto.coolboxtienda.repository.RolPermisoRepository;
import com.proyecto.coolboxtienda.repository.RolRepository;
import com.proyecto.coolboxtienda.repository.SucursalRepository;
import com.proyecto.coolboxtienda.security.JwtTokenProvider;
import com.proyecto.coolboxtienda.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final ColaboradorRepository colaboradorRepository;
        private final RolRepository rolRepository;
        private final SucursalRepository sucursalRepository;
        private final RolPermisoRepository rolPermisoRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenProvider jwtTokenProvider;
        private final AuthenticationManager authenticationManager;
        private final EntityMapper entityMapper;

        @Override
        @Transactional
        public AuthResponse register(RegisterRequest request) {
                // Validar que el usuario no exista
                if (colaboradorRepository.findByUsuarioColaborador(request.getUsuarioColaborador()).isPresent()) {
                        throw new RuntimeException("El usuario ya existe");
                }
                if (colaboradorRepository.findByEmailColaborador(request.getEmailColaborador()).isPresent()) {
                        throw new RuntimeException("El email ya está registrado");
                }

                // Obtener rol
                Rol rol = rolRepository.findById(request.getIdRol())
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

                // Obtener sucursal (opcional)
                Sucursal sucursal = null;
                if (request.getIdSucursal() != null) {
                        sucursal = sucursalRepository.findById(request.getIdSucursal())
                                        .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));
                }

                // Encriptar contraseña
                String hashedPassword = passwordEncoder.encode(request.getContraseñaColaborador());

                // Crear colaborador
                Colaborador colaborador = entityMapper.toColaboradorEntity(request, rol, sucursal, hashedPassword);
                colaborador = colaboradorRepository.save(colaborador);

                // Generar token
                org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                                colaborador.getUsuarioColaborador(),
                                colaborador.getContraseñaColaborador(),
                                new java.util.ArrayList<>());
                String token = jwtTokenProvider.generateToken(userDetails);

                // Obtener permisos del rol
                List<PermisoResponse> permisos = getPermisosByRol(colaborador.getRol().getIdRol());

                return AuthResponse.builder()
                                .token(token)
                                .colaborador(entityMapper.toColaboradorResponse(colaborador))
                                .permisos(permisos)
                                .tipoAcceso(colaborador.getRol().getTipoAcceso())
                                .build();
        }

        @Override
        public AuthResponse login(LoginRequest request) {
                // Autenticar
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsuarioColaborador(),
                                                request.getContraseniaColaborador()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Generar token usando el UserDetails del Authentication
                org.springframework.security.core.userdetails.UserDetails userDetails = (org.springframework.security.core.userdetails.UserDetails) authentication
                                .getPrincipal();
                String token = jwtTokenProvider.generateToken(userDetails);

                // Obtener colaborador
                Colaborador colaborador = colaboradorRepository
                                .findByUsuarioColaborador(request.getUsuarioColaborador())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // Obtener permisos del rol
                List<PermisoResponse> permisos = getPermisosByRol(colaborador.getRol().getIdRol());

                return AuthResponse.builder()
                                .token(token)
                                .colaborador(entityMapper.toColaboradorResponse(colaborador))
                                .permisos(permisos)
                                .tipoAcceso(colaborador.getRol().getTipoAcceso())
                                .build();
        }

        @Override
        @Transactional
        public void changePassword(ChangePasswordRequest request) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                Colaborador colaborador = colaboradorRepository.findByUsuarioColaborador(username)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // Verificar contraseña actual
                if (!passwordEncoder.matches(request.getCurrentPassword(), colaborador.getContraseñaColaborador())) {
                        throw new RuntimeException("Contraseña actual incorrecta");
                }

                // Actualizar contraseña
                colaborador.setContraseñaColaborador(passwordEncoder.encode(request.getNewPassword()));
                colaboradorRepository.save(colaborador);
        }

        @Override
        public ColaboradorResponse getCurrentUser() {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                Colaborador colaborador = colaboradorRepository.findByUsuarioColaborador(username)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                return entityMapper.toColaboradorResponse(colaborador);
        }

        private List<PermisoResponse> getPermisosByRol(Integer idRol) {
                List<RolPermiso> rolPermisos = rolPermisoRepository.findAllByIdRol(idRol);
                return rolPermisos.stream()
                                .map(rp -> PermisoResponse.builder()
                                                .nombreModulo(rp.getId().getNombreModulo())
                                                .puedeVer(rp.getPuedeVer())
                                                .puedeEditar(rp.getPuedeEditar())
                                                .puedeCrear(rp.getPuedeCrear())
                                                .puedeEliminar(rp.getPuedeEliminar())
                                                .build())
                                .collect(Collectors.toList());
        }
}
