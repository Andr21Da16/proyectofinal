package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteVentasResponse {
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal totalVentas;
    private Long cantidadVentas;
    private BigDecimal promedioVenta;
    private List<VentasPorVendedorDTO> ventasPorVendedor;
    private List<VentasPorSucursalDTO> ventasPorSucursal;
    private Map<String, BigDecimal> ventasPorMetodoPago;
    private List<ProductoMasVendidoDTO> productosMasVendidos;
}
