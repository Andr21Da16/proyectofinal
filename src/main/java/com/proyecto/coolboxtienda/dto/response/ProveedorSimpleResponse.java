package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorSimpleResponse {
    private Integer idProveedor;
    private String nombreProveedor;
    private String rucProveedor;
    private String telefonoProveedor;
}
