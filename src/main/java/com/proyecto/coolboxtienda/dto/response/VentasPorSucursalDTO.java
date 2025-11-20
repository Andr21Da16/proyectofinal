package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentasPorSucursalDTO {
    private String nombreSucursal;
    private Long cantidadVentas;
    private BigDecimal totalVentas;
}
