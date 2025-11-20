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
public class CarritoDetalleResponse {
    private Integer idProducto;
    private String nombreProducto;
    private String marcaProducto;
    private String urlImagenProducto;
    private Integer idProveedor;
    private String nombreProveedor;
    private Integer idSucursal;
    private String nombreSucursal;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
