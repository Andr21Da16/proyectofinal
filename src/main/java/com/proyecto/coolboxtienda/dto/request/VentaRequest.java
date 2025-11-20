package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequest {

    @NotNull(message = "La sucursal es obligatoria")
    private Integer idSucursal;

    @NotNull(message = "El colaborador es obligatorio")
    private Integer idColaborador;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 50, message = "El método de pago no puede exceder 50 caracteres")
    private String metodoPago;

    @NotNull(message = "El carrito es obligatorio")
    private Integer idCarrito;

    private List<DetalleVentaRequest> detalles;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}
