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
public class ProductoDetailResponse {
    private Integer idProducto;
    private String nombreProducto;
    private String marcaProducto;
    private String modeloProducto;
    private String dimensionesProducto;
    private String especificacionesProducto;
    private BigDecimal pesoProducto;
    private String urlImagenProducto;
    private String categoriaNombre;
    private Integer idCategoria;
    private Boolean activo;
    private BigDecimal descuentoActual;
    private BigDecimal porcentajeDescuento;
    private List<InventarioResponse> inventario;
    private List<ProveedorSimpleResponse> proveedores;
}
