# CompraAmigo Backend - TP1 2026-20

Backend revisado contra la rúbrica TP1 y las Historias de Usuario Extendidas de CompraAmigo.

## Stack alineado al código del docente
- Java 21
- Spring Boot 4.0.8
- Spring Web
- Spring Data JPA
- PostgreSQL
- Spring Security + JWT
- DTO + ModelMapper
- Lombok
- Swagger/OpenAPI (springdoc 3.1.0)
- JPQL / Query Methods
- @Transactional

## Cobertura funcional
El backend cubre las HU01-HU30 del documento validado y añade tres historias necesarias para cubrir las pantallas de reporte del prototipo y el criterio **Reports** de la rúbrica:

- HU31: Consultar impacto personal ODS 12 -> `GET /api/impacto/usuario/{userId}`
- HU32: Consultar resumen del dashboard administrativo -> `GET /api/dashboard/resumen`
- HU33: Consultar reporte ODS 12 por categoría -> `GET /api/impacto/categorias`

## Criterios de API de la rúbrica
- CRUD: CRUD de productos (`GET/POST/PUT/DELETE /api/producto(s)`).
- Reports: Dashboard e impacto ODS mediante consultas agregadas JPQL/Query Methods.
- Security: registro, autenticación JWT y autorización con `@PreAuthorize` para USER/ADMIN.

## Modelo JPA
Se priorizan relaciones unidireccionales. `DetalleCarrito` conoce a `Carrito` y `Producto`; `DetallePedido` conoce a `Pedido` y `Producto`. `Pedido` y `Carrito` no contienen colecciones `@OneToMany` hacia sus detalles.

## Base de datos
Crear en PostgreSQL:

```sql
CREATE DATABASE db_compraamigo;
```

Ajustar credenciales en `src/main/resources/application.properties`.

Hibernate genera/actualiza las tablas con:

```properties
spring.jpa.hibernate.ddl-auto=update
```

## Cuentas demo del import.sql
- Cliente: `cliente@gmail.com` / `123456`
- Administrador: `admin@gmail.com` / `Admin123`

Si la base ya existía antes, comprobar manualmente que ambas cuentas estén presentes y con sus roles.

## Prueba mínima para TP1
1. Ejecutar la aplicación con Java 21.
2. Abrir Swagger: `http://localhost:8080/swagger-ui/index.html`.
3. Probar `POST /api/authenticate`.
4. Copiar el JWT y usar **Authorize**.
5. Probar CRUD de productos con ADMIN.
6. Probar carrito/pedido con USER.
7. Probar `GET /api/dashboard/resumen` y `GET /api/impacto/categorias` con ADMIN.
8. Verificar datos en pgAdmin.

> Nota: en este entorno no se pudo ejecutar Maven Wrapper porque requiere descargar Maven desde Internet. La validación final de compilación/ejecución debe hacerse localmente en IntelliJ.
