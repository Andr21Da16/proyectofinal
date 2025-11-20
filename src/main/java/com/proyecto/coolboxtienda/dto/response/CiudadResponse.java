package com.proyecto.coolboxtienda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CiudadResponse {
    private Integer idCiudad;
    private String nombreCiudad;
    private Integer idDepartamento;
    private String nombreDepartamento;
}
