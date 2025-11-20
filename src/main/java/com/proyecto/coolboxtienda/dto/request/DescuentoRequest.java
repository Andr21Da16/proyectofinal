package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoRequest {

    @NotNull(message = "El producto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @Min(value = 0, message = "El descuento debe ser al menos 0")
    @Max(value = 100, message = "El descuento no puede exceder 100")
    private BigDecimal porcentajeDescuento;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;
}
