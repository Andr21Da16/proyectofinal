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
public class ProductoSucursalResponse {
    private Integer idProducto;
    private String nombreProducto;
    private String marcaProducto;
    private String modeloProducto;
    private String urlImagenProducto;
    private String categoriaNombre;
    private Boolean activo;

    private Integer idSucursal;
    private String nombreSucursal;

    private BigDecimal precio;
    private Integer stock;
}
