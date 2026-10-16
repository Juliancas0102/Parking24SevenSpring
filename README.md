# Parking 24 Seven - Spring Boot

Módulo REST desarrollado para la evidencia **GA7-220501096-AA3-EV02** del proyecto **Parking 24 Seven**.

## Tecnologías

- Java 17
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- MySQL Driver
- Maven
- Hibernate / JPA

## Arquitectura

El proyecto se encuentra organizado por responsabilidades:

- `model`: contiene la entidad JPA `Usuario`.
- `repository`: acceso y persistencia de datos mediante `JpaRepository`.
- `service`: lógica de negocio, validaciones, autenticación y gestión de usuarios.
- `controller`: exposición de servicios mediante API REST.
- `exception`: manejo centralizado de excepciones y respuestas de error.
- `util`: utilidades de seguridad para tratamiento de contraseñas.

## Módulo de usuarios

El módulo implementa operaciones para administrar usuarios de Parking 24 Seven.

Cada usuario contiene principalmente:

- Identificador único.
- Nombre de usuario.
- Contraseña protegida mediante PBKDF2.
- Rol.
- Estado activo o inactivo.

## Roles implementados

El sistema admite los siguientes roles:

- `ADMINISTRADOR`
- `OPERADOR`
- `CONTADOR`
- `CLIENTE`

Los roles son normalizados y validados desde la capa de servicio.

## Base de datos

La aplicación utiliza MySQL y trabaja con el esquema:

`parking24seven`

La entidad `Usuario` se relaciona con la tabla:

`usuarios`

El script de base de datos se encuentra en:

`database/parking24seven.sql`

La contraseña de MySQL no debe almacenarse directamente en el repositorio.

Para ejecutar localmente puede configurarse mediante variables de entorno.

Ejemplo en Windows PowerShell:

```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="SU_CLAVE_MYSQL"