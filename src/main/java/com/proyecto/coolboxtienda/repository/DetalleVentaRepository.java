package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.DetalleVenta;
import com.proyecto.coolboxtienda.entity.DetalleVentaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, DetalleVentaId> {
    List<DetalleVenta> findByVenta_IdVenta(Integer idVenta);

    @Query("SELECT SUM(dv.cantidad * dv.precioUnitario - dv.descuento) FROM DetalleVenta dv WHERE dv.venta.idVenta = :idVenta")
    BigDecimal calculateVentaTotal(@Param("idVenta") Integer idVenta);

    @Query("SELECT dv.producto.nombreProducto, SUM(dv.cantidad), SUM(dv.cantidad * dv.precioUnitario) " +
            "FROM DetalleVenta dv " +
            "WHERE dv.venta.fechaVenta BETWEEN :fechaInicio AND :fechaFin " +
            "AND dv.venta.estadoVenta.nombreEstado = 'COMPLETADA' " +
            "GROUP BY dv.producto.idProducto, dv.producto.nombreProducto " +
            "ORDER BY SUM(dv.cantidad) DESC")
    List<Object[]> getProductosMasVendidos(
            @Param("fechaInicio") java.time.LocalDateTime fechaInicio,
            @Param("fechaFin") java.time.LocalDateTime fechaFin,
            @Param("limit") Integer limit);
}
