package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.CarritoDetalle;
import com.proyecto.coolboxtienda.entity.CarritoDetalleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, CarritoDetalleId> {
    List<CarritoDetalle> findByCarrito_IdCarrito(Integer idCarrito);

    @Query("SELECT SUM(cd.cantidad * cd.precioUnitario) FROM CarritoDetalle cd WHERE cd.carrito.idCarrito = :idCarrito")
    BigDecimal calculateCartTotal(@Param("idCarrito") Integer idCarrito);

    Integer countByCarrito_IdCarrito(Integer idCarrito);

    void deleteByCarrito_IdCarrito(Integer idCarrito);
}
