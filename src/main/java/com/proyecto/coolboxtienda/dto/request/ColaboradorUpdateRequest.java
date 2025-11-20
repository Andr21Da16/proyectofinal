package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorUpdateRequest {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombreColaborador;

    @Email(message = "El email debe ser válido")
    @Size(max = 300, message = "El email no puede exceder 300 caracteres")
    private String emailColaborador;

    @Size(max = 20, message = "El número no puede exceder 20 caracteres")
    private String numeroColaborador;

    @Size(max = 50, message = "El usuario no puede exceder 50 caracteres")
    private String usuarioColaborador;

    private Integer idRol;

    private Integer idSucursal;
}
