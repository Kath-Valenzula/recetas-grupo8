-- Script de creación de base de datos y tablas (entrega Semana 3)
CREATE DATABASE IF NOT EXISTS mydatabase
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;
USE mydatabase;

-- Usuarios
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_username (username)
);

-- Recetas
CREATE TABLE recetas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    tipo_cocina VARCHAR(100),
    ingredientes TEXT,
    pais_origen VARCHAR(100),
    dificultad VARCHAR(50),
    tiempo_coccion INT,
    instrucciones TEXT,
    imagen_url VARCHAR(512),
    popular BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_busqueda (nombre, tipo_cocina, pais_origen),
    INDEX idx_popular (popular)
);

-- Datos de prueba
INSERT INTO users (email, password, username) VALUES
('juan.perez@example.com', '$2a$10$wRfZ99gV3psUtydNY/d.huptK4ajBn.a9rMxjPb76Wi2FJbYmBwwC', 'juanperez'),
('maria.gonzalez@example.com', '$2a$10$wRfZ99gV3psUtydNY/d.huptK4ajBn.a9rMxjPb76Wi2FJbYmBwwC', 'mariagonzalez'),
('se.valdivia@duocuc.cl', '$2a$10$wRfZ99gV3psUtydNY/d.huptK4ajBn.a9rMxjPb76Wi2FJbYmBwwC', 'se.valdivia');

INSERT INTO recetas (nombre, tipo_cocina, ingredientes, pais_origen, dificultad, tiempo_coccion, instrucciones, imagen_url, popular) VALUES
('Spaghetti Carbonara', 'Italiana', 'Spaghetti, huevo, panceta, queso parmesano', 'Italia', 'Media', 25, 'Cocinar la pasta al dente...', 'https://ejemplo.com/carbonara.jpg', FALSE),
('Ceviche Peruano', 'Peruana', 'Pescado, limón, cebolla, ají', 'Perú', 'Fácil', 15, 'Cortar el pescado...', 'https://ejemplo.com/ceviche.jpg', TRUE),
('Empanadas Chilenas', 'Chilena', 'Harina, carne molida, cebolla, huevo', 'Chile', 'Media', 40, 'Preparar el pino...', 'https://ejemplo.com/empanadas.jpg', TRUE);