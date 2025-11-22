-- V1.1: Fix missing tables (restore base schema if deleted)

-- DEPARTAMENTOS Y CIUDADES
CREATE TABLE IF NOT EXISTS departamentos(
    id_departamento SERIAL NOT NULL,
    nombre_departamento VARCHAR(100) NOT NULL,
    CONSTRAINT id_departamento_pk PRIMARY KEY (id_departamento),
    CONSTRAINT nombre_departamento_un UNIQUE (nombre_departamento)
);

CREATE TABLE IF NOT EXISTS ciudades(
    id_ciudad SERIAL NOT NULL,
    nombre_ciudad VARCHAR(100) NOT NULL,
    id_departamento INTEGER NOT NULL,
    CONSTRAINT id_ciudad_pk PRIMARY KEY (id_ciudad),
    CONSTRAINT id_departamento_fk FOREIGN KEY (id_departamento)
        REFERENCES departamentos (id_departamento)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT nombre_ciudad_un UNIQUE (nombre_ciudad)
);

-- PROVEEDORES
CREATE TABLE IF NOT EXISTS proveedores(
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

-- CATEGORIAS Y PRODUCTOS
CREATE TABLE IF NOT EXISTS categorias(
    id_categoria SERIAL NOT NULL,
    nombre_categoria VARCHAR(100) NOT NULL,
    descripcion_categoria TEXT,
    CONSTRAINT id_categoria_pk PRIMARY KEY (id_categoria),
    CONSTRAINT nombre_categoria_un UNIQUE (nombre_categoria)
);

CREATE TABLE IF NOT EXISTS productos(
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

-- TABLA PIVOT PRODUCTOS-PROVEEDORES
CREATE TABLE IF NOT EXISTS productos_proveedores(
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

-- SUCURSALES
CREATE TABLE IF NOT EXISTS sucursales (
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

-- TABLA PIVOT SUCURSAL-PRODUCTO
CREATE TABLE IF NOT EXISTS sucursal_producto(
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

-- ROLES Y COLABORADORES
CREATE TABLE IF NOT EXISTS roles(
    id_rol SERIAL NOT NULL,
    nombre_rol VARCHAR(50) NOT NULL,
    descripcion_rol TEXT,
    CONSTRAINT id_rol_pk PRIMARY KEY (id_rol),
    CONSTRAINT nombre_rol_un UNIQUE (nombre_rol)
);

CREATE TABLE IF NOT EXISTS colaborador(
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

-- CARRITO DE COMPRAS
CREATE TABLE IF NOT EXISTS carrito_compras(
    id_carrito SERIAL NOT NULL,
    id_colaborador INTEGER NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT id_carrito_pk PRIMARY KEY (id_carrito),
    CONSTRAINT id_colaborador_carrito_fk FOREIGN KEY (id_colaborador)
        REFERENCES colaborador (id_colaborador)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS carrito_detalle (
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

-- ESTADOS DE VENTA
CREATE TABLE IF NOT EXISTS estado_venta (
    id_estado_venta SERIAL NOT NULL,
    nombre_estado VARCHAR(50) NOT NULL,
    descripcion_estado TEXT,
    CONSTRAINT id_estado_venta_pk PRIMARY KEY (id_estado_venta),
    CONSTRAINT nombre_estado_un UNIQUE (nombre_estado)
);

-- VENTAS
CREATE TABLE IF NOT EXISTS ventas (
    id_venta SERIAL NOT NULL,
    id_carrito INTEGER NOT NULL,
    id_colaborador INTEGER NOT NULL,
    id_sucursal INTEGER NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10, 2) NOT NULL CHECK (total >= 0),
    metodo_pago VARCHAR(50) NOT NULL,
    id_estado_venta INTEGER NOT NULL,
    descuento_aplicado NUMERIC(10,2) DEFAULT 0,
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
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS detalle_venta (
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

-- BOLETAS/COMPROBANTES
CREATE TABLE IF NOT EXISTS boletas (
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

-- Índices para mejorar rendimiento
CREATE INDEX IF NOT EXISTS idx_productos_categoria ON productos(id_categoria);
CREATE INDEX IF NOT EXISTS idx_productos_activo ON productos(activo);
CREATE INDEX IF NOT EXISTS idx_colaborador_sucursal ON colaborador(id_sucursal);
CREATE INDEX IF NOT EXISTS idx_colaborador_rol ON colaborador(id_rol);
CREATE INDEX IF NOT EXISTS idx_ventas_fecha ON ventas(fecha_venta);
CREATE INDEX IF NOT EXISTS idx_ventas_colaborador ON ventas(id_colaborador);
CREATE INDEX IF NOT EXISTS idx_ventas_sucursal ON ventas(id_sucursal);
CREATE INDEX IF NOT EXISTS idx_ventas_estado ON ventas(id_estado_venta);
