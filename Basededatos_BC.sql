CREATE DATABASE ford_motor_company;
USE ford_motor_company;

-- Creación de tablas
CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    rol VARCHAR(50),
    clave VARCHAR(255)
);

CREATE TABLE productos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    anio INT NOT NULL,
    cantidad INT NOT NULL
);

CREATE TABLE pedidos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE detalle_pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- Insertar datos
INSERT INTO usuarios (nombre, correo, rol, clave)
VALUES
    ('Bruno Campana', 'bruno.campana@gmail.com', 'Cliente', 'clave123'),
    ('Matías Peralta', 'matias.peralta@gmail.com', 'Admin', 'clave456'),
    ('María Boetto', 'maria.boetto@gmail.com', 'Cliente', 'clave789');


INSERT INTO productos (nombre, modelo, descripcion, anio, cantidad)
VALUES
    ('Ford KA', 'Trend', 'Auto compacto', 2021, 15),
    ('Ford Focus', 'SE', 'Sedán deportivo', 2022, 12),
    ('Ford Ranger Raptor', 'Raptor', 'Camioneta todo terreno', 2023, 8);


INSERT INTO pedidos (usuario_id, fecha)
VALUES
    (1, '2024-10-01'),
    (2, '2024-10-02'),
    (1, '2024-10-03');

INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad)
VALUES
    (1, 1, 2),  
    (1, 2, 1),  
    (2, 3, 1);  

-- Consultar
SELECT 
    p.id AS pedido_id,
    u.nombre AS cliente,
    p.fecha,
    dp.cantidad,
    pr.nombre AS producto,
    pr.modelo
FROM 
    pedidos p
JOIN 
    usuarios u ON p.usuario_id = u.id
JOIN 
    detalle_pedido dp ON p.id = dp.pedido_id
JOIN 
    productos pr ON dp.producto_id = pr.id;

-- Borrar datos
DELETE FROM detalle_pedido;
DELETE FROM pedidos;
DELETE FROM productos;
DELETE FROM usuarios;

-- Consultar datos
SELECT * FROM detalle_pedido;
SELECT * FROM pedidos;
SELECT * FROM productos;
SELECT * FROM usuarios;


