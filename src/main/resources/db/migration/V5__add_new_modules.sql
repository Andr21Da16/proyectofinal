-- V5: Add new modules for backend expansion
-- Roles and Permissions
ALTER TABLE roles ADD COLUMN IF NOT EXISTS tipo_acceso VARCHAR(50);
ALTER TABLE roles ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT true;

CREATE TABLE IF NOT EXISTS rol_permiso (
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

-- Clientes
CREATE TABLE IF NOT EXISTS clientes (
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

-- Cupones
CREATE TABLE IF NOT EXISTS cupones (
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

-- Pedidos
CREATE TABLE IF NOT EXISTS pedidos (
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

CREATE TABLE IF NOT EXISTS pedido_items (
    id_pedido INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_pedido, id_producto),
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- Compras a Proveedores
CREATE TABLE IF NOT EXISTS compras_proveedor (
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

CREATE TABLE IF NOT EXISTS compra_proveedor_items (
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

-- Campañas de Marketing
CREATE TABLE IF NOT EXISTS campanias_marketing (
    id_campania SERIAL PRIMARY KEY,
    nombre_campania VARCHAR(200) NOT NULL,
    descripcion TEXT,
    fecha_inicio TIMESTAMP,
    fecha_fin TIMESTAMP,
    descuento_porcentaje DECIMAL(5,2),
    activo BOOLEAN DEFAULT true,
    tipo_descuento VARCHAR(20) DEFAULT 'PORCENTAJE'
);

CREATE TABLE IF NOT EXISTS campania_productos (
    id_campania INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    PRIMARY KEY (id_campania, id_producto),
    FOREIGN KEY (id_campania) REFERENCES campanias_marketing(id_campania) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE
);

-- Uso de Cupones
CREATE TABLE IF NOT EXISTS cupon_usos (
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

-- Garantías
CREATE TABLE IF NOT EXISTS garantias (
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

-- Update Ventas table
ALTER TABLE ventas ADD COLUMN IF NOT EXISTS id_cliente INTEGER;
ALTER TABLE ventas ADD COLUMN IF NOT EXISTS id_pedido INTEGER;
ALTER TABLE ventas ADD CONSTRAINT fk_ventas_cliente FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente);
ALTER TABLE ventas ADD CONSTRAINT fk_ventas_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido);

-- Update Chat Messages
ALTER TABLE chat_messages ADD COLUMN IF NOT EXISTS url_archivo TEXT;
ALTER TABLE chat_messages ADD COLUMN IF NOT EXISTS es_fijado BOOLEAN DEFAULT false;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_pedidos_cliente ON pedidos(id_cliente);
CREATE INDEX IF NOT EXISTS idx_pedidos_estado_logistico ON pedidos(estado_logistico);
CREATE INDEX IF NOT EXISTS idx_pedidos_estado_pago ON pedidos(estado_pago);
CREATE INDEX IF NOT EXISTS idx_garantias_cliente ON garantias(id_cliente);
CREATE INDEX IF NOT EXISTS idx_garantias_estado ON garantias(estado);
CREATE INDEX IF NOT EXISTS idx_compras_proveedor ON compras_proveedor(id_proveedor);
CREATE INDEX IF NOT EXISTS idx_clientes_email ON clientes(email);
CREATE INDEX IF NOT EXISTS idx_cupones_codigo ON cupones(codigo);
