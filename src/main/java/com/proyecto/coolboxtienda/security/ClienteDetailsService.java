package com.proyecto.coolboxtienda.security;

import com.proyecto.coolboxtienda.entity.Cliente;
import com.proyecto.coolboxtienda.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("clienteDetailsService")
@RequiredArgsConstructor
public class ClienteDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado con email: " + email));

        if (!cliente.getActivo()) {
            throw new UsernameNotFoundException("Cliente inactivo");
        }

        return User.builder()
                .username(cliente.getEmail())
                .password(cliente.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
}
