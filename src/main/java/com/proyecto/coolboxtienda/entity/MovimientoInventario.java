package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private Sucursal sucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private Proveedor proveedor;

    @Column(name = "tipo_movimiento", nullable = false, length = 50)
    private String tipoMovimiento; // ENTRADA, SALIDA, TRANSFERENCIA_SALIDA, TRANSFERENCIA_ENTRADA, AJUSTE

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento = LocalDateTime.now();

    @Column(name = "motivo", columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "usuario", length = 100)
    private String usuario;
}
