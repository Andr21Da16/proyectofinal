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
public class CompraProveedorRequest {

    @NotNull(message = "El ID del proveedor es obligatorio")
    private Integer idProveedor;

    @NotNull(message = "El ID del colaborador es obligatorio")
    private Integer idColaborador;

    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Integer idSucursal;

    private String observaciones;

    @NotNull(message = "Los items son obligatorios")
    private List<CompraProveedorItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompraProveedorItemRequest {
        @NotNull(message = "El ID del producto es obligatorio")
        private Integer idProducto;

        @NotNull(message = "La cantidad es obligatoria")
        private Integer cantidadSolicitada;

        @NotNull(message = "El precio unitario es obligatorio")
        private BigDecimal precioUnitario;
    }
}
