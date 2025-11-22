package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuponResponse {
    private Integer idCupon;
    private String codigo;
    private String descripcion;
    private BigDecimal descuentoPorcentaje;
    private BigDecimal descuentoMonto;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer usoMaximo;
    private Integer usoActual;
    private Boolean activo;
    private BigDecimal montoMinimo;
}
