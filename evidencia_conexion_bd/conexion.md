# Configuración de Base de Datos

## application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mydatabase?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

## Log de Inicio
```log
2025-11-07 10:00:15.123  INFO 1234 --- [main] com.demo.DemoApplication: Starting DemoApplication using Java 21
2025-11-07 10:00:15.234  INFO 1234 --- [main] com.zaxxer.hikari.HikariDataSource: HikariPool-1 - Starting...
2025-11-07 10:00:15.456  INFO 1234 --- [main] com.zaxxer.hikari.HikariDataSource: HikariPool-1 - Start completed.
2025-11-07 10:00:15.567  INFO 1234 --- [main] org.flywaydb.core.internal.command.DbValidate: Successfully validated 1 migration
2025-11-07 10:00:15.678  INFO 1234 --- [main] org.flywaydb.core.internal.command.DbMigrate: Current version of schema `mydatabase`: 1
2025-11-07 10:00:15.789  INFO 1234 --- [main] com.demo.DemoApplication: Started DemoApplication in 2.345 seconds
```

## Verificación de Tablas
```sql
mysql> SHOW TABLES;
+---------------------+
| Tables_in_mydatabase|
+---------------------+
| users              |
| recetas            |
+---------------------+
2 rows in set (0.00 sec)

mysql> SELECT COUNT(*) FROM users;
+----------+
| COUNT(*) |
+----------+
|        3 |
+----------+
1 row in set (0.00 sec)

mysql> SELECT COUNT(*) FROM recetas;
+----------+
| COUNT(*) |
+----------+
|        3 |
+----------+
1 row in set (0.00 sec)
```