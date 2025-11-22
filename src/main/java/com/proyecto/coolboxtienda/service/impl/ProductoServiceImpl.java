package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.ProductoRequest;
import com.proyecto.coolboxtienda.dto.response.PageResponse;
import com.proyecto.coolboxtienda.dto.response.ProductoDetailResponse;
import com.proyecto.coolboxtienda.dto.response.ProductoResponse;
import com.proyecto.coolboxtienda.entity.Categoria;
import com.proyecto.coolboxtienda.entity.Producto;
import com.proyecto.coolboxtienda.entity.ProductoProveedor;
import com.proyecto.coolboxtienda.entity.SucursalProducto;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.mapper.ResponseMapper;
import com.proyecto.coolboxtienda.repository.CategoriaRepository;
import com.proyecto.coolboxtienda.repository.ProductoProveedorRepository;
import com.proyecto.coolboxtienda.repository.ProductoRepository;
import com.proyecto.coolboxtienda.repository.SucursalProductoRepository;
import com.proyecto.coolboxtienda.service.FileUploadService;
import com.proyecto.coolboxtienda.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SucursalProductoRepository sucursalProductoRepository;
    private final ProductoProveedorRepository productoProveedorRepository;
    private final FileUploadService fileUploadService;
    private final EntityMapper entityMapper;
    private final ResponseMapper responseMapper;

    @Override
    @Transactional
    public ProductoResponse createProducto(ProductoRequest request, MultipartFile imagen) {
        // Validar categoría
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Subir imagen
        String urlImagen = null;
        if (imagen != null && !imagen.isEmpty()) {
            urlImagen = fileUploadService.upload(imagen);
        }

        // Crear producto
        Producto producto = entityMapper.toProductoEntity(request, categoria, urlImagen);
        producto = productoRepository.save(producto);

        return entityMapper.toProductoResponse(producto);
    }

    @Override
    @Transactional
    public ProductoResponse updateProducto(Integer id, ProductoRequest request, MultipartFile imagen) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Subir nueva imagen si se proporciona
        String urlImagen = null;
        if (imagen != null && !imagen.isEmpty()) {
            // Eliminar imagen anterior
            if (producto.getUrlImagenProducto() != null) {
                fileUploadService.delete(producto.getUrlImagenProducto());
            }
            urlImagen = fileUploadService.upload(imagen);
        }

        entityMapper.updateProductoEntity(producto, request, categoria, urlImagen);
        producto = productoRepository.save(producto);

        return entityMapper.toProductoResponse(producto);
    }

    @Override
    @Transactional
    public void deleteProducto(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDetailResponse getProductoById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        List<SucursalProducto> inventario = sucursalProductoRepository.findByProducto_IdProducto(id);
        List<ProductoProveedor> proveedores = productoProveedorRepository.findByProducto_IdProducto(id);

        return entityMapper.toProductoDetailResponse(producto, inventario, proveedores);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductoResponse> getAllProductos(Pageable pageable) {
        Page<Producto> page = productoRepository.findByActivoTrue(pageable);
        Page<ProductoResponse> responsePage = page.map(entityMapper::toProductoResponse);
        return responseMapper.toPageResponse(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductoResponse> searchProductos(String nombre, String marca, Integer idCategoria,
            Pageable pageable) {
        Page<Producto> page = productoRepository.searchProductosAvanzado(nombre, marca, idCategoria, pageable);
        Page<ProductoResponse> responsePage = page.map(entityMapper::toProductoResponse);
        return responseMapper.toPageResponse(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> getProductosByCategoria(Integer idCategoria) {
        List<Producto> productos = productoRepository.findByCategoria_IdCategoriaAndActivoTrue(idCategoria);
        return productos.stream()
                .map(entityMapper::toProductoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> getProductosByMarca(String marca) {
        List<Producto> productos = productoRepository.findByMarcaProductoAndActivoTrue(marca);
        return productos.stream()
                .map(entityMapper::toProductoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllMarcas() {
        return productoRepository.findAllMarcas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> getAllProductosAllBranches() {
        // Obtener todos los productos de todas las sucursales sin excepción
        List<SucursalProducto> allInventory = sucursalProductoRepository.findAll();
        return allInventory.stream()
                .map(sp -> entityMapper.toProductoResponse(sp.getProducto()))
                .distinct()
                .collect(Collectors.toList());
    }
}
