# Guía de configuración de demo

## Java 17

En caso de tener Java 17, cambia en el `pom.xml` la versión de Java a utilizar.

## Ejecutar MySQL con Docker

```bash
docker build -t my-mysql-db .
docker run -d \
  --name mysql-container \
  -p 3306:3306 \
  -p 33060:33060 \
  my-mysql-db
```

## Java 21

En caso de tener Java 21, asegúrate de que el entorno esté configurado correctamente.
