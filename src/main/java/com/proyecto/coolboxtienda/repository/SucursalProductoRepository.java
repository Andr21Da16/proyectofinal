package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.SucursalProducto;
import com.proyecto.coolboxtienda.entity.SucursalProductoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalProductoRepository extends JpaRepository<SucursalProducto, SucursalProductoId> {
        List<SucursalProducto> findBySucursal_IdSucursal(Integer idSucursal);

        List<SucursalProducto> findByProducto_IdProducto(Integer idProducto);

        Optional<SucursalProducto> findBySucursal_IdSucursalAndProducto_IdProductoAndProveedor_IdProveedor(
                        Integer idSucursal, Integer idProducto, Integer idProveedor);

        @Query("SELECT sp FROM SucursalProducto sp WHERE sp.sucursal.idSucursal = :idSucursal AND sp.stockProducto < :minStock")
        List<SucursalProducto> findByStockBajoEnSucursal(@Param("idSucursal") Integer idSucursal,
                        @Param("minStock") Integer minStock);

        @Query("SELECT sp FROM SucursalProducto sp WHERE sp.sucursal.idSucursal = :idSucursal AND sp.stockProducto < :threshold")
        List<SucursalProducto> findLowStockProducts(@Param("idSucursal") Integer idSucursal,
                        @Param("threshold") Integer threshold);

        List<SucursalProducto> findBySucursal_IdSucursalAndProducto_IdProducto(Integer idSucursal, Integer idProducto);

        @Query("SELECT sp FROM SucursalProducto sp WHERE sp.producto.idProducto = :idProducto ORDER BY sp.precioProducto ASC")
        List<SucursalProducto> findByProductoOrderByPrecio(@Param("idProducto") Integer idProducto);

        @Query("SELECT sp FROM SucursalProducto sp WHERE sp.sucursal.idSucursal = :idSucursal AND sp.stockProducto > 0")
        List<SucursalProducto> findBySucursalWithStock(@Param("idSucursal") Integer idSucursal);

        List<SucursalProducto> findByStockProductoLessThan(Integer threshold);
}
