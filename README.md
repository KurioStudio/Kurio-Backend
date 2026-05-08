# Backend de **Kurio**.

Este repositorio contiene el servicio backend y su configuración para ejecutarlo localmente y desplegarlo con **Docker Compose**.

## Tecnologías

- Java (Spring Boot)
- Docker / Docker Compose
- Firebase (requiere JSON de credenciales)
- MongoDB

## Requisitos

- Docker y Docker Compose instalados
- Archivo JSON de credenciales de Firebase (service account)

## Ejecutar en local

- Para ejecutar la aplicación en local es necesario contar con Docker en el sistema.
```bash
cd Kurio-Backend #Nos posicionamos en la carpeta del proyecto
docker-compose up --build #Creamos el contenedor y lo ejecutamos
```

- Despues de ejecutar estos comandos, accede a esta ruta para entrar en la aplicación en local [http://localhost:8080](http://localhost:8080).

## Despliegue
Recomendado: ejecutar detrás de un reverse proxy (Nginx/Traefik) y configurar un gestor de procesos (systemd).

### Opción 2: Docker (si existe `Dockerfile`)

```bash
docker build -t kurio-backend .
docker run -p 8080:8080 --env-file .env kurio-backend
```

## Base de datos

El proyecto utiliza:

- **Firebase** (requiere credenciales JSON)
- **MongoDB** (Se ejecuta junto a la aplicación)

## Comandos útiles

- Levantar en segundo plano:
  ```bash
  docker compose up -d --build
  ```
- Ver logs:
  ```bash
  docker compose logs -f
  ```
- Parar:
  ```bash
  docker compose down
  ```
