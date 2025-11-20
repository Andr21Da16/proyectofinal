package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequest {

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;

    private Integer idSucursal;

    private Integer idColaborador;

    private Integer idCategoria;

    private String tipoReporte; // VENTAS, PRODUCTOS, INVENTARIO, etc.
}
