package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraProveedorResponse {
    private Integer idCompra;
    private Integer idProveedor;
    private String nombreProveedor;
    private LocalDateTime fechaCompra;
    private BigDecimal total;
    private String estado;
    private Integer idColaborador;
    private String nombreColaborador;
    private Integer idSucursal;
    private String nombreSucursal;
    private String observaciones;
    private List<CompraProveedorItemResponse> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompraProveedorItemResponse {
        private Integer idProducto;
        private String nombreProducto;
        private Integer cantidadSolicitada;
        private Integer cantidadRecibida;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}
