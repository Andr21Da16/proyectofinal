package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.CampaniaMarketing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CampaniaMarketingRepository extends JpaRepository<CampaniaMarketing, Integer> {

    List<CampaniaMarketing> findByActivoTrue();

    @Query("SELECT c FROM CampaniaMarketing c WHERE c.activo = true AND " +
            "c.fechaInicio <= :now AND c.fechaFin >= :now")
    List<CampaniaMarketing> findActiveCampanias(@Param("now") LocalDateTime now);
}
