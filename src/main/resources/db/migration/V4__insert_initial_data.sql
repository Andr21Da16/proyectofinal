-- V4: Insert initial data

-- Insert roles
INSERT INTO roles (nombre_rol, descripcion_rol) VALUES
('ADMIN', 'Administrador del sistema con acceso completo'),
('GERENTE', 'Gerente con acceso a todas las sucursales'),
('VENDEDOR', 'Vendedor asignado a una sucursal específica');

-- Insert estados de venta
INSERT INTO estado_venta (nombre_estado, descripcion_estado) VALUES
('PENDIENTE', 'Venta pendiente de confirmación'),
('COMPLETADA', 'Venta completada exitosamente'),
('CANCELADA', 'Venta cancelada'),
('REEMBOLSADA', 'Venta reembolsada');

-- Insert departamentos de ejemplo
INSERT INTO departamentos (nombre_departamento) VALUES
('Lima'),
('Arequipa'),
('Cusco'),
('La Libertad'),
('Piura');

-- Insert ciudades de ejemplo
INSERT INTO ciudades (nombre_ciudad, id_departamento) VALUES
('Lima', 1),
('Callao', 1),
('Arequipa', 2),
('Cusco', 3),
('Trujillo', 4),
('Piura', 5);

-- Insert categorías de ejemplo
INSERT INTO categorias (nombre_categoria, descripcion_categoria) VALUES
('Refrigeración', 'Refrigeradoras y congeladoras'),
('Lavado', 'Lavadoras y secadoras'),
('Cocina', 'Cocinas y hornos'),
('Climatización', 'Aires acondicionados y ventiladores'),
('Audio y Video', 'Televisores y equipos de sonido'),
('Pequeños Electrodomésticos', 'Licuadoras, batidoras, etc.');
