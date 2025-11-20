package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaRequest {

    @NotNull(message = "El producto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer idProveedor;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private BigDecimal precioUnitario;

    private BigDecimal descuento;
}
