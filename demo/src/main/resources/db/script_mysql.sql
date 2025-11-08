-- Configuración de charset y collation
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS mydatabase
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

USE mydatabase;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX idx_role_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de asignación de roles a usuarios
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user 
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de recetas
CREATE TABLE IF NOT EXISTS recetas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    ingredientes TEXT NOT NULL,
    preparacion TEXT NOT NULL,
    tiempo_prep INT NOT NULL,
    porciones INT NOT NULL,
    dificultad VARCHAR(20) NOT NULL,
    imagen_url VARCHAR(255),
    is_public BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_titulo (titulo),
    INDEX idx_is_public (is_public),
    CONSTRAINT fk_recetas_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Reiniciar foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Insertar roles básicos
INSERT IGNORE INTO roles (name) VALUES 
('ROLE_ADMIN'),
('ROLE_USER'),
('ROLE_CHEF');

-- Insertar usuarios demo
-- Passwords: admin123, user123, chef123 (BCrypt encoded)
INSERT IGNORE INTO users (username, password, email, enabled) VALUES
('admin', '$2a$10$rDm6yMXWCGiLWcq8YyN0/.7vGBHgYHyNYMkm7C8EgWwdcyV8Vd2A6', 'admin@recetas.local', true),
('user', '$2a$10$EebknDGNXB0s4hsDMLvDVOW8oRp6yd.oqKdgtx93QUiBAGBJXZw6y', 'user@recetas.local', true),
('chef', '$2a$10$uGqYB9bVBajEOhJn7nIYL.ZKzEQzJsRNg1Lj3PFH15VWZV2uTGJFi', 'chef@recetas.local', true);

-- Asignar roles a usuarios
INSERT IGNORE INTO user_roles (user_id, role_id)
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

INSERT IGNORE INTO user_roles (user_id, role_id)
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'user' AND r.name = 'ROLE_USER';

INSERT IGNORE INTO user_roles (user_id, role_id)
SELECT u.id, r.id 
FROM users u, roles r 
WHERE u.username = 'chef' AND r.name = 'ROLE_CHEF';

-- Insertar algunas recetas de ejemplo
INSERT IGNORE INTO recetas (titulo, descripcion, ingredientes, preparacion, tiempo_prep, porciones, dificultad, is_public, user_id)
SELECT 
    'Pastel de Choclo',
    'Tradicional pastel de choclo chileno',
    '- 6 choclos\n- 500g carne molida\n- 2 cebollas\n- Condimentos al gusto',
    '1. Preparar el pino\n2. Moler el choclo\n3. Armar el pastel\n4. Hornear por 30 minutos',
    60,
    6,
    'MEDIA',
    true,
    u.id
FROM users u
WHERE u.username = 'chef'
LIMIT 1;

INSERT IGNORE INTO recetas (titulo, descripcion, ingredientes, preparacion, tiempo_prep, porciones, dificultad, is_public, user_id)
SELECT 
    'Empanadas de Pino',
    'Clásicas empanadas chilenas de pino',
    '- Masa para empanadas\n- Carne molida\n- Cebollas\n- Huevos duros\n- Aceitunas',
    '1. Preparar el pino\n2. Armar las empanadas\n3. Hornear hasta dorar',
    90,
    8,
    'MEDIA',
    true,
    u.id
FROM users u
WHERE u.username = 'chef'
LIMIT 1;