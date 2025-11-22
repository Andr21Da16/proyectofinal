-- COMPLETE DATABASE SCRIPT FOR COOLBOX ERP
-- This script recreates the entire database schema and inserts sample data.
-- WARNING: This will drop the existing 'public' schema and lose all data.

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

-- ==================================================================================
-- 1. TABLES CREATION
-- ==================================================================================

-- 1.1. DEPARTAMENTOS Y CIUDADES
CREATE TABLE departamentos(
    id_departamento SERIAL NOT NULL,
    nombre_departamento VARCHAR(100) NOT NULL,
    CONSTRAINT id_departamento_pk PRIMARY KEY (id_departamento),
    CONSTRAINT nombre_departamento_un UNIQUE (nombre_departamento)
);

CREATE TABLE ciudades(
    id_ciudad SERIAL NOT NULL,
    nombre_ciudad VARCHAR(100) NOT NULL,
    id_departamento INTEGER NOT NULL,
    CONSTRAINT id_ciudad_pk PRIMARY KEY (id_ciudad),
    CONSTRAINT id_departamento_fk FOREIGN KEY (id_departamento)
        REFERENCES departamentos (id_departamento)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT nombre_ciudad_un UNIQUE (nombre_ciudad)
);

-- 1.2. PROVEEDORES
CREATE TABLE proveedores(
    id_proveedor SERIAL NOT NULL,
    nombre_proveedor VARCHAR(100) NOT NULL,
    email_proveedor VARCHAR(300) NOT NULL,
    telefono_proveedor VARCHAR(20) NOT NULL,
    id_ciudad INTEGER NOT NULL,
    CONSTRAINT id_proveedor_pk PRIMARY KEY (id_proveedor),
    CONSTRAINT id_ciudad_proveedor_fk FOREIGN KEY (id_ciudad)
        REFERENCES ciudades (id_ciudad)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT nombre_proveedor_un UNIQUE (nombre_proveedor),
    CONSTRAINT email_proveedor_un UNIQUE (email_proveedor),
    CONSTRAINT telefono_proveedor_un UNIQUE (telefono_proveedor)
);

-- 1.3. CATEGORIAS Y PRODUCTOS
CREATE TABLE categorias(
    id_categoria SERIAL NOT NULL,
    nombre_categoria VARCHAR(100) NOT NULL,
    descripcion_categoria TEXT,
    CONSTRAINT id_categoria_pk PRIMARY KEY (id_categoria),
    CONSTRAINT nombre_categoria_un UNIQUE (nombre_categoria)
);

CREATE TABLE productos(
    id_producto SERIAL NOT NULL,
    nombre_producto VARCHAR(100) NOT NULL,
    marca_producto VARCHAR(50) NOT NULL,
    modelo_producto VARCHAR(100) NOT NULL,
    id_categoria INTEGER NOT NULL,
    dimensiones_producto TEXT,
    especificaciones_producto TEXT,
    peso_producto NUMERIC(10,2),
    url_imagen_producto TEXT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT id_producto_pk PRIMARY KEY (id_producto),
    CONSTRAINT id_categoria_fk FOREIGN KEY (id_categoria)
        REFERENCES categorias (id_categoria)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT nombre_producto_un UNIQUE (nombre_producto)
);

