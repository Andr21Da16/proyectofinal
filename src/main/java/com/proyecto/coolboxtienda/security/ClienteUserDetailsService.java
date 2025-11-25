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

@Service("clienteUserDetailsService")
@RequiredArgsConstructor
public class ClienteUserDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado: " + username));

        if (!cliente.getActivo()) {
            throw new UsernameNotFoundException("Cliente inactivo: " + username);
        }

        return new User(
                cliente.getEmail(),
                cliente.getPassword(),
                new ArrayList<>());
    }
}
