package com.proyecto.coolboxtienda.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaRequest {

    @NotNull(message = "El producto es obligatorio")
    private Integer idProducto;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer idProveedor;

    @NotNull(message = "La sucursal de origen es obligatoria")
    private Integer idSucursalOrigen;

    @NotNull(message = "La sucursal de destino es obligatoria")
    private Integer idSucursalDestino;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
