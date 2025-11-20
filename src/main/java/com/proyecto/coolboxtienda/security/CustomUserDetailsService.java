package com.proyecto.coolboxtienda.security;

import com.proyecto.coolboxtienda.entity.Colaborador;
import com.proyecto.coolboxtienda.repository.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ColaboradorRepository colaboradorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Colaborador colaborador = colaboradorRepository.findByUsuarioColaborador(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (!colaborador.getActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        return new User(
                colaborador.getUsuarioColaborador(),
                colaborador.getContraseñaColaborador(),
                getAuthorities(colaborador));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Colaborador colaborador) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + colaborador.getRol().getNombreRol()));
        return authorities;
    }
}
