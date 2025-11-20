package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.ReporteRequest;
import com.proyecto.coolboxtienda.dto.response.ProductoMasVendidoDTO;
import com.proyecto.coolboxtienda.dto.response.ReporteVentasResponse;
import com.proyecto.coolboxtienda.dto.response.VentasPorSucursalDTO;
import com.proyecto.coolboxtienda.dto.response.VentasPorVendedorDTO;
import com.proyecto.coolboxtienda.repository.DetalleVentaRepository;
import com.proyecto.coolboxtienda.repository.VentaRepository;
import com.proyecto.coolboxtienda.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        BigDecimal total = ventaRepository.getTotalVentasByPeriodo(fechaInicio, fechaFin);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentasPorVendedorDTO> getVentasPorVendedor(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> results = ventaRepository.getVentasPorVendedor(fechaInicio, fechaFin);
        return results.stream()
                .map(row -> new VentasPorVendedorDTO(
                        (String) row[0], // nombreVendedor
                        (Long) row[1], // cantidadVentas (Long, not int)
                        (BigDecimal) row[2])) // totalVentas
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentasPorSucursalDTO> getVentasPorSucursal(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> results = ventaRepository.getVentasPorSucursal(fechaInicio, fechaFin);
        return results.stream()
                .map(row -> new VentasPorSucursalDTO(
                        (String) row[0], // nombreSucursal
                        (Long) row[1], // cantidadVentas (Long, not int)
                        (BigDecimal) row[2])) // totalVentas
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoMasVendidoDTO> getProductosMasVendidos(LocalDateTime fechaInicio, LocalDateTime fechaFin,
            Integer limit) {
        List<Object[]> results = detalleVentaRepository.getProductosMasVendidos(fechaInicio, fechaFin, limit);
        return results.stream()
                .map(row -> new ProductoMasVendidoDTO(
                        (String) row[0], // nombreProducto
                        null, // marcaProducto (not in query, set to null)
                        (Long) row[1])) // cantidadVendida
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getVentasPorMetodoPago(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> results = ventaRepository.getVentasPorMetodoPago(fechaInicio, fechaFin);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (BigDecimal) row[2])); // row[2] es el total, row[1] es el count
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteVentasResponse getReporteCompleto(ReporteRequest request) {
        BigDecimal totalVentas = getVentasPorPeriodo(request.getFechaInicio(), request.getFechaFin());
        List<VentasPorVendedorDTO> ventasPorVendedor = getVentasPorVendedor(request.getFechaInicio(),
                request.getFechaFin());
        List<VentasPorSucursalDTO> ventasPorSucursal = getVentasPorSucursal(request.getFechaInicio(),
                request.getFechaFin());
        List<ProductoMasVendidoDTO> productosMasVendidos = getProductosMasVendidos(request.getFechaInicio(),
                request.getFechaFin(), 10);
        Map<String, BigDecimal> ventasPorMetodoPago = getVentasPorMetodoPago(request.getFechaInicio(),
                request.getFechaFin());

        return ReporteVentasResponse.builder()
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .totalVentas(totalVentas)
                .ventasPorVendedor(ventasPorVendedor)
                .ventasPorSucursal(ventasPorSucursal)
                .productosMasVendidos(productosMasVendidos)
                .ventasPorMetodoPago(ventasPorMetodoPago)
                .build();
    }
}
