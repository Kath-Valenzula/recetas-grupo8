-- Moved from application resources. Example seed data for manual use.

INSERT INTO users (email, password, username)
VALUES ('juan.perez@example.com', '$2a$10$wRfZ99gV3psUtydNY/d.huptK4ajBn.a9rMxjPb76Wi2FJbYmBwwC', 'juanperez');

INSERT INTO users (email, password, username)
VALUES ('maria.gonzalez@example.com', '$2a$10$wRfZ99gV3psUtydNY/d.huptK4ajBn.a9rMxjPb76Wi2FJbYmBwwC', 'mariagonzalez');

-- sample recetas rows (shortened)
INSERT INTO recetas (nombre, tipo_cocina, ingredientes, pais_origen, dificultad, tiempo_coccion, instrucciones, imagen_url, popular) VALUES
('Spaghetti Carbonara', 'Italiana', 'Spaghetti, huevo, panceta, queso parmesano', 'Italia', 'Media', 25, 'Preparar y mezclar', 'https://receta.example/img1.jpg', FALSE);
