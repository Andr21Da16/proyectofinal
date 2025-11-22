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
public class PedidoResponse {
    private Integer idPedido;
    private Integer idCliente;
    private String nombreCliente;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private BigDecimal descuentoAplicado;
    private String estadoLogistico;
    private String estadoPago;
    private String metodoPago;
    private String direccionEntrega;
    private Integer idSucursal;
    private String nombreSucursal;
    private String codigoCupon;
    private LocalDateTime fechaEntrega;
    private String observaciones;
    private Integer idVentaGenerada;
    private List<PedidoItemResponse> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PedidoItemResponse {
        private Integer idProducto;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}
