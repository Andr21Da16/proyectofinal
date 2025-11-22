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
public class ClienteAnalyticsResponse {
    private Integer idCliente;
    private String nombreCompleto;
    private Long totalCompras;
    private BigDecimal montoTotal;
    private BigDecimal promedioCompra;
    private String frecuenciaCompra; // ALTA, MEDIA, BAJA
}
