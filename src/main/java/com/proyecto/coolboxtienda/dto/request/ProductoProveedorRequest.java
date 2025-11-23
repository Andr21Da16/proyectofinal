package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoProveedorRequest {

    @NotNull(message = "El ID del producto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "El ID del proveedor es obligatorio")
    private Integer idProveedor;

    @NotNull(message = "El precio del producto es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precioProducto;

    @NotNull(message = "El stock del producto es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stockProducto;
}
