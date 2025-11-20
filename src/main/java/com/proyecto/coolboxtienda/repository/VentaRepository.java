package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
        List<Venta> findByColaborador_IdColaborador(Integer idColaborador);

        Page<Venta> findByColaborador_IdColaborador(Integer idColaborador, Pageable pageable);

        List<Venta> findBySucursal_IdSucursal(Integer idSucursal);

        Page<Venta> findBySucursal_IdSucursal(Integer idSucursal, Pageable pageable);

        // Métodos para consultas con fecha
        List<Venta> findByColaborador_IdColaboradorAndFechaVentaAfter(Integer idColaborador, LocalDateTime fechaInicio);

        List<Venta> findBySucursal_IdSucursalAndFechaVentaAfter(Integer idSucursal, LocalDateTime fechaInicio);

        List<Venta> findByFechaVentaAfter(LocalDateTime fechaInicio);

        @Query("SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
        List<Venta> findByFechaVentaBetween(
                        @Param("fechaInicio") LocalDateTime fechaInicio,
                        @Param("fechaFin") LocalDateTime fechaFin);

        @Query("SELECT v FROM Venta v WHERE " +
                        "v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND " +
                        "v.sucursal.idSucursal = :idSucursal")
        List<Venta> findByFechaAndSucursal(
                        @Param("fechaInicio") LocalDateTime fechaInicio,
                        @Param("fechaFin") LocalDateTime fechaFin,
                        @Param("idSucursal") Integer idSucursal);

        @Query("SELECT v FROM Venta v WHERE " +
                        "v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND " +
                        "v.colaborador.idColaborador = :idColaborador")
        List<Venta> findByFechaAndColaborador(
                        @Param("fechaInicio") LocalDateTime fechaInicio,
                        @Param("fechaFin") LocalDateTime fechaFin,
                        @Param("idColaborador") Integer idColaborador);

        @Query("SELECT v FROM Venta v WHERE " +
                        "(:fechaInicio IS NULL OR v.fechaVenta >= :fechaInicio) AND " +
                        "(:fechaFin IS NULL OR v.fechaVenta <= :fechaFin) AND " +
                        "(:idSucursal IS NULL OR v.sucursal.idSucursal = :idSucursal) AND " +
                        "(:idColaborador IS NULL OR v.colaborador.idColaborador = :idColaborador) AND " +
                        "(:idEstado IS NULL OR v.estadoVenta.idEstadoVenta = :idEstado)")
        Page<Venta> findVentasConFiltros(
                        @Param("fechaInicio") LocalDateTime fechaInicio,
                        @Param("fechaFin") LocalDateTime fechaFin,
                        @Param("idSucursal") Integer idSucursal,
                        @Param("idColaborador") Integer idColaborador,
                        @Param("idEstado") Integer idEstado,
                        Pageable pageable);

        @Query("SELECT SUM(v.total) FROM Venta v WHERE " +
                        "v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND " +
                        "v.estadoVenta.nombreEstado = 'COMPLETADA'")
        BigDecimal getTotalVentasByPeriodo(
                        @Param("fechaInicio") LocalDateTime fechaInicio,
                        @Param("fechaFin") LocalDateTime fechaFin);

        @Query("SELECT v.colaborador.nombreColaborador, COUNT(v), SUM(v.total) FROM Venta v " +
                        "WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin " +
                        "AND v.estadoVenta.nombreEstado = 'COMPLETADA' " +
                        "GROUP BY v.colaborador.idColaborador, v.colaborador.nombreColaborador " +
                        "ORDER BY SUM(v.total) DESC")
        List<Object[]> getVentasPorVendedor(
                        @Param("fechaInicio") LocalDateTime fechaInicio,
                        @Param("fechaFin") LocalDateTime fechaFin);

        @Query("SELECT v.sucursal.nombreSucursal, COUNT(v), SUM(v.total) FROM Venta v " +
                        "WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin " +
                        "AND v.estadoVenta.nombreEstado = 'COMPLETADA' " +
                        "GROUP BY v.sucursal.idSucursal, v.sucursal.nombreSucursal " +
                        "ORDER BY SUM(v.total) DESC")
        List<Object[]> getVentasPorSucursal(
                        @Param("fechaInicio") LocalDateTime fechaInicio,
                        @Param("fechaFin") LocalDateTime fechaFin);

        @Query("SELECT v.metodoPago, COUNT(v), SUM(v.total) FROM Venta v " +
                        "WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin " +
                        "AND v.estadoVenta.nombreEstado = 'COMPLETADA' " +
                        "GROUP BY v.metodoPago")
        List<Object[]> getVentasPorMetodoPago(
                        @Param("fechaInicio") LocalDateTime fechaInicio,
                        @Param("fechaFin") LocalDateTime fechaFin);

        @Query("SELECT COUNT(v) FROM Venta v WHERE " +
                        "v.colaborador.idColaborador = :idColaborador AND " +
                        "v.fechaVenta >= :fechaInicio")
        Long countVentasByColaboradorSinceFecha(
                        @Param("idColaborador") Integer idColaborador,
                        @Param("fechaInicio") LocalDateTime fechaInicio);
}
