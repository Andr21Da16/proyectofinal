package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.ClienteRequest;
import com.proyecto.coolboxtienda.dto.response.ClienteAnalyticsResponse;
import com.proyecto.coolboxtienda.dto.response.ClienteResponse;
import com.proyecto.coolboxtienda.dto.response.VentaResponse;
import com.proyecto.coolboxtienda.entity.Ciudad;
import com.proyecto.coolboxtienda.entity.Cliente;
import com.proyecto.coolboxtienda.entity.Venta;
import com.proyecto.coolboxtienda.repository.CiudadRepository;
import com.proyecto.coolboxtienda.repository.ClienteRepository;
import com.proyecto.coolboxtienda.repository.VentaRepository;
import com.proyecto.coolboxtienda.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final CiudadRepository ciudadRepository;
    private final VentaRepository ventaRepository;

    @Override
    public List<ClienteResponse> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::toClienteResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteResponse> getClientesByFilters(String nombre, String email, String numeroDocumento,
            Boolean activo) {
        return clienteRepository.findByFilters(nombre, email, numeroDocumento, activo).stream()
                .map(this::toClienteResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponse getClienteById(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return toClienteResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponse createCliente(ClienteRequest request) {
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
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setTipoDocumento(request.getTipoDocumento());
        cliente.setNumeroDocumento(request.getNumeroDocumento());
        cliente.setActivo(request.getActivo());

        if (request.getIdCiudad() != null) {
            Ciudad ciudad = ciudadRepository.findById(request.getIdCiudad())
                    .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            cliente.setCiudad(ciudad);
        }

        cliente = clienteRepository.save(cliente);
        return toClienteResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponse updateCliente(Integer id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Validar email único (excepto el actual)
        clienteRepository.findByEmail(request.getEmail()).ifPresent(c -> {
            if (!c.getIdCliente().equals(id)) {
                throw new RuntimeException("El email ya está registrado");
            }
        });

        // Validar documento único (excepto el actual)
        if (request.getNumeroDocumento() != null) {
            clienteRepository.findByNumeroDocumento(request.getNumeroDocumento()).ifPresent(c -> {
                if (!c.getIdCliente().equals(id)) {
                    throw new RuntimeException("El número de documento ya está registrado");
                }
            });
        }

        cliente.setNombreCompleto(request.getNombreCompleto());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setTipoDocumento(request.getTipoDocumento());
        cliente.setNumeroDocumento(request.getNumeroDocumento());
        cliente.setActivo(request.getActivo());

        if (request.getIdCiudad() != null) {
            Ciudad ciudad = ciudadRepository.findById(request.getIdCiudad())
                    .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
            cliente.setCiudad(ciudad);
        } else {
            cliente.setCiudad(null);
        }

        cliente = clienteRepository.save(cliente);
        return toClienteResponse(cliente);
    }

    @Override
    @Transactional
    public void deleteCliente(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Override
    public List<VentaResponse> getHistorialCompras(Integer idCliente) {
        // Verificar que el cliente existe
        clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // TODO: Implementar cuando se cree la relación Cliente-Venta
        // Por ahora retornamos lista vacía
        return List.of();
    }

    @Override
    public ClienteAnalyticsResponse getClienteAnalytics(Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // TODO: Implementar cuando se cree la relación Cliente-Venta
        // Por ahora retornamos analytics básicos
        return ClienteAnalyticsResponse.builder()
                .idCliente(cliente.getIdCliente())
                .nombreCompleto(cliente.getNombreCompleto())
                .totalCompras(0L)
                .montoTotal(BigDecimal.ZERO)
                .promedioCompra(BigDecimal.ZERO)
                .frecuenciaCompra("BAJA")
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
