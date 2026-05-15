# Backend de **Kurio**.

Este repositorio contiene el servicio backend, su configuración para ejecutarlo y desplegarlo con **Docker Compose**.

[!NOTE]
Este repositorio contiene la build hecha en el repositorio del frontend. Sin embargo, no contiene el archivo para hacer la conexión al proyecto de Firebase.

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

- Despues de ejecutar estos comandos, accede al frontend y executa estes comando:
```bash
npm run dev
```

Después entra en esta ruta para entrar en la aplicación en local [http://localhost:5173](http://localhost:5173).

## Probar en producción
- Para acceder a la aplicación en producción accede a traves de este enlace [https://kurio.duckdns.org/](https://kurio.duckdns.org/).

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
  docker exec -it kurio-backend sh
  cd logs
  ls
  cat LOG.log #Reemplaza LOG por el archivo mostrado en el ls
  ```
- Parar:
  ```bash
  docker compose down
  ```
