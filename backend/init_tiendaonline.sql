-- Crear tablas principales para TiendaOnline
CREATE DATABASE IF NOT EXISTS TiendaOnline;
USE TiendaOnline;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    first_last_name VARCHAR(100),
    second_last_name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS clients (
    id INT PRIMARY KEY,
    id_client VARCHAR(20),
    email VARCHAR(100),
    password VARCHAR(100),
    payment_method_id INT,
    FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS discounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    discount_code VARCHAR(20) UNIQUE,
    description VARCHAR(255),
    percentage DECIMAL(5,2)
);

CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_product VARCHAR(20) UNIQUE,
    name_product VARCHAR(100),
    description VARCHAR(255),
    price DECIMAL(10,2),
    stock INT,
    category_id INT,
    discount_id INT,
    image_url VARCHAR(255),
    FOREIGN KEY (discount_id) REFERENCES discounts(id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    client_id INT,
    quantity INT,
    coupon_code VARCHAR(64),
    coupon_pct DECIMAL(5,2),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- Insertar datos de ejemplo
INSERT INTO users (name, first_last_name, second_last_name) VALUES ('Vi', 'A', 'B');
INSERT INTO clients (id, id_client, email, password, payment_method_id) VALUES (LAST_INSERT_ID(), '2', 'vivi@gmail.com', '1234', NULL);

INSERT INTO discounts (discount_code, description, percentage) VALUES ('0000', 'Descuento de ejemplo', 10.00);

INSERT INTO products (id_product, name_product, description, price, stock, category_id, discount_id, image_url) VALUES
('BAG001', 'Mochila Urbana Negra', 'Mochila resistente al agua con múltiples compartimentos', 220.50, 15, 1, NULL, NULL),
('BAG002', 'Bolso Tote Colorido', 'Bolso tote grande con diseño colorido ideal para el día a día', 180.00, 20, 1, NULL, NULL),
('BAG003', 'Mochila Escolar Azul', 'Mochila ergonómica con compartimiento para laptop', 200.00, 10, 1, NULL, NULL),
('BAG004', 'Bolso de Mano Elegante', 'Bolso compacto para ocasiones especiales, material sintético', 250.00, 8, 1, NULL, NULL),
('BAG005', 'Mochila Deportiva Roja', 'Ideal para gimnasio y salidas, con bolsillos exteriores', 195.75, 12, 1, NULL, NULL);

UPDATE products SET discount_id = (SELECT id FROM discounts WHERE discount_code = '0000') WHERE id = 6;
