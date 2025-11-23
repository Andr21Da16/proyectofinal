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
public class ProductoProveedorResponse {

    private Integer idProducto;
    private String nombreProducto;
    private String marca;
    private String modelo;
    private String nombreCategoria;
    private Integer idProveedor;
    private String nombreProveedor;
    private BigDecimal precioProducto;
    private Integer stockProducto;
}
