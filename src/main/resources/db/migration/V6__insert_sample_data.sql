-- Sample Data for Backend Expansion
-- This file contains sample data for all new tables

-- Update existing roles with tipoAcceso
UPDATE roles SET tipo_acceso = 'ERP', activo = true WHERE id_rol = 1;
UPDATE roles SET tipo_acceso = 'ERP,WEB', activo = true WHERE id_rol = 2;

-- Insert sample rol_permiso data
INSERT INTO rol_permiso (id_rol, nombre_modulo, puede_ver, puede_editar, puede_crear, puede_eliminar) VALUES
(1, 'PRODUCTOS', true, true, true, true),
(1, 'VENTAS', true, true, true, false),
(1, 'INVENTARIO', true, true, true, false),
(1, 'CLIENTES', true, true, true, true),
(1, 'PEDIDOS', true, true, true, false),
(1, 'COMPRAS', true, true, true, true),
(1, 'CAMPANIAS', true, true, true, true),
(1, 'CUPONES', true, true, true, true),
(1, 'GARANTIAS', true, true, true, false),
(1, 'REPORTES', true, false, false, false),
(2, 'PRODUCTOS', true, true, false, false),
(2, 'VENTAS', true, false, false, false),
(2, 'CLIENTES', true, true, true, false),
(2, 'PEDIDOS', true, true, false, false);

-- Insert sample clientes
INSERT INTO clientes (nombre_completo, email, telefono, direccion, tipo_documento, numero_documento, id_ciudad, activo) VALUES
('Juan Pérez García', 'juan.perez@email.com', '987654321', 'Av. Principal 123, Lima', 'DNI', '12345678', 1, true),
('María López Rodríguez', 'maria.lopez@email.com', '987654322', 'Jr. Los Olivos 456, Lima', 'DNI', '23456789', 1, true),
('Carlos Sánchez Torres', 'carlos.sanchez@email.com', '987654323', 'Calle Las Flores 789, Arequipa', 'DNI', '34567890', 2, true),
('Ana Martínez Díaz', 'ana.martinez@email.com', '987654324', 'Av. Grau 321, Cusco', 'DNI', '45678901', 3, true),
('Luis Fernández Castro', 'luis.fernandez@email.com', '987654325', 'Jr. Comercio 654, Trujillo', 'DNI', '56789012', 4, true),
('Carmen Ruiz Vargas', 'carmen.ruiz@email.com', '987654326', 'Av. Arequipa 987, Lima', 'DNI', '67890123', 1, true),
('Roberto Flores Mendoza', 'roberto.flores@email.com', '987654327', 'Calle Real 147, Chiclayo', 'DNI', '78901234', 5, true),
('Patricia Vega Romero', 'patricia.vega@email.com', '987654328', 'Jr. Unión 258, Piura', 'DNI', '89012345', 6, true),
('Miguel Ángel Torres', 'miguel.torres@email.com', '987654329', 'Av. Bolognesi 369, Ica', 'DNI', '90123456', 7, true),
('Sofía Ramírez Gutiérrez', 'sofia.ramirez@email.com', '987654330', 'Calle Lima 741, Huancayo', 'DNI', '01234567', 8, true),
('Empresa Tech SAC', 'ventas@techsac.com', '987654331', 'Av. Javier Prado 1500, Lima', 'RUC', '20123456789', 1, true),
('Comercial del Sur EIRL', 'contacto@comsur.com', '987654332', 'Av. Ejército 890, Arequipa', 'RUC', '20234567890', 2, true);

-- Insert sample cupones
INSERT INTO cupones (codigo, descripcion, descuento_porcentaje, descuento_monto, fecha_inicio, fecha_fin, uso_maximo, uso_actual, activo, monto_minimo) VALUES
('BIENVENIDA10', 'Descuento de bienvenida 10%', 10.00, NULL, '2025-01-01', '2025-12-31', 100, 5, true, 50.00),
('VERANO2025', 'Promoción de verano', 15.00, NULL, '2025-01-01', '2025-03-31', 200, 12, true, 100.00),
('NAVIDAD50', 'Descuento navideño S/50', NULL, 50.00, '2024-12-01', '2024-12-31', 50, 45, false, 200.00),
('CYBER30', 'CyberDay 30% descuento', 30.00, NULL, '2025-02-01', '2025-02-28', 500, 234, true, 150.00),
('PRIMERACOMPRA', 'Primera compra 20%', 20.00, NULL, '2025-01-01', '2025-12-31', 1000, 89, true, 0.00),
('FIDELIDAD25', 'Cliente frecuente 25%', 25.00, NULL, '2025-01-01', '2025-06-30', 300, 67, true, 300.00),
('FLASH100', 'Venta flash S/100', NULL, 100.00, '2025-03-01', '2025-03-15', 100, 23, true, 500.00),
('ANIVERSARIO', 'Aniversario tienda 35%', 35.00, NULL, '2025-04-01', '2025-04-30', 200, 0, true, 200.00),
('ESTUDIANTE15', 'Descuento estudiante', 15.00, NULL, '2025-01-01', '2025-12-31', 500, 145, true, 80.00),
('MAYORISTA40', 'Compra mayorista 40%', 40.00, NULL, '2025-01-01', '2025-12-31', 50, 12, true, 1000.00);

