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
public class VentasPorVendedorDTO {
    private String nombreVendedor;
    private Long cantidadVentas;
    private BigDecimal totalVentas;
}
