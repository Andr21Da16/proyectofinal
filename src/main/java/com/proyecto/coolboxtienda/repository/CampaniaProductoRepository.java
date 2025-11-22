package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.CampaniaProducto;
import com.proyecto.coolboxtienda.entity.CampaniaProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CampaniaProductoRepository extends JpaRepository<CampaniaProducto, CampaniaProductoId> {

    List<CampaniaProducto> findByCampania_IdCampania(Integer idCampania);

    List<CampaniaProducto> findByProducto_IdProducto(Integer idProducto);

    @Modifying
    @Transactional
    void deleteByCampania_IdCampania(Integer idCampania);
}