-- Insert sample pedidos (assuming cliente ids 1-12 exist)
INSERT INTO pedidos (id_cliente, total, descuento_aplicado, estado_logistico, estado_pago, metodo_pago, direccion_entrega, id_sucursal, id_cupon, observaciones) VALUES
(1, 450.00, 45.00, 'ENTREGADO', 'PAGADO', 'TARJETA', 'Av. Principal 123, Lima', 1, 1, 'Entrega exitosa'),
(2, 780.50, 117.08, 'EN_TRANSITO', 'PAGADO', 'TRANSFERENCIA', 'Jr. Los Olivos 456, Lima', 1, 2, 'En camino'),
(3, 1200.00, 0.00, 'EN_PREPARACION', 'CONTRAENTREGA', 'CONTRAENTREGA', 'Calle Las Flores 789, Arequipa', 2, NULL, 'Preparando pedido'),
(4, 350.00, 70.00, 'PENDIENTE', 'PENDIENTE', 'TARJETA', 'Av. Grau 321, Cusco', 3, 5, 'Esperando confirmación'),
(5, 920.00, 276.00, 'ENTREGADO', 'PAGADO', 'YAPE', 'Jr. Comercio 654, Trujillo', 1, 4, 'Cliente satisfecho'),
(6, 560.00, 140.00, 'EN_TRANSITO', 'PAGADO', 'TARJETA', 'Av. Arequipa 987, Lima', 1, 6, NULL),
(7, 1500.00, 100.00, 'ENTREGADO', 'PAGADO', 'TRANSFERENCIA', 'Calle Real 147, Chiclayo', 2, 7, 'Compra corporativa'),
(8, 420.00, 0.00, 'CANCELADO', 'DEVUELTO', 'TARJETA', 'Jr. Unión 258, Piura', 1, NULL, 'Cliente canceló'),
(9, 680.00, 102.00, 'EN_PREPARACION', 'PAGADO', 'PLIN', 'Av. Bolognesi 369, Ica', 3, 9, NULL),
(10, 2100.00, 840.00, 'PENDIENTE', 'PENDIENTE', 'TRANSFERENCIA', 'Calle Lima 741, Huancayo', 1, 10, 'Pedido grande'),
(11, 850.00, 85.00, 'ENTREGADO', 'PAGADO', 'TARJETA', 'Av. Javier Prado 1500, Lima', 1, 1, 'Empresa'),
(12, 1350.00, 202.50, 'EN_TRANSITO', 'PAGADO', 'TRANSFERENCIA', 'Av. Ejército 890, Arequipa', 2, 2, NULL);

-- Insert sample pedido_items (assuming producto ids 1-10 exist)
INSERT INTO pedido_items (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 2, 225.00, 450.00),
(2, 2, 1, 780.50, 780.50),
(3, 3, 3, 400.00, 1200.00),
(4, 1, 1, 225.00, 225.00),
(4, 4, 1, 125.00, 125.00),
(5, 5, 2, 460.00, 920.00),
(6, 6, 4, 140.00, 560.00),
(7, 7, 5, 300.00, 1500.00),
(8, 8, 3, 140.00, 420.00),
(9, 9, 2, 340.00, 680.00),
(10, 10, 7, 300.00, 2100.00),
(11, 1, 2, 225.00, 450.00),
(11, 2, 1, 400.00, 400.00),
(12, 3, 3, 450.00, 1350.00);

-- Insert sample compras_proveedor (assuming proveedor ids 1-3 exist)
INSERT INTO compras_proveedor (id_proveedor, total, estado, id_colaborador, id_sucursal, observaciones) VALUES
(1, 15000.00, 'RECEPCION_COMPLETA', 1, 1, 'Compra mensual de productos electrónicos'),
(2, 8500.00, 'APROBADA', 1, 1, 'Pedido de accesorios'),
(1, 22000.00, 'RECEPCION_PARCIAL', 1, 2, 'Compra grande, falta mercancía'),
(3, 5600.00, 'REGISTRADA', 1, 1, 'Esperando aprobación'),
(2, 12000.00, 'RECEPCION_COMPLETA', 1, 3, 'Compra completada'),
(1, 18500.00, 'APROBADA', 1, 1, 'En proceso de recepción'),
(3, 9200.00, 'CANCELADA', 1, 2, 'Proveedor no pudo cumplir'),
(2, 14000.00, 'RECEPCION_COMPLETA', 1, 1, 'Todo recibido correctamente'),
(1, 25000.00, 'RECEPCION_PARCIAL', 1, 3, 'Falta 30% de mercancía'),
(3, 7800.00, 'APROBADA', 1, 1, 'Pedido urgente');

