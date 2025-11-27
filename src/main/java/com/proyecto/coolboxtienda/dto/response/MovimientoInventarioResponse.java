package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventarioResponse {

    private Integer idMovimiento;
    private String nombreProducto;
    private String nombreSucursal;
    private String nombreProveedor;
    private String tipoMovimiento;
    private Integer cantidad;
    private LocalDateTime fechaMovimiento;
    private String motivo;
    private String usuario;
}