package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.RolPermiso;
import com.proyecto.coolboxtienda.entity.RolPermisoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {

    @Query("SELECT rp FROM RolPermiso rp WHERE rp.id.idRol = :idRol")
    List<RolPermiso> findAllByIdRol(@Param("idRol") Integer idRol);

    @Query("SELECT rp FROM RolPermiso rp WHERE rp.id.idRol = :idRol AND rp.id.nombreModulo = :nombreModulo")
    Optional<RolPermiso> findByIdRolAndNombreModulo(@Param("idRol") Integer idRol,
            @Param("nombreModulo") String nombreModulo);

    @Query("SELECT rp FROM RolPermiso rp WHERE rp.rol.idRol = :idRol AND rp.puedeVer = true")
    List<RolPermiso> findAccessibleModulesByRol(@Param("idRol") Integer idRol);
}
