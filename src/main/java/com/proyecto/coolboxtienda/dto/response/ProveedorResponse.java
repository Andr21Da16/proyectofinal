package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorResponse {
    private Integer idProveedor;
    private String nombreProveedor;
    private String rucProveedor;
    private String direccionProveedor;
    private String telefonoProveedor;
    private String emailProveedor;
    private String nombreCiudad;
    private String nombreDepartamento;
    private Boolean activo;
}
