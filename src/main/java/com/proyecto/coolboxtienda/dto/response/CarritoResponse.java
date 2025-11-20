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
public class CarritoResponse {
    private Integer idCarrito;
    private Integer idColaborador;
    private String nombreColaborador;
    private Integer cantidadItems;
    private BigDecimal total;
}
