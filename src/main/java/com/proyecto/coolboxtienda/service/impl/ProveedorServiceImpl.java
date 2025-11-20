package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.ProveedorRequest;
import com.proyecto.coolboxtienda.dto.response.ProveedorResponse;
import com.proyecto.coolboxtienda.entity.Ciudad;
import com.proyecto.coolboxtienda.entity.Producto;
import com.proyecto.coolboxtienda.entity.ProductoProveedor;
import com.proyecto.coolboxtienda.entity.ProductoProveedorId;
import com.proyecto.coolboxtienda.entity.Proveedor;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.repository.CiudadRepository;
import com.proyecto.coolboxtienda.repository.ProductoProveedorRepository;
import com.proyecto.coolboxtienda.repository.ProductoRepository;
import com.proyecto.coolboxtienda.repository.ProveedorRepository;
import com.proyecto.coolboxtienda.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

        private final ProveedorRepository proveedorRepository;
        private final CiudadRepository ciudadRepository;
        private final ProductoRepository productoRepository;
        private final ProductoProveedorRepository productoProveedorRepository;
        private final EntityMapper entityMapper;

        @Override
        @Transactional
        public ProveedorResponse createProveedor(ProveedorRequest request) {
                Ciudad ciudad = ciudadRepository.findById(request.getIdCiudad())
                                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));

                Proveedor proveedor = entityMapper.toProveedorEntity(request, ciudad);
                proveedor = proveedorRepository.save(proveedor);

                return entityMapper.toProveedorResponse(proveedor);
        }

        @Override
        @Transactional
        public ProveedorResponse updateProveedor(Integer id, ProveedorRequest request) {
                Proveedor proveedor = proveedorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

                Ciudad ciudad = ciudadRepository.findById(request.getIdCiudad())
                                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));

                entityMapper.updateProveedorEntity(proveedor, request, ciudad);
                proveedor = proveedorRepository.save(proveedor);

                return entityMapper.toProveedorResponse(proveedor);
        }

        @Override
        @Transactional
        public void deleteProveedor(Integer id) {
                proveedorRepository.deleteById(id);
        }

        @Override
        @Transactional(readOnly = true)
        public ProveedorResponse getProveedorById(Integer id) {
                Proveedor proveedor = proveedorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
                return entityMapper.toProveedorResponse(proveedor);
        }

        @Override
        @Transactional(readOnly = true)
        public List<ProveedorResponse> getAllProveedores() {
                return proveedorRepository.findAll().stream()
                                .map(entityMapper::toProveedorResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<ProveedorResponse> getProveedoresByProducto(Integer idProducto) {
                List<ProductoProveedor> productoProveedores = productoProveedorRepository
                                .findByProducto_IdProducto(idProducto);
                return productoProveedores.stream()
                                .map(pp -> entityMapper.toProveedorResponse(pp.getProveedor()))
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void assignProductoToProveedor(Integer idProducto, Integer idProveedor, BigDecimal precioCompra,
                        Integer stockInicial) {
                Producto producto = productoRepository.findById(idProducto)
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                Proveedor proveedor = proveedorRepository.findById(idProveedor)
                                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

                ProductoProveedorId id = new ProductoProveedorId(idProducto, idProveedor);
                ProductoProveedor productoProveedor = new ProductoProveedor();
                // Set composite key fields directly (no setId() for @IdClass)
                productoProveedor.setProducto(producto);
                productoProveedor.setProveedor(proveedor);
                productoProveedor.setPrecioProducto(precioCompra);
                productoProveedor.setStockProducto(stockInicial);

                productoProveedorRepository.save(productoProveedor);
        }
}
