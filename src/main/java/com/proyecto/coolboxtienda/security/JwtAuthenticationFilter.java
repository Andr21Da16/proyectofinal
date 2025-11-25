package com.proyecto.coolboxtienda.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService colaboradorUserDetailsService;
    private final UserDetailsService clienteUserDetailsService;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            @Qualifier("customUserDetailsService") UserDetailsService colaboradorUserDetailsService,
            @Qualifier("clienteUserDetailsService") UserDetailsService clienteUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.colaboradorUserDetailsService = colaboradorUserDetailsService;
        this.clienteUserDetailsService = clienteUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            username = jwtTokenProvider.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Determinar qué UserDetailsService usar basado en el claim "tipoUsuario"
                Claims claims = jwtTokenProvider.extractAllClaimsPublic(jwt);
                String tipoUsuario = claims.get("tipoUsuario", String.class);

                UserDetails userDetails;
                if ("CLIENTE".equals(tipoUsuario)) {
                    userDetails = this.clienteUserDetailsService.loadUserByUsername(username);
                } else {
                    // Por defecto asumimos que es un colaborador si no es CLIENTE
                    userDetails = this.colaboradorUserDetailsService.loadUserByUsername(username);
                }

                if (jwtTokenProvider.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Si hay error validando el token (expirado, firma inválida, etc),
            // simplemente no autenticamos y dejamos que pase al siguiente filtro
            // que rechazará la petición si requiere autenticación.
            logger.error("Error procesando JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
