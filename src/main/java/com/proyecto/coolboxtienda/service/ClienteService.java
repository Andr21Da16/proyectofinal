package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.ClienteRequest;
import com.proyecto.coolboxtienda.dto.response.ClienteAnalyticsResponse;
import com.proyecto.coolboxtienda.dto.response.ClienteResponse;
import com.proyecto.coolboxtienda.dto.response.VentaResponse;

import java.util.List;

public interface ClienteService {
    List<ClienteResponse> getAllClientes();

    List<ClienteResponse> getClientesByFilters(String nombre, String email, String numeroDocumento, Boolean activo);

    ClienteResponse getClienteById(Integer id);

    ClienteResponse createCliente(ClienteRequest request);

    ClienteResponse updateCliente(Integer id, ClienteRequest request);

    void deleteCliente(Integer id);

    List<VentaResponse> getHistorialCompras(Integer idCliente);

    ClienteAnalyticsResponse getClienteAnalytics(Integer idCliente);
}