-- Insert sample compra_proveedor_items
INSERT INTO compra_proveedor_items (id_compra, id_producto, cantidad_solicitada, cantidad_recibida, precio_unitario, subtotal) VALUES
(1, 1, 100, 100, 150.00, 15000.00),
(2, 2, 50, 0, 170.00, 8500.00),
(3, 3, 80, 50, 275.00, 22000.00),
(4, 4, 70, 0, 80.00, 5600.00),
(5, 5, 40, 40, 300.00, 12000.00),
(6, 6, 150, 0, 123.33, 18500.00),
(7, 7, 40, 0, 230.00, 9200.00),
(8, 8, 100, 100, 140.00, 14000.00),
(9, 9, 100, 70, 250.00, 25000.00),
(10, 10, 30, 0, 260.00, 7800.00);

-- Insert sample campanias_marketing
INSERT INTO campanias_marketing (nombre_campania, descripcion, fecha_inicio, fecha_fin, descuento_porcentaje, activo, tipo_descuento) VALUES
('Campaña Verano 2025', 'Descuentos especiales para la temporada de verano', '2025-01-15', '2025-03-31', 20.00, true, 'PORCENTAJE'),
('Black Friday Tech', 'Ofertas increíbles en tecnología', '2025-11-25', '2025-11-30', 40.00, false, 'PORCENTAJE'),
('Regreso a Clases', 'Productos escolares y tecnológicos', '2025-02-15', '2025-03-15', 15.00, true, 'PORCENTAJE'),
('Cyber Monday', 'Descuentos online exclusivos', '2025-11-28', '2025-11-29', 35.00, false, 'PORCENTAJE'),
('Aniversario Coolbox', 'Celebramos nuestro aniversario', '2025-04-01', '2025-04-30', 25.00, true, 'PORCENTAJE'),
('Día de la Madre', 'Regalos especiales para mamá', '2025-05-01', '2025-05-15', 18.00, false, 'PORCENTAJE'),
('Fiestas Patrias', 'Celebra con tecnología', '2025-07-20', '2025-07-31', 22.00, false, 'PORCENTAJE'),
('Halloween Tech', 'Ofertas de miedo', '2025-10-25', '2025-10-31', 30.00, false, 'PORCENTAJE'),
('Navidad 2025', 'Los mejores regalos navideños', '2025-12-01', '2025-12-25', 28.00, false, 'PORCENTAJE'),
('Año Nuevo Digital', 'Empieza el año con tecnología', '2025-12-26', '2026-01-05', 20.00, false, 'PORCENTAJE');

-- Insert sample campania_productos (assuming producto ids 1-10 exist)
INSERT INTO campania_productos (id_campania, id_producto) VALUES
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 5), (2, 6), (2, 7),
(3, 1), (3, 8), (3, 9),
(4, 2), (4, 5), (4, 10),
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5),
(6, 6), (6, 7), (6, 8),
(7, 1), (7, 3), (7, 5), (7, 7),
(8, 9), (8, 10),
(9, 1), (9, 2), (9, 3), (9, 4), (9, 5), (9, 6),
(10, 7), (10, 8), (10, 9), (10, 10);

-- Insert sample cupon_usos
INSERT INTO cupon_usos (id_cupon, id_venta, id_pedido, monto_descuento) VALUES
(1, NULL, 1, 45.00),
(2, NULL, 2, 117.08),
(5, NULL, 4, 70.00),
(4, NULL, 5, 276.00),
(6, NULL, 6, 140.00),
(7, NULL, 7, 100.00),
(9, NULL, 9, 102.00),
(10, NULL, 10, 840.00),
(1, NULL, 11, 85.00),
(2, NULL, 12, 202.50);

-- Insert sample garantias
INSERT INTO garantias (id_venta, id_pedido, id_producto, id_cliente, descripcion_problema, fecha_compra, estado, observaciones) VALUES
(NULL, 1, 1, 1, 'Producto no enciende correctamente', '2025-01-15', 'EN_REVISION', 'Cliente reportó falla'),
(NULL, 5, 5, 5, 'Pantalla con líneas', '2025-01-20', 'EN_REPARACION', 'En proceso de reparación'),
(NULL, 7, 7, 7, 'Batería no carga', '2025-01-25', 'LISTA_EN_TIENDA', 'Reparación completada'),
(NULL, 11, 1, 11, 'Botón de encendido defectuoso', '2025-02-01', 'REGISTRADA', 'Pendiente de revisión'),
(NULL, 1, 1, 1, 'Problema con el audio', '2025-01-15', 'ENTREGADA', 'Garantía completada'),
(NULL, 2, 2, 2, 'Teclado con teclas que no responden', '2025-01-18', 'EN_REVISION', NULL),
(NULL, 3, 3, 3, 'Sobrecalentamiento', '2025-01-22', 'EN_REPARACION', 'Cambio de ventilador'),
(NULL, 5, 5, 5, 'Puerto USB dañado', '2025-01-20', 'REGISTRADA', NULL),
(NULL, 6, 6, 6, 'Cámara no funciona', '2025-01-28', 'FALLIDA', 'Daño por mal uso'),
(NULL, 9, 9, 9, 'Conectividad WiFi intermitente', '2025-02-03', 'EN_REVISION', 'Diagnóstico en proceso'),
(NULL, 12, 3, 12, 'Pantalla táctil no responde', '2025-02-05', 'LISTA_EN_TIENDA', 'Cambio de digitalizador');
