package com.proyecto.coolboxtienda.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SucursalProductoId implements Serializable {
    private Integer sucursal;
    private Integer producto;
    private Integer proveedor;
}
