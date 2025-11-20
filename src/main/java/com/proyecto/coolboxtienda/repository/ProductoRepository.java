package com.proyecto.coolboxtienda.repository;

import com.proyecto.coolboxtienda.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
        List<Producto> findByActivoTrue();

        Page<Producto> findByActivoTrue(Pageable pageable);

        List<Producto> findByCategoria_IdCategoria(Integer idCategoria);

        List<Producto> findByCategoria_IdCategoriaAndActivoTrue(Integer idCategoria);

        @Query("SELECT p FROM Producto p WHERE " +
                        "(LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(p.marcaProducto) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(p.modeloProducto) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
                        "p.activo = true")
        List<Producto> searchProductos(@Param("search") String search);

        @Query("SELECT p FROM Producto p WHERE " +
                        "(:nombre IS NULL OR LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
                        "(:marca IS NULL OR LOWER(p.marcaProducto) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
                        "(:idCategoria IS NULL OR p.categoria.idCategoria = :idCategoria) AND " +
                        "p.activo = true")
        Page<Producto> searchProductosAvanzado(
                        @Param("nombre") String nombre,
                        @Param("marca") String marca,
                        @Param("idCategoria") Integer idCategoria,
                        Pageable pageable);

        List<Producto> findByMarcaProductoAndActivoTrue(String marca);

        @Query("SELECT DISTINCT p.marcaProducto FROM Producto p WHERE p.activo = true ORDER BY p.marcaProducto")
        List<String> findAllMarcas();
}
