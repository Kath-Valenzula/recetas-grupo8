# sc-s2 (microservices demo)

Pequeño proyecto con backend (`demo`) y frontend (`frontend`).

## Requisitos

- JDK 21
- Docker (opcional, para levantar MySQL)
- Maven (se usa el wrapper `mvnw` incluido)

## Cómo construir

1. Backend (demo):

    ```powershell
    & '.\\demo\\mvnw.cmd' -f '.\\demo\\pom.xml' -DskipTests clean package
    ```

2. Frontend:

    ```powershell
    & '.\\frontend\\mvnw.cmd' -f '.\\frontend\\pom.xml' -DskipTests clean package
    ```

## Levantar MySQL con docker-compose (opcional)

```powershell
cd sc-s2\sc-s2
docker-compose up -d
```

## Endpoints y usuarios de prueba

- DB: `mydatabase` en `localhost:3306` (usuario `myuser`, pass `mypassword`)
- Usuarios demo creados por la migración Flyway: `juan.perez@example.com`, `maria.gonzalez@example.com` (password hash placeholder).

## Notas

- Las pruebas de `demo` usan H2 en memoria (configuración en `demo/src/test/resources/application.properties`).
- Se usa Flyway para la migración inicial (`demo/src/main/resources/db/migration/V1__init_schema.sql`).
- `data.sql` original se movió a `docs/examples/data.sql` para uso manual.
