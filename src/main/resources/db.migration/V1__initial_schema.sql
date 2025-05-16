-- Crear base de datos (se debe ejecutar desde otra conexión)
-- CREATE DATABASE quotation_db;
-- \c quotation_db

-- Crear tipos ENUM solo si no existen
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'role_type') THEN
        CREATE TYPE role_type AS ENUM ('ADMIN', 'SELLER');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status_type') THEN
        CREATE TYPE status_type AS ENUM ('DRAFT', 'APPROVED');
    END IF;
END
$$;

-- Crear tablas solo si no existen
CREATE TABLE IF NOT EXISTS client (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    rnc VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    address VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS configuration (
    id BIGSERIAL PRIMARY KEY,
    rnc VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    draft_prefix VARCHAR(10),
    draft_sequence INT NOT NULL,
    prefix  VARCHAR(10),
    sequence INT NOT NULL,
    report_template VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS delivery_time (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    days INT NOT NULL
);

CREATE TABLE IF NOT EXISTS department (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS tax (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    rate DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS product (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    unit_price DOUBLE PRECISION,
    tax_id BIGINT REFERENCES Tax(id)
);

CREATE TABLE IF NOT EXISTS seller (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    report_template VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_app (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role role_type NOT NULL
);

CREATE TABLE IF NOT EXISTS quotation_header (
    id BIGSERIAL PRIMARY KEY,
    quotation_number VARCHAR(255),
    sequential_number VARCHAR(255),
    client_id BIGINT REFERENCES Client(id),
    seller_id BIGINT REFERENCES Seller(id),
    user_id BIGINT REFERENCES User_App(id),
    department_id BIGINT REFERENCES Department(id),
    delivery_time_id BIGINT REFERENCES Delivery_Time(id),
    issue_date DATE,
    subtotal_amount DOUBLE PRECISION,
    tax_amount DOUBLE PRECISION,
    total_amount DOUBLE PRECISION,
    status status_type
);

CREATE TABLE IF NOT EXISTS quotation_detail (
    id BIGSERIAL PRIMARY KEY,
    quotation_header_id BIGINT REFERENCES Quotation_Header(id),
    product_id BIGINT REFERENCES Product(id),
    quoted_price DOUBLE PRECISION NOT NULL,
    quantity INT,
    tax_amount DOUBLE PRECISION
);

-- Insertar datos solo si las tablas están vacías
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM user_app) THEN
        INSERT INTO user_app (username, password, name, role) VALUES
            ('admin', 'admin123', 'Administrador General', 'ADMIN'),
            ('seller', 'seller123', 'Vendedor Uno', 'SELLER');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Department) THEN
        INSERT INTO Department (name) VALUES
            ('TIENDA PRINCIPAL'),
            ('TIENDA SANTIAGO');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM delivery_time) THEN
        INSERT INTO delivery_time (description, days) VALUES
            ('1 a 2 Dias Laborables', 2),
            ('3 a 5 Dias Laborables', 5),
            ('6 a 10 Dias Laborables', 10);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Tax) THEN
        INSERT INTO Tax (name, rate) VALUES
            ('ITBIS 18%', 1.18),
            ('ITBIS 16%', 1.16),
            ('ITBIS 0%', 1.0),
            ('EXENTO', 1.0);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Configuration) THEN
        INSERT INTO Configuration (name, address, phone, draft_prefix, draft_sequence, prefix, sequence, report_template) VALUES
            ('OmegaTech', 'Av. John F. Kennedy, Km 8 1/2
Los Prados, Santo Domingo
Rep. Dom.', '(809) 683-4343', 'COT', 0, 'TRA', 0,
'Tiempo de entrega de 3 a 5 dias en stock si no de 3 a 5 semanas una vez colocada la PO, sin imprevistos.
Precios y disponibilidad del producto pueden variar sin previo aviso.');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Client) THEN
        INSERT INTO Client (name, rnc, email, phone, address) VALUES
            ('Empresa ABC', '123456789', 'contacto@abc.com', '8091234567', 'Av. Independencia #45, Santo Domingo'),
            ('Comercial XYZ', '987654321', 'ventas@xyz.com', '8097654321', 'Calle del Sol #12, Santiago');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM Seller) THEN
        INSERT INTO Seller (name, report_template) VALUES
            ('Juan Pérez'),
            ('María Gómez');
    END IF;
END
$$;
