
INSERT INTO users (email, password, username)
VALUES ('juan.perez@example.com', '$2a$10$wRfZ99gV3psUtydNY/d.huptK4ajBn.a9rMxjPb76Wi2FJbYmBwwC', 'juanperez');

INSERT INTO users (email, password, username)
VALUES ('maria.gonzalez@example.com', '$2a$10$wRfZ99gV3psUtydNY/d.huptK4ajBn.a9rMxjPb76Wi2FJbYmBwwC', 'mariagonzalez');

INSERT INTO users (email, password, username)
VALUES ('se.valdivia@duocuc.cl', '$2a$10$wRfZ99gV3psUtydNY/d.huptK4ajBn.a9rMxjPb76Wi2FJbYmBwwC', 'se.valdivia');


INSERT INTO recetas (nombre, tipo_cocina, ingredientes, pais_origen, dificultad, tiempo_coccion, instrucciones, imagen_url, popular)
VALUES
(
  'Spaghetti Carbonara',
  'Italiana',
  'Spaghetti, huevo, panceta, queso parmesano',
  'Italia',
  'Media',
  25,
  'Cocinar la pasta al dente; Preparar la mezcla de huevo y queso parmesano; Freír la panceta hasta que esté crujiente; Mezclar la pasta caliente con la panceta y la mezcla de huevo y queso; Servir inmediatamente con más queso por encima.',
  'https://recetasdecocina.elmundo.es/wp-content/uploads/2024/09/espaguetis-a-la-carbonara-1024x683.jpg',
  FALSE
),
(
  'Ceviche Peruano',
  'Peruana',
  'Pescado, limón, cebolla, ají',
  'Perú',
  'Fácil',
  15,
  'Cortar el pescado en cubos pequeños; Exprimir los limones y agregar el jugo sobre el pescado; Añadir la cebolla en pluma y el ají picado; Dejar reposar la mezcla por unos minutos; Servir frío acompañado de camote o choclo.',
  'https://www.laylita.com/recetas/wp-content/uploads/2013/08/1-Cebiche-de-pescado.jpg',
  TRUE
),
(
  'Tacos al Pastor',
  'Mexicana',
  'Tortillas, carne de cerdo, piña, cebolla, cilantro',
  'México',
  'Media',
  30,
  'Marinar la carne de cerdo con achiote y especias; Cocinar la carne en un trompo o sartén hasta que esté dorada; Calentar las tortillas; Servir la carne con piña, cebolla y cilantro; Acompañar con salsa al gusto.',
  'https://sharkninja-cookingcircle.s3.eu-west-1.amazonaws.com/wp-content/uploads/2021/12/02154019/Tacos-al-pastor-1500x1000.jpg',
  FALSE
),
(
  'Sushi de Salmón',
  'Japonesa',
  'Arroz para sushi, salmón, alga nori, vinagre de arroz',
  'Japón',
  'Difícil',
  50,
  'Cocinar el arroz para sushi y mezclar con vinagre de arroz; Cortar el salmón en tiras delgadas; Colocar el alga nori sobre la esterilla; Extender el arroz sobre el alga y añadir el salmón; Enrollar y cortar en piezas iguales.',
  'https://www.salmonchile.cl/assets/uploads/2023/05/Sashimi-de-salmon-scaled.webp',
  TRUE
),
(
  'Empanadas Chilenas',
  'Chilena',
  'Harina, carne molida, cebolla, huevo duro, aceituna',
  'Chile',
  'Media',
  40,
  'Preparar el pino cocinando carne molida con cebolla y condimentos; Hacer la masa con harina, manteca y agua; Rellenar cada empanada con pino, huevo y aceituna; Cerrar y sellar los bordes; Hornear hasta que estén doradas.',
  'https://gourmet.iprospect.cl/wp-content/uploads/2016/09/Empanadas-web-1.jpg',
  TRUE
);
