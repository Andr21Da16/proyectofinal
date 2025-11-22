package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.RolPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, Integer> {

    List<RolPermiso> findAllByRol_IdRol(Integer idRol);

    Optional<RolPermiso> findByRol_IdRolAndNombreModulo(Integer idRol, String nombreModulo);

    @Query("SELECT rp FROM RolPermiso rp WHERE rp.rol.idRol = :idRol AND rp.puedeVer = true")
    List<RolPermiso> findAccessibleModulesByRol(@Param("idRol") Integer idRol);
}
