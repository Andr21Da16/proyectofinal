package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "sucursal_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SucursalProductoId.class)
public class SucursalProducto {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private Sucursal sucursal;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @Column(name = "stock_producto", nullable = false)
    private Integer stockProducto;

    @Column(name = "precio_producto", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioProducto;
}
