package com.proyecto.coolboxtienda.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoProveedorId implements Serializable {
    private Integer proveedor;
    private Integer producto;
}
