package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "compra_proveedor_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraProveedorItem {

    @EmbeddedId
    private CompraProveedorItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCompra")
    @JoinColumn(name = "id_compra", nullable = false)
    private CompraProveedor compraProveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "cantidad_solicitada", nullable = false)
    private Integer cantidadSolicitada;

    @Column(name = "cantidad_recibida")
    private Integer cantidadRecibida = 0;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}
