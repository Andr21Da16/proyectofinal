package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.VentaRequest;
import com.proyecto.coolboxtienda.dto.response.PageResponse;
import com.proyecto.coolboxtienda.dto.response.VentaResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface VentaService {
    VentaResponse createVenta(VentaRequest request);

    VentaResponse getVentaById(Integer id);

    PageResponse<VentaResponse> getVentasByColaborador(Integer idColaborador, Pageable pageable);

    PageResponse<VentaResponse> getVentasBySucursal(Integer idSucursal, Pageable pageable);

    VentaResponse updateEstadoVenta(Integer idVenta, Integer idEstado);

    void cancelVenta(Integer idVenta);

    PageResponse<VentaResponse> getVentasByFiltros(LocalDateTime fechaInicio, LocalDateTime fechaFin,
            Integer idSucursal, Integer idColaborador,
            Integer idEstado, Pageable pageable);
}
