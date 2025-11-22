package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCliente;

    private String metodoPago;
    private String direccionEntrega;
    private Integer idSucursal;
    private Integer idCupon;
    private String observaciones;

    @NotNull(message = "Los items del pedido son obligatorios")
    private List<PedidoItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PedidoItemRequest {
        @NotNull(message = "El ID del producto es obligatorio")
        private Integer idProducto;

        @NotNull(message = "La cantidad es obligatoria")
        private Integer cantidad;

        @NotNull(message = "El precio unitario es obligatorio")
        private BigDecimal precioUnitario;
    }
}
