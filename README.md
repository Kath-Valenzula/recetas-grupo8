# Aplicación de Recetas

Aplicación web para gestión de recetas de cocina con autenticación de usuarios y roles.

## Requisitos

- Java 21 (Temurin/Eclipse Adoptium)
- Maven (se usa el wrapper `mvnw` incluido)
- MySQL 8.0+
- Docker y Docker Compose (opcional, para la base de datos)

## Cómo construir

1. Backend (demo):

    ```powershell
    & '.\\demo\\mvnw.cmd' -f '.\\demo\\pom.xml' -DskipTests clean package
    ```

2. Frontend:

    ```powershell
    & '.\\frontend\\mvnw.cmd' -f '.\\frontend\\pom.xml' -DskipTests clean package
    ```

## Configuración de Base de Datos

### Opción 1: MySQL Local

1. Instalar MySQL 8.0+.
2. Crear base de datos y usuario:

    ```sql
    CREATE DATABASE mydatabase CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    CREATE USER 'myuser'@'localhost' IDENTIFIED BY 'mypassword';
    GRANT ALL PRIVILEGES ON mydatabase.* TO 'myuser'@'localhost';
    ```

3. Ejecutar script de inicialización:

    ```bash
    mysql -u myuser -p mydatabase < db/script_mysql.sql
    ```

### Opción 2: Docker Compose (recomendado)

1. Ejecutar contenedor MySQL:

    ```bash
    docker-compose up -d
    ```

2. El script de inicialización se ejecutará automáticamente.

## Levantar MySQL con docker-compose (opcional)

```powershell
cd sc-s2\sc-s2
```

## Endpoints y usuarios de prueba

- Base de datos: `mydatabase` en `localhost:3306` (usuario `myuser`, contraseña `mypassword`).
- Usuarios demo creados por la migración Flyway: `juan.perez@example.com`, `maria.gonzalez@example.com` (password hash placeholder).

## Ejecución de la Aplicación

### Backend (Puerto 8080)

```bash
cd demo
./mvnw spring-boot:run
```

### Frontend (Puerto 8081)

```bash
cd frontend
./mvnw spring-boot:run
```

## Usuarios Demo

| Usuario | Contraseña | Rol        | Permisos                          |
|---------|------------|------------|-----------------------------------|
| admin   | admin123   | ROLE_ADMIN | Acceso total más panel de admin   |
| chef    | chef123    | ROLE_CHEF  | Gestión de recetas                |
| user    | user123    | ROLE_USER  | Ver recetas y gestionar favoritos |

## Endpoints

### Públicos

- `GET /` - Página principal
- `GET /login` - Formulario de login
- `GET /css/**`, `/js/**`, `/images/**` - Recursos estáticos
- `GET /recetas/public/**` - Recetas públicas
- `GET /actuator/health`, `/actuator/info` - Health checks

### Protegidos (requiere autenticación)

- `GET /recetas/**` - Listado/detalle de recetas
- `POST/PUT/DELETE /**` - Modificaciones (según rol)

### Admin

- `GET/POST /admin/**` - Panel administrativo

## Pruebas con Postman

1. Importar colección: `New Collection.postman_collection.json`.
2. Configurar variables de ambiente:

   - `host`: [http://localhost:8080](http://localhost:8080)
   - `token`: se obtiene al hacer login

### Evidencias y Documentación

- `/postman/evidencias/` - Capturas de pruebas Postman
- `/evidencia_conexion_bd/` - Logs de conexión a la base de datos
- `/db/script_mysql.sql` - Script de inicialización de la base de datos
- `links.txt` - Enlaces relevantes (repositorio, video demo)

## Seguridad

- CORS habilitado para frontend (localhost:8081)
- Encabezados de seguridad (CSP, HSTS, etc.)
- Sesión única por usuario
- Protección contra CSRF
- Endpoints auditados con Actuator

## CI/CD

El proyecto incluye GitHub Actions para:

- Pruebas unitarias
- Análisis de dependencias (OWASP)
- Build de backend y frontend

Para ejecutar el análisis de seguridad localmente:

```bash
./mvnw -Psecurity verify
```

## Estructura del Proyecto

```text
.
├── demo/                # Backend (Spring Boot)
│   ├── src/
│   └── pom.xml
├── frontend/            # Frontend (Spring MVC + Thymeleaf)
│   ├── src/
│   └── pom.xml
├── db/                  # Scripts de base de datos
├── postman/             # Colección y evidencias Postman
└── docker-compose.yml   # Configuración Docker
```

## Notas Adicionales

- Las pruebas usan H2 en memoria (configuración en `demo/src/test/resources/application.properties`).
- Se usa Flyway para migraciones de base de datos.
- La base de datos usa UTF-8MB4 para soporte completo de caracteres Unicode.