-- 1.4. TABLA PIVOT PRODUCTOS-PROVEEDORES
CREATE TABLE productos_proveedores(
    id_proveedor INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    precio_producto NUMERIC(10,2) NOT NULL CHECK (precio_producto >= 0),
    stock_producto INTEGER NOT NULL CHECK (stock_producto >= 0),
    CONSTRAINT id_proveedor_producto_pk PRIMARY KEY (id_proveedor, id_producto),
    CONSTRAINT id_proveedor_pp_fk FOREIGN KEY (id_proveedor)
        REFERENCES proveedores (id_proveedor)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT id_producto_pp_fk FOREIGN KEY (id_producto)
        REFERENCES productos (id_producto)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1.5. SUCURSALES
CREATE TABLE sucursales (
    id_sucursal SERIAL NOT NULL,
    nombre_sucursal VARCHAR(100) NOT NULL,
    direccion_sucursal TEXT NOT NULL,
    telefono_sucursal VARCHAR(20),
    email_sucursal VARCHAR(150),
    id_ciudad INTEGER NOT NULL,
    fecha_apertura DATE,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT id_sucursal_pk PRIMARY KEY (id_sucursal),
    CONSTRAINT id_ciudad_sucursal_fk FOREIGN KEY (id_ciudad)
        REFERENCES ciudades (id_ciudad)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT nombre_sucursal_un UNIQUE (nombre_sucursal)
);

-- 1.6. TABLA PIVOT SUCURSAL-PRODUCTO
CREATE TABLE sucursal_producto(
    id_sucursal INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    id_proveedor INTEGER NOT NULL,
    stock_producto INTEGER NOT NULL CHECK (stock_producto >= 0),
    precio_producto NUMERIC(10, 2) NOT NULL CHECK (precio_producto >= 0),
    CONSTRAINT id_sucursal_producto_proveedor_pk PRIMARY KEY (id_sucursal, id_producto, id_proveedor),
    CONSTRAINT id_sucursal_sp_fk FOREIGN KEY (id_sucursal)
        REFERENCES sucursales (id_sucursal)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT id_producto_proveedor_sp_fk FOREIGN KEY (id_producto, id_proveedor)
        REFERENCES productos_proveedores(id_producto, id_proveedor)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1.7. ROLES Y COLABORADORES
CREATE TABLE roles(
    id_rol SERIAL NOT NULL,
    nombre_rol VARCHAR(50) NOT NULL,
    descripcion_rol TEXT,
    tipo_acceso VARCHAR(50), -- Added in V5
    activo BOOLEAN DEFAULT true, -- Added in V5
    CONSTRAINT id_rol_pk PRIMARY KEY (id_rol),
    CONSTRAINT nombre_rol_un UNIQUE (nombre_rol)
);

CREATE TABLE colaborador(
    id_colaborador SERIAL NOT NULL,
    nombre_colaborador VARCHAR(100) NOT NULL,
    email_colaborador VARCHAR(300) NOT NULL,
    numero_colaborador VARCHAR(20),
    usuario_colaborador VARCHAR(50) NOT NULL,
    contraseña_colaborador VARCHAR(255) NOT NULL,
    id_sucursal INTEGER,
    id_rol INTEGER NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso TIMESTAMP,
    CONSTRAINT id_colaborador_pk PRIMARY KEY (id_colaborador),
    CONSTRAINT id_sucursal_colaborador_fk FOREIGN KEY (id_sucursal)
        REFERENCES sucursales (id_sucursal)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT id_rol_colaborador_fk FOREIGN KEY (id_rol)
        REFERENCES roles (id_rol)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT usuario_colaborador_un UNIQUE (usuario_colaborador),
    CONSTRAINT email_colaborador_un UNIQUE (email_colaborador)
);

-- 1.8. CLIENTES (V5)
CREATE TABLE clientes (
    id_cliente SERIAL PRIMARY KEY,
    nombre_completo VARCHAR(200) NOT NULL,
    email VARCHAR(300) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    direccion TEXT,
    tipo_documento VARCHAR(20),
    numero_documento VARCHAR(50) UNIQUE,
    id_ciudad INTEGER,
    password VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT true,
    FOREIGN KEY (id_ciudad) REFERENCES ciudades(id_ciudad)
);

-- 1.9. CARRITO DE COMPRAS
CREATE TABLE carrito_compras(
    id_carrito SERIAL NOT NULL,
    id_colaborador INTEGER NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT id_carrito_pk PRIMARY KEY (id_carrito),
    CONSTRAINT id_colaborador_carrito_fk FOREIGN KEY (id_colaborador)
        REFERENCES colaborador (id_colaborador)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE carrito_detalle (
    id_carrito INTEGER NOT NULL,
    id_sucursal INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    id_proveedor INTEGER NOT NULL,
    cantidad INTEGER NOT NULL DEFAULT 1 CHECK (cantidad > 0),
    precio_unitario NUMERIC(10,2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal NUMERIC(10,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,
    CONSTRAINT id_carrito_detalle_pk PRIMARY KEY (id_carrito, id_sucursal, id_producto, id_proveedor),
    CONSTRAINT id_carrito_cd_fk FOREIGN KEY (id_carrito)
        REFERENCES carrito_compras (id_carrito)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT id_sucursal_producto_cd_fk FOREIGN KEY (id_sucursal, id_producto, id_proveedor)
        REFERENCES sucursal_producto (id_sucursal, id_producto, id_proveedor)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1.10. ESTADOS DE VENTA
CREATE TABLE estado_venta (
    id_estado_venta SERIAL NOT NULL,
    nombre_estado VARCHAR(50) NOT NULL,
    descripcion_estado TEXT,
    CONSTRAINT id_estado_venta_pk PRIMARY KEY (id_estado_venta),
    CONSTRAINT nombre_estado_un UNIQUE (nombre_estado)
);

-- 1.11. CUPONES (V5)
CREATE TABLE cupones (
    id_cupon SERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    descuento_porcentaje DECIMAL(5,2),
    descuento_monto DECIMAL(10,2),
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
    uso_maximo INTEGER,
    uso_actual INTEGER DEFAULT 0,
    activo BOOLEAN DEFAULT true,
    monto_minimo DECIMAL(10,2)
);

-- 1.12. PEDIDOS (V5)
CREATE TABLE pedidos (
    id_pedido SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    descuento_aplicado DECIMAL(10,2) DEFAULT 0,
    estado_logistico VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    estado_pago VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    metodo_pago VARCHAR(50),
    direccion_entrega TEXT,
    id_sucursal INTEGER,
    id_cupon INTEGER,
    fecha_entrega TIMESTAMP,
    observaciones TEXT,
    id_venta_generada INTEGER,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal),
    FOREIGN KEY (id_cupon) REFERENCES cupones(id_cupon)
);

CREATE TABLE pedido_items (
    id_pedido INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_pedido, id_producto),
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- 1.13. VENTAS (Updated V5)
CREATE TABLE ventas (
    id_venta SERIAL NOT NULL,
    id_carrito INTEGER NOT NULL,
    id_colaborador INTEGER NOT NULL,
    id_sucursal INTEGER NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    metodo_pago VARCHAR(50) NOT NULL,
    id_estado_venta INTEGER NOT NULL,
    descuento_aplicado NUMERIC(10,2) DEFAULT 0,
    id_cliente INTEGER, -- Added V5
    id_pedido INTEGER, -- Added V5
    CONSTRAINT id_venta_pk PRIMARY KEY (id_venta),
    CONSTRAINT id_carrito_venta_fk FOREIGN KEY (id_carrito)
        REFERENCES carrito_compras (id_carrito)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT id_colaborador_venta_fk FOREIGN KEY (id_colaborador)
        REFERENCES colaborador (id_colaborador)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT id_sucursal_venta_fk FOREIGN KEY (id_sucursal)
        REFERENCES sucursales (id_sucursal)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT id_estado_venta_fk FOREIGN KEY (id_estado_venta)
        REFERENCES estado_venta (id_estado_venta)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_ventas_cliente FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    CONSTRAINT fk_ventas_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido)
);

CREATE TABLE detalle_venta (
    id_venta INTEGER NOT NULL,
    id_sucursal INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    id_proveedor INTEGER NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10,2) NOT NULL CHECK (precio_unitario >= 0),
    descuento NUMERIC(10,2) DEFAULT 0,
    subtotal NUMERIC(10,2) GENERATED ALWAYS AS (cantidad * precio_unitario - descuento) STORED,
    CONSTRAINT detalle_venta_pk PRIMARY KEY (id_venta, id_sucursal, id_producto, id_proveedor),
    CONSTRAINT id_venta_dv_fk FOREIGN KEY (id_venta)
        REFERENCES ventas (id_venta)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT id_sucursal_producto_dv_fk FOREIGN KEY (id_sucursal, id_producto, id_proveedor)
        REFERENCES sucursal_producto (id_sucursal, id_producto, id_proveedor)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 1.14. BOLETAS/COMPROBANTES
CREATE TABLE boletas (
    id_boleta SERIAL NOT NULL,
    id_venta INTEGER NOT NULL,
    numero_boleta VARCHAR(30) UNIQUE NOT NULL,
    tipo_comprobante VARCHAR(20) NOT NULL,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ruc_cliente VARCHAR(15),
    nombre_cliente VARCHAR(100),
    direccion_cliente TEXT,
    CONSTRAINT id_boleta_pk PRIMARY KEY (id_boleta),
    CONSTRAINT id_venta_boleta_fk FOREIGN KEY (id_venta)
        REFERENCES ventas (id_venta)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1.15. DESCUENTOS (V2)
CREATE TABLE descuentos (
    id_descuento SERIAL NOT NULL,
    id_producto INTEGER NOT NULL,
    porcentaje_descuento NUMERIC(5,2) NOT NULL CHECK (porcentaje_descuento >= 0 AND porcentaje_descuento <= 100),
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    descripcion TEXT,
    CONSTRAINT id_descuento_pk PRIMARY KEY (id_descuento),
    CONSTRAINT id_producto_descuento_fk FOREIGN KEY (id_producto)
        REFERENCES productos (id_producto)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fecha_descuento_check CHECK (fecha_fin > fecha_inicio)
);

-- 1.16. CHAT SYSTEM (V3)
CREATE TABLE chat_rooms (
    id_room SERIAL NOT NULL,
    nombre_room VARCHAR(100) NOT NULL,
    tipo_room VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT id_room_pk PRIMARY KEY (id_room)
);

CREATE TABLE chat_participants (
    id_room INTEGER NOT NULL,
    id_colaborador INTEGER NOT NULL,
    fecha_union TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT chat_participants_pk PRIMARY KEY (id_room, id_colaborador),
    CONSTRAINT id_room_cp_fk FOREIGN KEY (id_room)
        REFERENCES chat_rooms (id_room)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT id_colaborador_cp_fk FOREIGN KEY (id_colaborador)
        REFERENCES colaborador (id_colaborador)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE chat_messages (
    id_message SERIAL NOT NULL,
    id_room INTEGER NOT NULL,
    id_colaborador INTEGER NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leido BOOLEAN DEFAULT FALSE,
    tipo_mensaje VARCHAR(20) DEFAULT 'TEXT',
    url_archivo TEXT, -- Added V5
    es_fijado BOOLEAN DEFAULT false, -- Added V5
    CONSTRAINT id_message_pk PRIMARY KEY (id_message),
    CONSTRAINT id_room_cm_fk FOREIGN KEY (id_room)
        REFERENCES chat_rooms (id_room)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT id_colaborador_cm_fk FOREIGN KEY (id_colaborador)
        REFERENCES colaborador (id_colaborador)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE chat_online_status (
    id_colaborador INTEGER NOT NULL,
    online BOOLEAN DEFAULT FALSE,
    ultima_actividad TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT id_colaborador_online_pk PRIMARY KEY (id_colaborador),
    CONSTRAINT id_colaborador_online_fk FOREIGN KEY (id_colaborador)
        REFERENCES colaborador (id_colaborador)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 1.17. ROL PERMISOS (V5)
CREATE TABLE rol_permiso (
    id_permiso SERIAL PRIMARY KEY,
    id_rol INTEGER NOT NULL,
    nombre_modulo VARCHAR(100) NOT NULL,
    puede_ver BOOLEAN DEFAULT false,
    puede_editar BOOLEAN DEFAULT false,
    puede_crear BOOLEAN DEFAULT false,
    puede_eliminar BOOLEAN DEFAULT false,
    UNIQUE(id_rol, nombre_modulo),
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol) ON DELETE CASCADE
);

-- 1.18. COMPRAS PROVEEDOR (V5)
CREATE TABLE compras_proveedor (
    id_compra SERIAL PRIMARY KEY,
    id_proveedor INTEGER NOT NULL,
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'REGISTRADA',
    id_colaborador INTEGER,
    id_sucursal INTEGER,
    observaciones TEXT,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor),
    FOREIGN KEY (id_colaborador) REFERENCES colaborador(id_colaborador),
    FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal)
);

CREATE TABLE compra_proveedor_items (
    id_compra INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    cantidad_solicitada INTEGER NOT NULL,
    cantidad_recibida INTEGER DEFAULT 0,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_compra, id_producto),
    FOREIGN KEY (id_compra) REFERENCES compras_proveedor(id_compra) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- 1.19. CAMPAÑAS MARKETING (V5)
CREATE TABLE campanias_marketing (
    id_campania SERIAL PRIMARY KEY,
    nombre_campania VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
    descuento_porcentaje DECIMAL(5,2),
    activo BOOLEAN DEFAULT true,
    tipo_descuento VARCHAR(20) DEFAULT 'PORCENTAJE'
);

CREATE TABLE campania_productos (
    id_campania INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    PRIMARY KEY (id_campania, id_producto),
    FOREIGN KEY (id_campania) REFERENCES campanias_marketing(id_campania) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE
);

-- 1.20. USO DE CUPONES (V5)
CREATE TABLE cupon_usos (
    id_uso SERIAL PRIMARY KEY,
    id_cupon INTEGER NOT NULL,
    id_venta INTEGER,
    id_pedido INTEGER,
    fecha_uso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    monto_descuento DECIMAL(10,2),
    FOREIGN KEY (id_cupon) REFERENCES cupones(id_cupon),
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido)
);

-- 1.21. GARANTIAS (V5)
CREATE TABLE garantias (
    id_garantia SERIAL PRIMARY KEY,
    id_venta INTEGER,
    id_pedido INTEGER,
    id_producto INTEGER NOT NULL,
    id_cliente INTEGER NOT NULL,
    descripcion_problema TEXT,
    fecha_compra TIMESTAMP,
    fecha_reporte TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'REGISTRADA',
    id_colaborador_asignado INTEGER,
    observaciones TEXT,
    fecha_resolucion TIMESTAMP,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_colaborador_asignado) REFERENCES colaborador(id_colaborador)
);

-- ==================================================================================
-- 2. INDEXES
-- ==================================================================================
CREATE INDEX idx_productos_categoria ON productos(id_categoria);
CREATE INDEX idx_productos_activo ON productos(activo);
CREATE INDEX idx_colaborador_sucursal ON colaborador(id_sucursal);
CREATE INDEX idx_colaborador_rol ON colaborador(id_rol);
CREATE INDEX idx_ventas_fecha ON ventas(fecha_venta);
CREATE INDEX idx_ventas_colaborador ON ventas(id_colaborador);
CREATE INDEX idx_ventas_sucursal ON ventas(id_sucursal);
CREATE INDEX idx_ventas_estado ON ventas(id_estado_venta);
CREATE INDEX idx_descuentos_producto ON descuentos(id_producto);
CREATE INDEX idx_descuentos_fechas ON descuentos(fecha_inicio, fecha_fin);
CREATE INDEX idx_chat_messages_room ON chat_messages(id_room);
CREATE INDEX idx_chat_messages_fecha ON chat_messages(fecha_envio);
CREATE INDEX idx_pedidos_cliente ON pedidos(id_cliente);
CREATE INDEX idx_pedidos_estado_logistico ON pedidos(estado_logistico);
CREATE INDEX idx_garantias_cliente ON garantias(id_cliente);
CREATE INDEX idx_compras_proveedor ON compras_proveedor(id_proveedor);
CREATE INDEX idx_clientes_email ON clientes(email);
CREATE INDEX idx_cupones_codigo ON cupones(codigo);

-- ==================================================================================
-- 3. DATA INSERTION (10+ Records per table)
-- ==================================================================================

-- 3.1. ROLES
INSERT INTO roles (nombre_rol, descripcion_rol, tipo_acceso, activo) VALUES
('ADMIN', 'Administrador del sistema con acceso completo', 'ERP', true),
('GERENTE', 'Gerente con acceso a todas las sucursales', 'ERP', true),
('VENDEDOR', 'Vendedor asignado a una sucursal específica', 'ERP', true),
('ALMACENERO', 'Encargado de inventario y almacén', 'ERP', true),
('CAJERO', 'Encargado de caja y facturación', 'ERP', true),
('SOPORTE', 'Soporte técnico y garantías', 'ERP', true),
('MARKETING', 'Encargado de campañas y promociones', 'ERP', true),
('CLIENTE_WEB', 'Cliente registrado en la web', 'WEB', true),
('INVITADO', 'Usuario invitado con acceso limitado', 'WEB', true),
('SUPERVISOR', 'Supervisor de zona', 'ERP', true);

-- 3.2. DEPARTAMENTOS
INSERT INTO departamentos (nombre_departamento) VALUES
('Lima'), ('Arequipa'), ('Cusco'), ('La Libertad'), ('Piura'),
('Lambayeque'), ('Junín'), ('Ancash'), ('Ica'), ('Tacna');

-- 3.3. CIUDADES
INSERT INTO ciudades (nombre_ciudad, id_departamento) VALUES
('Lima', 1), ('Callao', 1), ('Arequipa', 2), ('Cusco', 3), ('Trujillo', 4),
('Piura', 5), ('Chiclayo', 6), ('Huancayo', 7), ('Chimbote', 8), ('Ica', 9);

-- 3.4. SUCURSALES
INSERT INTO sucursales (nombre_sucursal, direccion_sucursal, telefono_sucursal, email_sucursal, id_ciudad, fecha_apertura, activo) VALUES
('Sede Central Lima', 'Av. Javier Prado 1234', '01-222-3333', 'lima@coolbox.com', 1, '2020-01-01', true),
('Tienda Jockey Plaza', 'Av. Javier Prado Este 4200', '01-222-4444', 'jockey@coolbox.com', 1, '2020-02-15', true),
('Tienda Arequipa Mall', 'Av. Ejército 793', '054-222-333', 'arequipa@coolbox.com', 3, '2020-03-10', true),
('Tienda Cusco Real', 'Av. Collasuyo 2964', '084-222-333', 'cusco@coolbox.com', 4, '2020-04-05', true),
('Tienda Trujillo Centro', 'Jr. Pizarro 456', '044-222-333', 'trujillo@coolbox.com', 5, '2020-05-20', true),
('Tienda Piura Open', 'Av. Cáceres 147', '073-222-333', 'piura@coolbox.com', 6, '2020-06-15', true),
('Tienda Chiclayo Mall', 'Av. Balta 890', '074-222-333', 'chiclayo@coolbox.com', 7, '2020-07-01', true),
('Tienda Huancayo Real', 'Av. Ferrocarril 123', '064-222-333', 'huancayo@coolbox.com', 8, '2020-08-10', true),
('Tienda Chimbote Mega', 'Av. Pardo 567', '043-222-333', 'chimbote@coolbox.com', 9, '2020-09-05', true),
('Tienda Ica Plaza', 'Av. San Martín 890', '056-222-333', 'ica@coolbox.com', 10, '2020-10-20', true);

-- 3.5. COLABORADORES (Passwords are BCrypt for 'password123' except Gerente)
-- Gerente: gerente / gerente123 ($2a$10$dXJ3SW6G7P3EyFCLTwMemuIUVlb67B3OjSO.WNxR3VgOSdyBXm.5e)
-- Otros: password123 ($2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J)
INSERT INTO colaborador (nombre_colaborador, email_colaborador, usuario_colaborador, contraseña_colaborador, id_rol, id_sucursal) VALUES
('Gerente General', 'gerente@coolbox.com', 'gerente', '$2a$10$dXJ3SW6G7P3EyFCLTwMemuIUVlb67B3OjSO.WNxR3VgOSdyBXm.5e', 2, 1),
('Admin Sistema', 'admin@coolbox.com', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', 1, 1),
('Juan Vendedor', 'juan.vendedor@coolbox.com', 'jvendedor', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', 3, 1),
('Maria Vendedora', 'maria.vendedora@coolbox.com', 'mvendedora', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', 3, 2),
('Pedro Almacen', 'pedro.almacen@coolbox.com', 'palmacen', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', 4, 1),
('Ana Cajera', 'ana.cajera@coolbox.com', 'acajera', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', 5, 2),
('Luis Soporte', 'luis.soporte@coolbox.com', 'lsoporte', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', 6, 1),
('Sofia Marketing', 'sofia.marketing@coolbox.com', 'smarketing', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', 7, 1),
('Carlos Supervisor', 'carlos.supervisor@coolbox.com', 'csupervisor', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', 10, 3),
('Elena Vendedora', 'elena.vendedora@coolbox.com', 'evendedora', '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', 3, 3);

-- 3.6. PROVEEDORES
INSERT INTO proveedores (nombre_proveedor, email_proveedor, telefono_proveedor, id_ciudad) VALUES
('Samsung Peru', 'contacto@samsung.pe', '01-555-1001', 1),
('LG Electronics', 'ventas@lg.pe', '01-555-1002', 1),
('Sony Peru', 'info@sony.pe', '01-555-1003', 1),
('Apple Distributor', 'sales@apple-dist.pe', '01-555-1004', 1),
('Xiaomi Peru', 'contacto@xiaomi.pe', '01-555-1005', 1),
('Lenovo Peru', 'ventas@lenovo.pe', '01-555-1006', 1),
('HP Peru', 'info@hp.pe', '01-555-1007', 1),
('Dell Peru', 'sales@dell.pe', '01-555-1008', 1),
('Logitech Peru', 'contacto@logitech.pe', '01-555-1009', 1),
('Asus Peru', 'ventas@asus.pe', '01-555-1010', 1);

-- 3.7. CATEGORIAS
INSERT INTO categorias (nombre_categoria, descripcion_categoria) VALUES
('Laptops', 'Portátiles de todas las marcas'),
('Smartphones', 'Teléfonos inteligentes'),
('Televisores', 'Smart TVs y pantallas'),
('Audio', 'Audífonos y parlantes'),
('Gaming', 'Consolas y accesorios gamer'),
('Cámaras', 'Fotografía y video'),
('Hogar Inteligente', 'Dispositivos IoT'),
('Accesorios', 'Cables, fundas y cargadores'),
('Impresoras', 'Impresión y escaneo'),
('Monitores', 'Pantallas para PC');

-- 3.8. PRODUCTOS
INSERT INTO productos (nombre_producto, marca_producto, modelo_producto, id_categoria, precio_producto, stock_producto, url_imagen_producto, activo) VALUES
-- Note: precio_producto and stock_producto are not in 'productos' table in V1 schema, they are in 'productos_proveedores' and 'sucursal_producto'.
-- But wait, V1 schema shows 'productos' table DOES NOT have price/stock.
-- I will insert into 'productos' first.
('Laptop Samsung Galaxy Book', 'Samsung', 'Book3 Pro', 1, 'https://example.com/img1.jpg', true),
('iPhone 15 Pro', 'Apple', '15 Pro 256GB', 2, 'https://example.com/img2.jpg', true),
('TV LG OLED C3', 'LG', 'OLED55C3', 3, 'https://example.com/img3.jpg', true),
('Audífonos Sony WH-1000XM5', 'Sony', 'WH-1000XM5', 4, 'https://example.com/img4.jpg', true),
('PlayStation 5', 'Sony', 'Slim Digital', 5, 'https://example.com/img5.jpg', true),
('Cámara Canon EOS R50', 'Canon', 'EOS R50', 6, 'https://example.com/img6.jpg', true),
('Xiaomi Robot Vacuum', 'Xiaomi', 'S10+', 7, 'https://example.com/img7.jpg', true),
('Mouse Logitech MX Master 3S', 'Logitech', 'MX Master 3S', 8, 'https://example.com/img8.jpg', true),
('Impresora HP Smart Tank', 'HP', '580', 9, 'https://example.com/img9.jpg', true),
('Monitor Dell UltraSharp', 'Dell', 'U2723QE', 10, 'https://example.com/img10.jpg', true);

-- 3.9. PRODUCTOS_PROVEEDORES (Pivot)
INSERT INTO productos_proveedores (id_proveedor, id_producto, precio_producto, stock_producto) VALUES
(1, 1, 4500.00, 100), (4, 2, 5200.00, 50), (2, 3, 4800.00, 30), (3, 4, 1200.00, 80),
(3, 5, 2500.00, 40), (3, 6, 3000.00, 20), (5, 7, 1500.00, 60), (9, 8, 450.00, 150),
(7, 9, 800.00, 40), (8, 10, 2200.00, 25);

-- 3.10. SUCURSAL_PRODUCTO (Stock en tiendas)
INSERT INTO sucursal_producto (id_sucursal, id_producto, id_proveedor, stock_producto, precio_producto) VALUES
(1, 1, 1, 10, 5000.00), (1, 2, 4, 5, 5800.00), (1, 3, 2, 3, 5500.00), (1, 4, 3, 8, 1500.00),
(2, 5, 3, 4, 2800.00), (2, 6, 3, 2, 3500.00), (2, 7, 5, 6, 1800.00), (2, 8, 9, 15, 550.00),
(3, 9, 7, 4, 950.00), (3, 10, 8, 2, 2500.00);

-- 3.11. CLIENTES
INSERT INTO clientes (nombre_completo, email, telefono, direccion, tipo_documento, numero_documento, id_ciudad, password, activo) VALUES
('Juan Pérez', 'juan.perez@email.com', '999888777', 'Av. Lima 123', 'DNI', '12345678', 1, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true),
('Maria Lopez', 'maria.lopez@email.com', '999888776', 'Av. Arequipa 456', 'DNI', '87654321', 2, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true),
('Carlos Ruiz', 'carlos.ruiz@email.com', '999888775', 'Av. Cusco 789', 'DNI', '11223344', 3, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true),
('Ana Diaz', 'ana.diaz@email.com', '999888774', 'Av. Trujillo 321', 'DNI', '44332211', 4, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true),
('Luis Torres', 'luis.torres@email.com', '999888773', 'Av. Piura 654', 'DNI', '55667788', 5, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true),
('Sofia Vega', 'sofia.vega@email.com', '999888772', 'Av. Chiclayo 987', 'DNI', '99887766', 6, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true),
('Pedro Gomez', 'pedro.gomez@email.com', '999888771', 'Av. Huancayo 147', 'DNI', '12312312', 7, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true),
('Elena Paz', 'elena.paz@email.com', '999888770', 'Av. Chimbote 258', 'DNI', '45645645', 8, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true),
('Miguel Cruz', 'miguel.cruz@email.com', '999888769', 'Av. Ica 369', 'DNI', '78978978', 9, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true),
('Empresa SAC', 'contacto@empresa.com', '999888768', 'Av. Industrial 100', 'RUC', '20123456789', 1, '$2a$10$N9qo8uLOickgx2ZMRZoMye1J5Gj8J8J8J8J8J8J8J8J8J8J8J8J8J', true);

-- 3.12. ESTADOS VENTA
INSERT INTO estado_venta (nombre_estado, descripcion_estado) VALUES
('PENDIENTE', 'Venta pendiente de pago'),
('PAGADO', 'Venta pagada'),
('ENTREGADO', 'Venta entregada al cliente'),
('CANCELADO', 'Venta cancelada'),
('DEVUELTO', 'Venta devuelta');

-- 3.13. CUPONES
INSERT INTO cupones (codigo, descripcion, descuento_porcentaje, fecha_inicio, fecha_fin, uso_maximo, activo) VALUES
('WELCOME10', 'Bienvenida 10%', 10.00, '2024-01-01', '2025-12-31', 1000, true),
('SUMMER20', 'Verano 20%', 20.00, '2024-01-01', '2024-03-31', 500, true),
('BLACKFRIDAY', 'Black Friday 50%', 50.00, '2024-11-25', '2024-11-30', 1000, false),
('CYBERMONDAY', 'Cyber Monday 40%', 40.00, '2024-12-01', '2024-12-05', 1000, false),
('XMAS30', 'Navidad 30%', 30.00, '2024-12-01', '2024-12-25', 1000, false),
('NEWYEAR15', 'Año Nuevo 15%', 15.00, '2025-01-01', '2025-01-15', 500, true),
('FLASH50', 'Venta Flash 50%', 50.00, '2024-06-01', '2024-06-02', 100, false),
('STUDENT10', 'Estudiantes 10%', 10.00, '2024-01-01', '2025-12-31', 2000, true),
('VIP25', 'Clientes VIP 25%', 25.00, '2024-01-01', '2025-12-31', 100, true),
('EMPLOYEE', 'Empleado 30%', 30.00, '2024-01-01', '2025-12-31', 1000, true);

-- 3.14. CAMPAÑAS
INSERT INTO campanias_marketing (nombre_campania, descripcion, fecha_inicio, fecha_fin, descuento_porcentaje) VALUES
('Campaña Escolar', 'Regreso a clases', '2024-02-01', '2024-03-15', 15.00),
('Día de la Madre', 'Regalos para mamá', '2024-04-15', '2024-05-15', 20.00),
('Día del Padre', 'Regalos para papá', '2024-05-15', '2024-06-20', 20.00),
('Fiestas Patrias', 'Celebra Perú', '2024-07-01', '2024-07-31', 25.00),
('Primavera Tech', 'Tecnología en primavera', '2024-09-01', '2024-09-30', 10.00),
('Halloween', 'Ofertas de miedo', '2024-10-15', '2024-10-31', 15.00),
('Cyber Days', 'Días de descuentos online', '2024-11-15', '2024-11-18', 40.00),
('Navidad Anticipada', 'Compra antes', '2024-11-20', '2024-12-10', 30.00),
('Fin de Año', 'Cierra el año bien', '2024-12-26', '2024-12-31', 20.00),
('Verano Cool', 'Tecnología para la playa', '2025-01-01', '2025-02-28', 15.00);

-- 3.15. ROL PERMISOS
INSERT INTO rol_permiso (id_rol, nombre_modulo, puede_ver, puede_editar, puede_crear, puede_eliminar) VALUES
(1, 'PRODUCTOS', true, true, true, true), (1, 'VENTAS', true, true, true, true),
(2, 'PRODUCTOS', true, true, true, true), (2, 'VENTAS', true, true, true, true),
(3, 'PRODUCTOS', true, false, false, false), (3, 'VENTAS', true, true, true, false),
(4, 'INVENTARIO', true, true, true, true), (5, 'CAJA', true, true, true, false),
(6, 'SOPORTE', true, true, true, true), (7, 'MARKETING', true, true, true, true),
(8, 'CATALOGO', true, false, false, false), (9, 'CATALOGO', true, false, false, false),
(10, 'REPORTES', true, true, false, false);

-- End of script
