# PalletApiJwt

# Sistema de Gestión de Palets y Cargas en la Industria Logística

Este proyecto es una solución para gestionar los palets y las cargas en un almacén logístico. Permite a los administradores controlar los inventarios de palets y cargas, mientras que los transportistas tienen acceso a la información sobre las cargas que deben transportar. Este sistema está desarrollado utilizando **Spring Boot** y **JWT** para la autenticación.

## Descripción del Proyecto

En la industria logística, los palets son plataformas utilizadas para almacenar, apilar y transportar mercancías. Este sistema permite gestionar los palets y las cargas asociadas a ellos, y está diseñado para ser utilizado por administradores y transportistas con diferentes roles y permisos.

### Características Principales

- **Gestión de Palets**: Los administradores pueden crear, modificar y eliminar palets.
- **Gestión de Cargas**: Los administradores pueden crear y asignar cargas a los palets, además de poder modificar las cargas existentes.
- **Auditoría**: El sistema mantiene un registro de todas las acciones realizadas en los palets y las cargas.
- **Autenticación con JWT**: Se utilizan tokens JWT para la autenticación y autorización de los usuarios.

## Especificaciones del Sistema

### 1. Endpoints y Métodos

#### Autenticación:
- **POST** `/api/auth/register`: Registrar un nuevo usuario (Administrador o Transportista).
- **POST** `/api/auth/login`: Iniciar sesión y obtener un token JWT.

#### Gestión de Palets:
- **GET** `/api/pallets`: Obtener todos los palets. Solo para administradores.
- **GET** `/api/pallets/{id}`: Obtener los detalles de un palet específico. Solo para administradores.
- **POST** `/api/pallets`: Crear un nuevo palet. Solo para administradores.
- **PUT** `/api/pallets/{id}`: Modificar un palet existente. Solo para administradores.
- **DELETE** `/api/pallets/{id}`: Eliminar un palet. Solo para administradores.

#### Gestión de Cargas:
- **GET** `/api/loads`: Obtener todas las cargas. Solo para administradores.
- **GET** `/api/loads/{id}`: Obtener los detalles de una carga específica. Solo para administradores.
- **POST** `/api/loads`: Crear una nueva carga y asignarla a un palet. Solo para administradores.
- **PUT** `/api/loads/{id}`: Modificar una carga existente. Solo para administradores.
- **PATCH** `/api/loads/{id}/status`: Actualizar el estado de una carga (Pendiente, En tránsito, Entregado). Transportista o administrador.
- **PATCH** `/api/loads/{id}/damage`: Reportar daños en una carga. Transportista o administrador.

#### Gestión de Transportistas:
- **GET** `/api/carriers/loads`: Obtener las cargas asignadas a un transportista. Solo para transportistas.

#### Auditoría:
- **GET** `/api/audit-logs`: Consultar los cambios y actualizaciones realizadas en el sistema por fecha específica. Solo para administradores.

### 2. Funcionalidades Detalladas

#### 1. Autenticación:
- Los usuarios pueden registrarse e iniciar sesión utilizando JWT. Los roles de **administrador** y **transportista** definen qué recursos están disponibles para cada usuario.

#### 2. Gestión de Palets:
- Los administradores pueden crear, modificar, y eliminar palets, que tienen atributos como capacidad máxima, ubicación y estado (Disponible, En uso, Dañado).

#### 3. Gestión de Cargas:
- Los administradores pueden asignar cargas a los palets, asegurándose de que la suma de los pesos no exceda la capacidad máxima del palet.
- Los transportistas pueden cambiar el estado de las cargas (por ejemplo, marcar una carga como "En tránsito" o "Entregado").

#### 4. Auditoría:
- El sistema realiza un seguimiento de todas las modificaciones realizadas en los palets y cargas, lo que permite a los administradores consultar un registro detallado en formato PDF por fecha.

### 3. Validaciones
- El sistema valida que no se puedan asignar cargas a un palet si la suma de los pesos excede su capacidad.
- Los administradores y transportistas pueden reportar daños en palets y cargas, lo cual se refleja en el estado del palet o la carga.

## Requisitos del Proyecto

- **Java 17** o superior
- **Spring Boot 2.x**
- **JWT** para la autenticación y autorización
- **MySQL** para la base de datos
- **Maven** o **Gradle** para la gestión de dependencias

## Instalación y Uso

### Clonación del Repositorio

Para clonar este repositorio, usa el siguiente comando:

```bash
git clone https://github.com/SoyNar/PalletApiJwt.git
