package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.CarritoItemRequest;
import com.proyecto.coolboxtienda.dto.response.CarritoFullResponse;
import com.proyecto.coolboxtienda.dto.response.CarritoResponse;
import com.proyecto.coolboxtienda.entity.*;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.mapper.ResponseMapper;
import com.proyecto.coolboxtienda.repository.*;
import com.proyecto.coolboxtienda.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoComprasRepository carritoRepository;
    private final CarritoDetalleRepository carritoDetalleRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final SucursalRepository sucursalRepository;
    private final SucursalProductoRepository sucursalProductoRepository;
    private final EntityMapper entityMapper;
    private final ResponseMapper responseMapper;

    @Override
    @Transactional
    public CarritoResponse getOrCreateCarrito(Integer idColaborador) {
        Colaborador colaborador = colaboradorRepository.findById(idColaborador)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

        CarritoCompras carrito = carritoRepository.findByColaborador_IdColaborador(idColaborador)
                .orElseGet(() -> {
                    CarritoCompras nuevoCarrito = new CarritoCompras();
                    nuevoCarrito.setColaborador(colaborador);
                    return carritoRepository.save(nuevoCarrito);
                });

        Integer cantidadItems = carritoDetalleRepository.countByCarrito_IdCarrito(carrito.getIdCarrito());
        BigDecimal total = carritoDetalleRepository.calculateCartTotal(carrito.getIdCarrito());

        return entityMapper.toCarritoResponse(carrito, cantidadItems, total != null ? total : BigDecimal.ZERO);
    }

    @Override
    @Transactional
    public CarritoFullResponse addItem(Integer idColaborador, CarritoItemRequest request) {
        CarritoCompras carrito = carritoRepository.findByColaborador_IdColaborador(idColaborador)
                .orElseGet(() -> {
                    Colaborador colaborador = colaboradorRepository.findById(idColaborador)
                            .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));
                    CarritoCompras nuevoCarrito = new CarritoCompras();
                    nuevoCarrito.setColaborador(colaborador);
                    return carritoRepository.save(nuevoCarrito);
                });

        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Sucursal sucursal = sucursalRepository.findById(request.getIdSucursal())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        // Verificar stock
        SucursalProducto sucursalProducto = sucursalProductoRepository
                .findBySucursal_IdSucursalAndProducto_IdProductoAndProveedor_IdProveedor(
                        request.getIdSucursal(), request.getIdProducto(), request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Producto no disponible en esta sucursal"));

        if (sucursalProducto.getStockProducto() < request.getCantidad()) {
            throw new RuntimeException("Stock insuficiente");
        }

        // Verificar si ya existe el item
        CarritoDetalleId detalleId = new CarritoDetalleId(
                carrito.getIdCarrito(), producto.getIdProducto(), proveedor.getIdProveedor(), sucursal.getIdSucursal());

        CarritoDetalle detalle = carritoDetalleRepository.findById(detalleId)
                .orElse(new CarritoDetalle());

        // Set composite key fields directly (no setId() for @IdClass)
        detalle.setCarrito(carrito);
        detalle.setProducto(producto);
        detalle.setProveedor(proveedor);
        detalle.setSucursal(sucursal);
        detalle.setCantidad(
                detalle.getCantidad() != null ? detalle.getCantidad() + request.getCantidad() : request.getCantidad());
        detalle.setPrecioUnitario(sucursalProducto.getPrecioProducto());

        carritoDetalleRepository.save(detalle);

        return getCarritoFull(idColaborador);
    }

    @Override
    @Transactional
    public CarritoFullResponse updateItemQuantity(Integer idColaborador, Integer idProducto, Integer idProveedor,
            Integer idSucursal, Integer cantidad) {
        CarritoCompras carrito = carritoRepository.findByColaborador_IdColaborador(idColaborador)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CarritoDetalleId detalleId = new CarritoDetalleId(carrito.getIdCarrito(), idProducto, idProveedor, idSucursal);
        CarritoDetalle detalle = carritoDetalleRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));

        // Verificar stock
        SucursalProducto sucursalProducto = sucursalProductoRepository
                .findBySucursal_IdSucursalAndProducto_IdProductoAndProveedor_IdProveedor(idSucursal, idProducto,
                        idProveedor)
                .orElseThrow(() -> new RuntimeException("Producto no disponible"));

        if (sucursalProducto.getStockProducto() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        detalle.setCantidad(cantidad);
        carritoDetalleRepository.save(detalle);

        return getCarritoFull(idColaborador);
    }

    @Override
    @Transactional
    public CarritoFullResponse removeItem(Integer idColaborador, Integer idProducto, Integer idProveedor,
            Integer idSucursal) {
        CarritoCompras carrito = carritoRepository.findByColaborador_IdColaborador(idColaborador)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CarritoDetalleId detalleId = new CarritoDetalleId(carrito.getIdCarrito(), idProducto, idProveedor, idSucursal);
        carritoDetalleRepository.deleteById(detalleId);

        return getCarritoFull(idColaborador);
    }

    @Override
    @Transactional
    public void clearCarrito(Integer idColaborador) {
        CarritoCompras carrito = carritoRepository.findByColaborador_IdColaborador(idColaborador)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        carritoDetalleRepository.deleteByCarrito_IdCarrito(carrito.getIdCarrito());
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoFullResponse getCarritoFull(Integer idColaborador) {
        CarritoCompras carrito = carritoRepository.findByColaborador_IdColaborador(idColaborador)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        List<CarritoDetalle> detalles = carritoDetalleRepository.findByCarrito_IdCarrito(carrito.getIdCarrito());
        BigDecimal total = carritoDetalleRepository.calculateCartTotal(carrito.getIdCarrito());

        return responseMapper.toCarritoFullResponse(carrito, detalles, total != null ? total : BigDecimal.ZERO);
    }
}
