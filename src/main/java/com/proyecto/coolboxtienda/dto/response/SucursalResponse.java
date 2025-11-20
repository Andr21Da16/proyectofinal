package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucursalResponse {
    private Integer idSucursal;
    private String nombreSucursal;
    private String direccionSucursal;
    private String telefonoSucursal;
    private String nombreCiudad;
    private String nombreDepartamento;
    private Boolean activo;
}
