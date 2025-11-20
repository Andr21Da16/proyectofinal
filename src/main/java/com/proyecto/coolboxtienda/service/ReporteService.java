package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.ReporteRequest;
import com.proyecto.coolboxtienda.dto.response.ProductoMasVendidoDTO;
import com.proyecto.coolboxtienda.dto.response.ReporteVentasResponse;
import com.proyecto.coolboxtienda.dto.response.VentasPorSucursalDTO;
import com.proyecto.coolboxtienda.dto.response.VentasPorVendedorDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ReporteService {
    BigDecimal getVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<VentasPorVendedorDTO> getVentasPorVendedor(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<VentasPorSucursalDTO> getVentasPorSucursal(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<ProductoMasVendidoDTO> getProductosMasVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin,
            Integer limit);

    Map<String, BigDecimal> getVentasPorMetodoPago(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    ReporteVentasResponse getReporteCompleto(ReporteRequest request);
}
