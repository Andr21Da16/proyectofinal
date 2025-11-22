package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Cupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuponRepository extends JpaRepository<Cupon, Integer> {

    Optional<Cupon> findByCodigo(String codigo);

    List<Cupon> findByActivoTrue();

    @Query("SELECT c FROM Cupon c WHERE c.codigo = :codigo AND c.activo = true AND " +
            "c.fechaInicio <= :now AND c.fechaFin >= :now AND c.usoActual < c.usoMaximo")
    Optional<Cupon> findValidCupon(@Param("codigo") String codigo, @Param("now") LocalDateTime now);
}
