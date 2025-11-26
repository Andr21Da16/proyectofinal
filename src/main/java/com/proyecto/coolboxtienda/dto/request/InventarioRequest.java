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
public class InventarioRequest {

    @NotNull(message = "La sucursal es obligatoria")
    private Integer idSucursal;

    @NotNull(message = "El producto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer idProveedor;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock debe ser mayor o igual a 0")
    private Integer stockProducto;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio debe ser mayor a 0")
    private BigDecimal precioProducto;

    private String tipoAjuste; // "ENTRADA", "SALIDA", o null/"AJUSTE"
}
