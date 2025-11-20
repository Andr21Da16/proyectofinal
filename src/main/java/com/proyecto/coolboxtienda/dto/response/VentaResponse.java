package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaResponse {
    private Integer idVenta;
    private LocalDateTime fechaVenta;
    private BigDecimal totalVenta;
    private String metodoPago;
    private String estadoVenta;
    private String nombreColaborador;
    private String nombreSucursal;
    private String observaciones;
    private List<DetalleVentaResponse> detalles;
}
