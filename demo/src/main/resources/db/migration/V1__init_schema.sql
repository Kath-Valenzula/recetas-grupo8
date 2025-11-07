-- V1: initial schema and demo data
-- Users table
CREATE TABLE IF NOT EXISTS users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  username VARCHAR(255) UNIQUE,
  password VARCHAR(255) NOT NULL
);

-- Recetas table (simplified)
CREATE TABLE IF NOT EXISTS recetas (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  tipo_cocina VARCHAR(100),
  ingredientes TEXT,
  pais_origen VARCHAR(100),
  dificultad VARCHAR(50),
  tiempo_coccion INT,
  instrucciones TEXT,
  imagen_url VARCHAR(512),
  popular BOOLEAN DEFAULT FALSE
);

-- Demo users (password is a bcrypt-like placeholder)
INSERT INTO users (email, password, username) VALUES
('juan.perez@example.com', '$2a$10$exampleexampleexampleexampleexamplex/abcd123456', 'juanperez')
ON DUPLICATE KEY UPDATE email=email;

INSERT INTO users (email, password, username) VALUES
('maria.gonzalez@example.com', '$2a$10$exampleexampleexampleexampleexamplex/abcd123456', 'mariagonzalez')
ON DUPLICATE KEY UPDATE email=email;

-- Demo recetas
INSERT INTO recetas (nombre, tipo_cocina, ingredientes, pais_origen, dificultad, tiempo_coccion, instrucciones, imagen_url, popular) VALUES
('Spaghetti Carbonara', 'Italiana', 'Spaghetti, huevo, panceta, queso parmesano', 'Italia', 'Media', 25, 'Mezclar y servir.', 'https://receta.example/carbonara.jpg', FALSE)
ON DUPLICATE KEY UPDATE nombre=nombre;

INSERT INTO recetas (nombre, tipo_cocina, ingredientes, pais_origen, dificultad, tiempo_coccion, instrucciones, imagen_url, popular) VALUES
('Ceviche Peruano', 'Peruana', 'Pescado, limón, cebolla, ají', 'Perú', 'Fácil', 15, 'Marinar y servir.', 'https://receta.example/ceviche.jpg', TRUE)
ON DUPLICATE KEY UPDATE nombre=nombre;
