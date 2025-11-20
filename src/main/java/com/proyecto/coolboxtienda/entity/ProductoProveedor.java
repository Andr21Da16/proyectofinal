package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "productos_proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProductoProveedorId.class)
public class ProductoProveedor {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "precio_producto", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioProducto;

    @Column(name = "stock_producto", nullable = false)
    private Integer stockProducto;
}
