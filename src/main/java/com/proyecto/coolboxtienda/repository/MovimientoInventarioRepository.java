package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {
    List<MovimientoInventario> findByProducto_IdProducto(Integer idProducto);

    List<MovimientoInventario> findBySucursal_IdSucursal(Integer idSucursal);

    List<MovimientoInventario> findByFechaMovimientoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
