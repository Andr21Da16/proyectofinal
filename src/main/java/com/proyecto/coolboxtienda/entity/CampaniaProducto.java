package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "campania_productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaniaProducto {

    @EmbeddedId
    private CampaniaProductoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCampania")
    @JoinColumn(name = "id_campania", nullable = false)
    private CampaniaMarketing campania;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
}
