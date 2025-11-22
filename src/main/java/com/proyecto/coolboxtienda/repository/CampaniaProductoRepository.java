package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.CampaniaProducto;
import com.proyecto.coolboxtienda.entity.CampaniaProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaniaProductoRepository extends JpaRepository<CampaniaProducto, CampaniaProductoId> {

    List<CampaniaProducto> findByCampania_IdCampania(Integer idCampania);

    List<CampaniaProducto> findByProducto_IdProducto(Integer idProducto);
}
