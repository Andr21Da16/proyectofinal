package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.CompraProveedorRequest;
import com.proyecto.coolboxtienda.dto.response.CompraProveedorResponse;
import com.proyecto.coolboxtienda.entity.*;
import com.proyecto.coolboxtienda.repository.*;
import com.proyecto.coolboxtienda.service.CompraProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraProveedorServiceImpl implements CompraProveedorService {

        private final CompraProveedorRepository compraProveedorRepository;
        private final CompraProveedorItemRepository compraProveedorItemRepository;
        private final ProveedorRepository proveedorRepository;
        private final ColaboradorRepository colaboradorRepository;
        private final SucursalRepository sucursalRepository;
        private final ProductoRepository productoRepository;
        private final SucursalProductoRepository sucursalProductoRepository;
        private final ProductoProveedorRepository productoProveedorRepository;
        private final MovimientoInventarioRepository movimientoInventarioRepository;

        @Override
        public List<CompraProveedorResponse> getAllCompras() {
                return compraProveedorRepository.findAll().stream()
                                .map(this::toCompraResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<CompraProveedorResponse> getComprasByFilters(Integer idProveedor, String estado,
                        Integer idSucursal) {
                return compraProveedorRepository.findByFilters(idProveedor, estado, idSucursal).stream()
                                .map(this::toCompraResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public CompraProveedorResponse getCompraById(Integer id) {
                CompraProveedor compra = compraProveedorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
                return toCompraResponse(compra);
        }

        @Override
        @Transactional
        public CompraProveedorResponse createCompra(CompraProveedorRequest request) {
                Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

                Colaborador colaborador = colaboradorRepository.findById(request.getIdColaborador())
                                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado"));

                Sucursal sucursal = sucursalRepository.findById(request.getIdSucursal())
                                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

                // Calcular total
                BigDecimal total = request.getItems().stream()
                                .map(item -> item.getPrecioUnitario()
                                                .multiply(BigDecimal.valueOf(item.getCantidadSolicitada())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                CompraProveedor compra = new CompraProveedor();
                compra.setProveedor(proveedor);
                compra.setColaborador(colaborador);
                compra.setSucursal(sucursal);
                compra.setTotal(total);
                compra.setEstado("COMPLETADA"); // Simulacion: Compra completada automaticamente
                compra.setObservaciones(request.getObservaciones());

                compra = compraProveedorRepository.save(compra);

                // Guardar items
                for (CompraProveedorRequest.CompraProveedorItemRequest itemReq : request.getItems()) {
                        Producto producto = productoRepository.findById(itemReq.getIdProducto())
                                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                        CompraProveedorItem item = new CompraProveedorItem();
                        item.setId(new CompraProveedorItemId(compra.getIdCompra(), producto.getIdProducto()));
                        item.setCompraProveedor(compra);
                        item.setProducto(producto);
                        item.setCantidadSolicitada(itemReq.getCantidadSolicitada());
                        item.setCantidadRecibida(0);
                        item.setPrecioUnitario(itemReq.getPrecioUnitario());
                        item.setSubtotal(itemReq.getPrecioUnitario()
                                        .multiply(BigDecimal.valueOf(itemReq.getCantidadSolicitada())));
                        compraProveedorItemRepository.save(item);

                        // Actualizar stock proveedor (restar)
                        ProductoProveedor productoProveedor = productoProveedorRepository
                                        .findById(new ProductoProveedorId(proveedor.getIdProveedor(),
                                                        producto.getIdProducto()))
                                        .orElseThrow(() -> new RuntimeException("Producto no asociado al proveedor"));

                        // Permitir stock negativo para simulación o validar si es necesario
                        productoProveedor.setStockProducto(
                                        productoProveedor.getStockProducto() - itemReq.getCantidadSolicitada());
                        productoProveedorRepository.save(productoProveedor);

                        // Actualizar stock sucursal (sumar)
                        SucursalProductoId sucursalProductoId = new SucursalProductoId(sucursal.getIdSucursal(),
                                        producto.getIdProducto(), proveedor.getIdProveedor());
                        SucursalProducto sucursalProducto = sucursalProductoRepository.findById(sucursalProductoId)
                                        .orElse(new SucursalProducto());

                        if (sucursalProducto.getSucursal() == null) {
                                sucursalProducto.setSucursal(sucursal);
                                sucursalProducto.setProducto(producto);
                                sucursalProducto.setProveedor(proveedor);
                                sucursalProducto.setStockProducto(0);
                                sucursalProducto.setPrecioProducto(productoProveedor.getPrecioProducto()); // Precio
                                                                                                           // base del
                                                                                                           // proveedor
                        }

                        sucursalProducto.setStockProducto(
                                        sucursalProducto.getStockProducto() + itemReq.getCantidadSolicitada());
                        sucursalProductoRepository.save(sucursalProducto);

                        // Registrar movimiento de inventario
                        MovimientoInventario movimiento = new MovimientoInventario();
                        movimiento.setProducto(producto);
                        movimiento.setSucursal(sucursal);
                        movimiento.setProveedor(proveedor);
                        movimiento.setTipoMovimiento("ENTRADA");
                        movimiento.setCantidad(itemReq.getCantidadSolicitada());
                        movimiento.setMotivo("Compra a proveedor #" + compra.getIdCompra());
                        // movimiento.setUsuario(...) // Si hay seguridad
                        movimientoInventarioRepository.save(movimiento);
                }

                return toCompraResponse(compra);
        }

        @Override
        @Transactional
        public CompraProveedorResponse actualizarEstado(Integer id, String nuevoEstado) {
                CompraProveedor compra = compraProveedorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
                compra.setEstado(nuevoEstado);
                compra = compraProveedorRepository.save(compra);
                return toCompraResponse(compra);
        }

        @Override
        @Transactional
        public CompraProveedorResponse registrarRecepcion(Integer id, Integer idProducto, Integer cantidadRecibida) {
                CompraProveedor compra = compraProveedorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

                CompraProveedorItem item = compraProveedorItemRepository
                                .findById(new CompraProveedorItemId(id, idProducto))
                                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

                item.setCantidadRecibida(item.getCantidadRecibida() + cantidadRecibida);
                compraProveedorItemRepository.save(item);

                // Verificar si todos los items están completos
                List<CompraProveedorItem> items = compraProveedorItemRepository.findByCompraProveedor_IdCompra(id);
                boolean todosCompletos = items.stream()
                                .allMatch(i -> i.getCantidadRecibida().equals(i.getCantidadSolicitada()));

                if (todosCompletos) {
                        compra.setEstado("RECEPCION_COMPLETA");
                } else {
                        compra.setEstado("RECEPCION_PARCIAL");
                }

                compra = compraProveedorRepository.save(compra);
                return toCompraResponse(compra);
        }

        @Override
        @Transactional
        public void cancelarCompra(Integer id) {
                CompraProveedor compra = compraProveedorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
                compra.setEstado("CANCELADA");
                compraProveedorRepository.save(compra);
        }

        private CompraProveedorResponse toCompraResponse(CompraProveedor compra) {
                List<CompraProveedorItem> items = compraProveedorItemRepository
                                .findByCompraProveedor_IdCompra(compra.getIdCompra());

                return CompraProveedorResponse.builder()
                                .idCompra(compra.getIdCompra())
                                .idProveedor(compra.getProveedor().getIdProveedor())
                                .nombreProveedor(compra.getProveedor().getNombreProveedor())
                                .fechaCompra(compra.getFechaCompra())
                                .total(compra.getTotal())
                                .estado(compra.getEstado())
                                .idColaborador(compra.getColaborador().getIdColaborador())
                                .nombreColaborador(compra.getColaborador().getNombreColaborador())
                                .idSucursal(compra.getSucursal().getIdSucursal())
                                .nombreSucursal(compra.getSucursal().getNombreSucursal())
                                .observaciones(compra.getObservaciones())
                                .items(items.stream().map(this::toItemResponse).collect(Collectors.toList()))
                                .build();
        }

        private CompraProveedorResponse.CompraProveedorItemResponse toItemResponse(CompraProveedorItem item) {
                return CompraProveedorResponse.CompraProveedorItemResponse.builder()
                                .idProducto(item.getProducto().getIdProducto())
                                .nombreProducto(item.getProducto().getNombreProducto())
                                .cantidadSolicitada(item.getCantidadSolicitada())
                                .cantidadRecibida(item.getCantidadRecibida())
                                .precioUnitario(item.getPrecioUnitario())
                                .subtotal(item.getSubtotal())
                                .build();
        }
}
