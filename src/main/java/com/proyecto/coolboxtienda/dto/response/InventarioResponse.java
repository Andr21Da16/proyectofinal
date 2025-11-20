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
public class InventarioResponse {
    private Integer idSucursal;
    private String nombreSucursal;
    private Integer idProveedor;
    private String nombreProveedor;
    private Integer stockProducto;
    private BigDecimal precioProducto;
}
