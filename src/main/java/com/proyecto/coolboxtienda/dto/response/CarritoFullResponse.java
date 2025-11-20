package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoFullResponse {
    private Integer idCarrito;
    private Integer idColaborador;
    private String nombreColaborador;
    private List<CarritoDetalleResponse> items;
    private Integer cantidadItems;
    private BigDecimal subtotal;
    private BigDecimal descuentos;
    private BigDecimal total;
}
