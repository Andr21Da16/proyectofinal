package com.proyecto.coolboxtienda.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaniaProductoId implements Serializable {

    @Column(name = "id_campania")
    private Integer idCampania;

    @Column(name = "id_producto")
    private Integer idProducto;
}
