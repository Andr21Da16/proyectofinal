package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.InventarioRequest;
import com.proyecto.coolboxtienda.dto.request.TransferenciaRequest;
import com.proyecto.coolboxtienda.dto.response.SucursalProductoResponse;
import com.proyecto.coolboxtienda.entity.*;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.repository.*;
import com.proyecto.coolboxtienda.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

        private final SucursalProductoRepository sucursalProductoRepository;
        private final ProductoRepository productoRepository;
        private final SucursalRepository sucursalRepository;
        private final ProveedorRepository proveedorRepository;
        private final EntityMapper entityMapper;

        @Override
        @Transactional
        public void addStock(InventarioRequest request) {
                Producto producto = productoRepository.findById(request.getIdProducto())
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                Sucursal sucursal = sucursalRepository.findById(request.getIdSucursal())
                                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

                Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

                SucursalProductoId id = new SucursalProductoId(
                                request.getIdSucursal(), request.getIdProducto(), request.getIdProveedor());

                SucursalProducto sucursalProducto = sucursalProductoRepository.findById(id)
                                .orElse(new SucursalProducto());

                // Set composite key fields directly (no setId() for @IdClass)
                sucursalProducto.setSucursal(sucursal);
                sucursalProducto.setProducto(producto);
                sucursalProducto.setProveedor(proveedor);
                sucursalProducto.setStockProducto(
                                sucursalProducto.getStockProducto() != null
                                                ? sucursalProducto.getStockProducto() + request.getStockProducto()
                                                : request.getStockProducto());
                sucursalProducto.setPrecioProducto(request.getPrecioProducto());

                sucursalProductoRepository.save(sucursalProducto);
        }

        @Override
        @Transactional
        public void updateStock(InventarioRequest request) {
                SucursalProductoId id = new SucursalProductoId(
                                request.getIdSucursal(), request.getIdProducto(), request.getIdProveedor());
                SucursalProducto sucursalProducto = sucursalProductoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

                sucursalProducto.setStockProducto(request.getStockProducto());
                sucursalProducto.setPrecioProducto(request.getPrecioProducto());
                sucursalProductoRepository.save(sucursalProducto);
        }

        @Override
        @Transactional
        public void transferStock(TransferenciaRequest request) {
                // Buscar inventario origen con el proveedor específico
                SucursalProductoId origenId = new SucursalProductoId(
                                request.getIdSucursalOrigen(), request.getIdProducto(), request.getIdProveedor());

                SucursalProducto origen = sucursalProductoRepository.findById(origenId)
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado en sucursal origen"));

                if (origen.getStockProducto() < request.getCantidad()) {
                        throw new RuntimeException("Stock insuficiente en sucursal origen");
                }

                // Reducir stock origen
                origen.setStockProducto(origen.getStockProducto() - request.getCantidad());
                sucursalProductoRepository.save(origen);

                // Aumentar stock destino
                SucursalProductoId destinoId = new SucursalProductoId(
                                request.getIdSucursalDestino(), request.getIdProducto(), request.getIdProveedor());

                SucursalProducto destino = sucursalProductoRepository.findById(destinoId)
                                .orElseGet(() -> {
                                        SucursalProducto nuevo = new SucursalProducto();
                                        // Set composite key fields directly (no setId() for @IdClass)
                                        nuevo.setSucursal(sucursalRepository.findById(request.getIdSucursalDestino())
                                                        .orElseThrow(() -> new RuntimeException(
                                                                        "Sucursal destino no encontrada")));
                                        nuevo.setProducto(origen.getProducto());
                                        nuevo.setProveedor(origen.getProveedor());
                                        nuevo.setStockProducto(0);
                                        nuevo.setPrecioProducto(origen.getPrecioProducto());
                                        return nuevo;
                                });

                destino.setStockProducto(destino.getStockProducto() + request.getCantidad());
                sucursalProductoRepository.save(destino);
        }

        @Override
        @Transactional(readOnly = true)
        public List<SucursalProductoResponse> getInventoryBySucursal(Integer idSucursal) {
                List<SucursalProducto> inventario = sucursalProductoRepository.findBySucursal_IdSucursal(idSucursal);
                return inventario.stream()
                                .map(this::toSucursalProductoResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<SucursalProductoResponse> getInventoryByProducto(Integer idProducto) {
                List<SucursalProducto> inventario = sucursalProductoRepository.findByProducto_IdProducto(idProducto);
                return inventario.stream()
                                .map(this::toSucursalProductoResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<SucursalProductoResponse> getLowStockProducts(Integer threshold) {
                List<SucursalProducto> lowStock = sucursalProductoRepository.findByStockProductoLessThan(threshold);
                return lowStock.stream()
                                .map(this::toSucursalProductoResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public boolean checkAvailability(Integer idProducto, Integer idSucursal, Integer idProveedor,
                        Integer cantidad) {
                SucursalProductoId id = new SucursalProductoId(idSucursal, idProducto, idProveedor);
                return sucursalProductoRepository.findById(id)
                                .map(sp -> sp.getStockProducto() >= cantidad)
                                .orElse(false);
        }

        private SucursalProductoResponse toSucursalProductoResponse(SucursalProducto sp) {
                return SucursalProductoResponse.builder()
                                .idSucursal(sp.getSucursal().getIdSucursal())
                                .nombreSucursal(sp.getSucursal().getNombreSucursal())
                                .direccionSucursal(sp.getSucursal().getDireccionSucursal())
                                .idProducto(sp.getProducto().getIdProducto())
                                .nombreProducto(sp.getProducto().getNombreProducto())
                                .descripcionProducto(sp.getProducto().getEspecificacionesProducto())
                                .idProveedor(sp.getProveedor().getIdProveedor())
                                .nombreProveedor(sp.getProveedor().getNombreProveedor())
                                .stockProducto(sp.getStockProducto())
                                .precioProducto(sp.getPrecioProducto())
                                .build();
        }
}
