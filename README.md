# Parking 24 Seven - Spring Boot

Módulo web REST desarrollado para la evidencia **GA7-220501096-AA3-EV01**.

## Tecnologías

- Java 17
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- MySQL Driver
- Lombok
- Spring Boot DevTools
- Maven

## Arquitectura

El código se organiza por responsabilidades:

- `model`: entidad JPA `Usuario`.
- `repository`: persistencia con `JpaRepository`.
- `service`: reglas de negocio y hash PBKDF2 de contraseñas.
- `controller`: API REST.
- `exception`: manejo centralizado de errores.
- `util`: utilidades de seguridad.

## Base de datos

La aplicación usa el esquema `parking24seven` y la tabla `usuarios`.
El script de creación se encuentra en `database/parking24seven.sql`.

La contraseña de MySQL **no se guarda en el repositorio**. Antes de ejecutar, defina:

Windows PowerShell:

```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="SU_CLAVE_MYSQL"
```

## Ejecución

Desde NetBeans, ejecute la clase `Parking24SevenSpringApplication`.
La API queda disponible en `http://localhost:8081`.

## Endpoints

| Método | Ruta | Operación |
|---|---|---|
| GET | `/api/usuarios` | Listar usuarios |
| GET | `/api/usuarios/{id}` | Consultar usuario |
| POST | `/api/usuarios` | Crear usuario |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |

La contraseña se recibe en las operaciones de escritura, se almacena con PBKDF2 y no se expone en las respuestas JSON.

## Pruebas

Se incluye una colección de Postman en:

`postman/Parking24SevenSpring.postman_collection.json`

También se incluyen pruebas unitarias del servicio en `src/test/java`.
