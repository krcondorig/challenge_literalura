# 📚 Literalura

**Challenge 2 - Oracle Alura**

Una aplicación Spring Boot para buscar, registrar y gestionar libros y autores de la API de Gutendex (Proyecto Gutenberg).

## 🎯 Descripción

Literalura es una aplicación de consola que permite:

- Buscar libros por título en la API de Gutendex
- Registrar libros y autores en una base de datos PostgreSQL
- Consultar estadísticas y rankings de libros
- Filtrar libros por idioma
- Analizar información de autores

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 4.0.2**
- **Spring Data JPA**
- **PostgreSQL**
- **Jackson** (para procesamiento JSON)
- **Maven** (gestión de dependencias)

## 📋 Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- PostgreSQL 12+
- Git

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/krcondorig/challenge_literalura.git
```

### 2. Configurar Base de Datos

Crea una base de datos PostgreSQL:

```sql
CREATE DATABASE db_literalura;
```

### 3. Configurar Variables de Entorno

La aplicación utiliza variables de entorno para la configuración de la base de datos. Configura las siguientes variables:

Deberas agregar las siguientes variables de entorno para que la aplicacion pueda funcionar, a continuacion el nombre que cada variable de entorno debe tener

<table style="border: 1px solid black; border-collapse: collapse;">
    <tr style="text-align: center;">
        <td>VARIABLE</td>
        <td>DESCRIPCIÓN</td>
    </tr>
    <tr>
        <td>DB_HOST</td>
        <td>Ruta a la base de datos con su puerto. Ejemplo: localhost</td>
    </tr>
    <tr>
        <td>DB_NAME</td>
        <td>Nombre de la base de datos</td>
    </tr>
    <tr>
        <td>DB_USER</td>
        <td>Usuario de la base de datos</td>
    </tr>
    <tr>
        <td>DB_PASSWORD</td>
        <td>Contraseña de acceso a la base de datos</td>
    </tr>
</table>

### 4. Configuración de la Aplicación

El archivo `src/main/resources/application.properties` ya está configurado para usar variables de entorno:

```properties
spring.application.name=literalura
spring.datasource.url=jdbc:postgresql://${DB_HOST}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.jpa.hibernate.ddl-auto=update
```

### 5. Compilar y Ejecutar

```bash
# Compilar el proyecto
./mvnw clean compile

# Ejecutar la aplicación
./mvnw spring-boot:run
```

## 📖 Funcionalidades

### Menú Principal

![menu](https://github.com/user-attachments/assets/2c0e05af-e39c-48c9-88ed-e5e6331e7fc9)

La aplicación ofrece las siguientes opciones:

1. **🔍 Buscar Libro por título** - Busca libros en Gutendex y los registra
2. **📚 Listar libros registrados** - Muestra todos los libros guardados
3. **✍️ Listar autores registrados** - Muestra todos los autores guardados
4. **📅 Listar autores vivos por año** - Autores vivos en un año específico
5. **🌍 Listar libros por idioma** - Filtra libros por código de idioma
6. **📊 Estadísticas** - Muestra estadísticas generales de la colección
7. **🏆 Top 10 libros más descargados** - Ranking de popularidad
8. **🔎 Buscar autor por nombre** - Búsqueda específica de autores
9. **📈 Autores por rango de nacimiento** - Autores nacidos en un período
10. **👵 Autores más longevos** - Autores con mayor edad al fallecer
11. **🚪 Salir** - Cierra la aplicación


### Buscar Libro por titulo

![opcion1](https://github.com/user-attachments/assets/6f3b38c8-ce66-440c-b4fb-874689bb4e7e)

### Listar libros registrados

![opcion2](https://github.com/user-attachments/assets/df0137cc-d641-41ae-bad2-0cb6835e6c05)

### Listar autores registrados

![opcion3](https://github.com/user-attachments/assets/a29a52c1-db27-4ff8-83fe-a93194f98ee1)

### Listar autores vivos por X año

![opcion4](https://github.com/user-attachments/assets/4292b478-0b78-4f7e-bf60-162a68264226)

### Listar libros por idioma

![opcion5](https://github.com/user-attachments/assets/f6c6b19b-6296-41a8-b1b9-cafa4affe8da)

### Algunas estadisticas

![opcion6](https://github.com/user-attachments/assets/aeca791e-aba4-46ee-83e2-284f6a700e04)

### Top 10 libros mas descargados

![opcion7](https://github.com/user-attachments/assets/c300590a-287b-4531-97ac-5e89c88b5509)

### Buscar autor por nombre

![opcion8](https://github.com/user-attachments/assets/d6d74f08-d933-49b8-8109-2de504826c8c)

### Autores por rango de nacimiento

![opcion9](https://github.com/user-attachments/assets/07407d7e-d17c-4be2-ae45-a1e1aec0f3fc)

### Autores mas longevos

![opcion10](https://github.com/user-attachments/assets/a41f0937-7740-4551-bc29-cea19053359a)

