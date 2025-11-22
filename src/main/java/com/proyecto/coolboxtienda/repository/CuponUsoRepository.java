package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.CuponUso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuponUsoRepository extends JpaRepository<CuponUso, Integer> {

    List<CuponUso> findByCupon_IdCupon(Integer idCupon);

    List<CuponUso> findByVenta_IdVenta(Integer idVenta);

    List<CuponUso> findByPedido_IdPedido(Integer idPedido);
}
