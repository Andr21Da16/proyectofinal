package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.VentaRequest;
import com.proyecto.coolboxtienda.dto.response.PageResponse;
import com.proyecto.coolboxtienda.dto.response.VentaResponse;
import com.proyecto.coolboxtienda.entity.*;
import com.proyecto.coolboxtienda.mapper.EntityMapper;
import com.proyecto.coolboxtienda.mapper.ResponseMapper;
import com.proyecto.coolboxtienda.repository.*;
import com.proyecto.coolboxtienda.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final CarritoComprasRepository carritoRepository;
    private final CarritoDetalleRepository carritoDetalleRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final SucursalRepository sucursalRepository;
    private final EstadoVentaRepository estadoVentaRepository;
    private final SucursalProductoRepository sucursalProductoRepository;
    private final EntityMapper entityMapper;
    private final ResponseMapper responseMapper;

    @Override
    @Transactional
    public VentaResponse createVenta(VentaRequest request) {
        // Obtener carrito
        CarritoCompras carrito = carritoRepository.findById(request.getIdCarrito())
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Obtener detalles del carrito
        List<CarritoDetalle> detallesCarrito = carritoDetalleRepository.findByCarrito_IdCarrito(carrito.getIdCarrito());
        if (detallesCarrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Obtener colaborador y sucursal
        Colaborador colaborador = colaboradorRepository.findById(request.getIdColaborador())
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

        Sucursal sucursal = sucursalRepository.findById(request.getIdSucursal())
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        // Obtener estado inicial
        EstadoVenta estadoVenta = estadoVentaRepository.findById(1) // Pendiente
                .orElseThrow(() -> new RuntimeException("Estado de venta no encontrado"));

        // Calcular total
        BigDecimal total = BigDecimal.ZERO;
        for (CarritoDetalle detalle : detallesCarrito) {
            BigDecimal subtotal = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);
        }

        // Crear venta
        Venta venta = new Venta();
        venta.setCarrito(carrito);
        venta.setColaborador(colaborador);
        venta.setSucursal(sucursal);
        venta.setFechaVenta(LocalDateTime.now());
        venta.setTotal(total);
        venta.setMetodoPago(request.getMetodoPago());
        venta.setEstadoVenta(estadoVenta);
        venta.setDescuentoAplicado(BigDecimal.ZERO);
        venta = ventaRepository.save(venta);

        // Crear detalles de venta y actualizar stock
        for (CarritoDetalle carritoDetalle : detallesCarrito) {
            // Verificar y actualizar stock
            SucursalProducto sucursalProducto = sucursalProductoRepository
                    .findBySucursal_IdSucursalAndProducto_IdProductoAndProveedor_IdProveedor(
                            carritoDetalle.getSucursal().getIdSucursal(),
                            carritoDetalle.getProducto().getIdProducto(),
                            carritoDetalle.getProveedor().getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Producto no disponible"));

            if (sucursalProducto.getStockProducto() < carritoDetalle.getCantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para: " + carritoDetalle.getProducto().getNombreProducto());
            }

            // Reducir stock
            sucursalProducto.setStockProducto(sucursalProducto.getStockProducto() - carritoDetalle.getCantidad());
            sucursalProductoRepository.save(sucursalProducto);

            // Crear detalle de venta (no usar setId() para @IdClass)
            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setVenta(venta);
            detalleVenta.setProducto(carritoDetalle.getProducto());
            detalleVenta.setProveedor(carritoDetalle.getProveedor());
            detalleVenta.setSucursal(carritoDetalle.getSucursal());
            detalleVenta.setCantidad(carritoDetalle.getCantidad());
            detalleVenta.setPrecioUnitario(carritoDetalle.getPrecioUnitario());
            detalleVenta.setDescuento(BigDecimal.ZERO);
            detalleVentaRepository.save(detalleVenta);
        }

        // Vaciar carrito
        carritoDetalleRepository.deleteByCarrito_IdCarrito(carrito.getIdCarrito());

        // Obtener detalles para respuesta
        List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(venta.getIdVenta());
        return entityMapper.toVentaResponse(venta, detalles);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse getVentaById(Integer id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(id);
        return entityMapper.toVentaResponse(venta, detalles);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<VentaResponse> getVentasByColaborador(Integer idColaborador, Pageable pageable) {
        Page<Venta> page = ventaRepository.findByColaborador_IdColaborador(idColaborador, pageable);
        Page<VentaResponse> responsePage = page.map(venta -> {
            List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(venta.getIdVenta());
            return entityMapper.toVentaResponse(venta, detalles);
        });
        return responseMapper.toPageResponse(responsePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<VentaResponse> getVentasBySucursal(Integer idSucursal, Pageable pageable) {
        Page<Venta> page = ventaRepository.findBySucursal_IdSucursal(idSucursal, pageable);
        Page<VentaResponse> responsePage = page.map(venta -> {
            List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(venta.getIdVenta());
            return entityMapper.toVentaResponse(venta, detalles);
        });
        return responseMapper.toPageResponse(responsePage);
    }

    @Override
    @Transactional
    public VentaResponse updateEstadoVenta(Integer idVenta, Integer idEstado) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        EstadoVenta estadoVenta = estadoVentaRepository.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        venta.setEstadoVenta(estadoVenta);
        venta = ventaRepository.save(venta);

        List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(idVenta);
        return entityMapper.toVentaResponse(venta, detalles);
    }

    @Override
    @Transactional
    public void cancelVenta(Integer idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        // Verificar que se pueda cancelar
        if (venta.getEstadoVenta().getIdEstadoVenta() == 3) { // Completada
            throw new RuntimeException("No se puede cancelar una venta completada");
        }

        // Devolver stock
        List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(idVenta);
        for (DetalleVenta detalle : detalles) {
            SucursalProducto sucursalProducto = sucursalProductoRepository
                    .findBySucursal_IdSucursalAndProducto_IdProductoAndProveedor_IdProveedor(
                            venta.getSucursal().getIdSucursal(),
                            detalle.getProducto().getIdProducto(),
                            detalle.getProveedor().getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            sucursalProducto.setStockProducto(sucursalProducto.getStockProducto() + detalle.getCantidad());
            sucursalProductoRepository.save(sucursalProducto);
        }

        // Actualizar estado a cancelada
        EstadoVenta estadoCancelada = estadoVentaRepository.findById(4) // Cancelada
                .orElseThrow(() -> new RuntimeException("Estado cancelada no encontrado"));
        venta.setEstadoVenta(estadoCancelada);
        ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<VentaResponse> getVentasByFiltros(LocalDateTime fechaInicio, LocalDateTime fechaFin,
            Integer idSucursal, Integer idColaborador,
            Integer idEstado, Pageable pageable) {
        Page<Venta> page = ventaRepository.findVentasConFiltros(fechaInicio, fechaFin, idSucursal, idColaborador,
                idEstado, pageable);
        Page<VentaResponse> responsePage = page.map(venta -> {
            List<DetalleVenta> detalles = detalleVentaRepository.findByVenta_IdVenta(venta.getIdVenta());
            return entityMapper.toVentaResponse(venta, detalles);
        });
        return responseMapper.toPageResponse(responsePage);
    }
}
