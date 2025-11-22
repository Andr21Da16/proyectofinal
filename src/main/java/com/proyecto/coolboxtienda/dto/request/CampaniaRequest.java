package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaniaRequest {

    @NotBlank(message = "El nombre de la campaña es obligatorio")
    private String nombreCampania;

    private String descripcion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;

    @NotNull(message = "El descuento es obligatorio")
    private BigDecimal descuentoPorcentaje;

    private Boolean activo = true;

    private String tipoDescuento = "PORCENTAJE";

    @NotNull(message = "Los productos son obligatorios")
    private List<Integer> idProductos;
}
