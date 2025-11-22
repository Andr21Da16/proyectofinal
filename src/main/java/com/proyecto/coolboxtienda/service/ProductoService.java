package com.proyecto.coolboxtienda.service;

import com.proyecto.coolboxtienda.dto.request.ProductoRequest;
import com.proyecto.coolboxtienda.dto.response.PageResponse;
import com.proyecto.coolboxtienda.dto.response.ProductoDetailResponse;
import com.proyecto.coolboxtienda.dto.response.ProductoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductoService {
    ProductoResponse createProducto(ProductoRequest request, MultipartFile imagen);

    ProductoResponse updateProducto(Integer id, ProductoRequest request, MultipartFile imagen);

    void deleteProducto(Integer id);

    ProductoDetailResponse getProductoById(Integer id);

    PageResponse<ProductoResponse> getAllProductos(Pageable pageable);

    PageResponse<ProductoResponse> searchProductos(String nombre, String marca, Integer idCategoria, Pageable pageable);

    List<ProductoResponse> getProductosByCategoria(Integer idCategoria);

    List<ProductoResponse> getProductosByMarca(String marca);

    List<String> getAllMarcas();

    List<com.proyecto.coolboxtienda.dto.response.ProductoSucursalResponse> getAllProductosAllBranches();

    List<com.proyecto.coolboxtienda.dto.response.ProductoSucursalResponse> getProductosBySucursal(Integer idSucursal);
}
