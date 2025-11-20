package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El usuario es obligatorio")
    private String usuarioColaborador;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contraseniaColaborador;
}
