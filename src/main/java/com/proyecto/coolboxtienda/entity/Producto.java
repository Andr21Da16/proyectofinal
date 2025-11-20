package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "nombre_producto", nullable = false, unique = true, length = 100)
    private String nombreProducto;

    @Column(name = "marca_producto", nullable = false, length = 50)
    private String marcaProducto;

    @Column(name = "modelo_producto", nullable = false, length = 100)
    private String modeloProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "dimensiones_producto", columnDefinition = "TEXT")
    private String dimensionesProducto;

    @Column(name = "especificaciones_producto", columnDefinition = "TEXT")
    private String especificacionesProducto;

    @Column(name = "peso_producto", precision = 10, scale = 2)
    private BigDecimal pesoProducto;

    @Column(name = "url_imagen_producto", nullable = false, columnDefinition = "TEXT")
    private String urlImagenProducto;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
