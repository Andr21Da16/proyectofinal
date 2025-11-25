package com.proyecto.coolboxtienda.service.impl;

import com.proyecto.coolboxtienda.dto.request.PedidoRequest;
import com.proyecto.coolboxtienda.dto.response.PedidoResponse;
import com.proyecto.coolboxtienda.entity.*;
import com.proyecto.coolboxtienda.repository.*;
import com.proyecto.coolboxtienda.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

        private final PedidoRepository pedidoRepository;
        private final PedidoItemRepository pedidoItemRepository;
        private final ClienteRepository clienteRepository;
        private final ProductoRepository productoRepository;
        private final SucursalRepository sucursalRepository;
        private final CuponRepository cuponRepository;

        @Override
        public List<PedidoResponse> getAllPedidos() {
                return pedidoRepository.findAll().stream()
                                .map(this::toPedidoResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<PedidoResponse> getPedidosByFilters(Integer idCliente, String estadoLogistico, String estadoPago,
                        Integer idSucursal) {
                return pedidoRepository.findByFilters(idCliente, estadoLogistico, estadoPago, idSucursal).stream()
                                .map(this::toPedidoResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public PedidoResponse getPedidoById(Integer id) {
                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
                return toPedidoResponse(pedido);
        }

        @Override
        @Transactional
        public PedidoResponse createPedido(PedidoRequest request) {
                Cliente cliente = clienteRepository.findById(request.getIdCliente())
                                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

                // Calcular total
                BigDecimal total = request.getItems().stream()
                                .map(item -> item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                Pedido pedido = new Pedido();
                pedido.setCliente(cliente);
                pedido.setTotal(total);
                pedido.setMetodoPago(request.getMetodoPago());
                pedido.setDireccionEntrega(request.getDireccionEntrega());
                pedido.setObservaciones(request.getObservaciones());

                if (request.getIdSucursal() != null) {
                        Sucursal sucursal = sucursalRepository.findById(request.getIdSucursal())
                                        .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));
                        pedido.setSucursal(sucursal);
                }

                if (request.getIdCupon() != null) {
                        Cupon cupon = cuponRepository.findById(request.getIdCupon())
                                        .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
                        pedido.setCupon(cupon);
                        // Aplicar descuento (simplificado)
                        BigDecimal descuento = cupon.getDescuentoPorcentaje() != null
                                        ? total.multiply(cupon.getDescuentoPorcentaje()).divide(BigDecimal.valueOf(100))
                                        : cupon.getDescuentoMonto();
                        pedido.setDescuentoAplicado(descuento);
                        pedido.setTotal(total.subtract(descuento));
                }

                pedido = pedidoRepository.save(pedido);

                // Guardar items
                for (PedidoRequest.PedidoItemRequest itemReq : request.getItems()) {
                        Producto producto = productoRepository.findById(itemReq.getIdProducto())
                                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                        PedidoItem item = new PedidoItem();
                        item.setId(new PedidoItemId(pedido.getIdPedido(), producto.getIdProducto()));
                        item.setPedido(pedido);
                        item.setProducto(producto);
                        item.setCantidad(itemReq.getCantidad());
                        item.setPrecioUnitario(itemReq.getPrecioUnitario());
                        item.setSubtotal(itemReq.getPrecioUnitario()
                                        .multiply(BigDecimal.valueOf(itemReq.getCantidad())));
                        pedidoItemRepository.save(item);
                }

                return toPedidoResponse(pedido);
        }

        @Override
        @Transactional
        public PedidoResponse actualizarEstadoLogistico(Integer id, String nuevoEstado) {
                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
                pedido.setEstadoLogistico(nuevoEstado);
                pedido = pedidoRepository.save(pedido);
                return toPedidoResponse(pedido);
        }

        @Override
        @Transactional
        public PedidoResponse actualizarEstadoPago(Integer id, String nuevoEstado) {
                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
                pedido.setEstadoPago(nuevoEstado);
                pedido = pedidoRepository.save(pedido);
                return toPedidoResponse(pedido);
        }

        @Override
        @Transactional
        public PedidoResponse entregarPedido(Integer id) {
                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
                pedido.setEstadoLogistico("ENTREGADO");
                pedido.setFechaEntrega(LocalDateTime.now());
                // TODO: Generar venta automáticamente
                pedido = pedidoRepository.save(pedido);
                return toPedidoResponse(pedido);
        }

        @Override
        public List<PedidoResponse> getPedidosParaAlmacen() {
                return pedidoRepository.findPedidosParaAlmacen().stream()
                                .map(this::toPedidoResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<PedidoResponse> getPedidosEnTransito() {
                return pedidoRepository.findPedidosEnTransito().stream()
                                .map(this::toPedidoResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void cancelarPedido(Integer id) {
                Pedido pedido = pedidoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
                pedido.setEstadoLogistico("CANCELADO");
                pedidoRepository.save(pedido);
        }

        private PedidoResponse toPedidoResponse(Pedido pedido) {
                List<PedidoItem> items = pedidoItemRepository.findByPedido_IdPedido(pedido.getIdPedido());

                return PedidoResponse.builder()
                                .idPedido(pedido.getIdPedido())
                                .idCliente(pedido.getCliente().getIdCliente())
                                .nombreCliente(pedido.getCliente().getNombreCompleto())
                                .fechaPedido(pedido.getFechaPedido())
                                .total(pedido.getTotal())
                                .descuentoAplicado(pedido.getDescuentoAplicado())
                                .estadoLogistico(pedido.getEstadoLogistico())
                                .estadoPago(pedido.getEstadoPago())
                                .metodoPago(pedido.getMetodoPago())
                                .direccionEntrega(pedido.getDireccionEntrega())
                                .idSucursal(pedido.getSucursal() != null ? pedido.getSucursal().getIdSucursal() : null)
                                .nombreSucursal(pedido.getSucursal() != null ? pedido.getSucursal().getNombreSucursal()
                                                : null)
                                .codigoCupon(pedido.getCupon() != null ? pedido.getCupon().getCodigo() : null)
                                .fechaEntrega(pedido.getFechaEntrega())
                                .observaciones(pedido.getObservaciones())
                                .idVentaGenerada(pedido.getIdVentaGenerada())
                                .items(items.stream().map(this::toItemResponse).collect(Collectors.toList()))
                                .build();
        }

        private PedidoResponse.PedidoItemResponse toItemResponse(PedidoItem item) {
                return PedidoResponse.PedidoItemResponse.builder()
                                .idProducto(item.getProducto().getIdProducto())
                                .nombreProducto(item.getProducto().getNombreProducto())
                                .cantidad(item.getCantidad())
                                .precioUnitario(item.getPrecioUnitario())
                                .subtotal(item.getSubtotal())
                                .build();
        }
}
