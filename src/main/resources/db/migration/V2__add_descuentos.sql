-- V2: Add discount management tables

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

CREATE INDEX idx_descuentos_producto ON descuentos(id_producto);
CREATE INDEX idx_descuentos_fechas ON descuentos(fecha_inicio, fecha_fin);
CREATE INDEX idx_descuentos_activo ON descuentos(activo);
