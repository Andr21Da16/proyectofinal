package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Integer> {
        List<Descuento> findByProducto_IdProducto(Integer idProducto);

        @Query("SELECT d FROM Descuento d WHERE d.producto.idProducto = :idProducto AND " +
                        "d.activo = true AND :fecha BETWEEN d.fechaInicio AND d.fechaFin")
        Optional<Descuento> findDescuentoActivoByProducto(
                        @Param("idProducto") Integer idProducto,
                        @Param("fecha") LocalDateTime fecha);

        @Query("SELECT d FROM Descuento d WHERE d.activo = true AND :fecha BETWEEN d.fechaInicio AND d.fechaFin")
        List<Descuento> findDescuentosActivos(@Param("fecha") LocalDateTime fecha);
}
