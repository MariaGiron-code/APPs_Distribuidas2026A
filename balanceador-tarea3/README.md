# Tarea 3 — Balanceador de Carga con Flask, Nginx y Docker
**Materia:** Aplicaciones Distribuidas
**Escuela Politécnica Nacional · Período Académico 2026-A**

---

# ¿Qué se construyó?

Un sistema de tres contenedores Docker orquestados con `docker-compose`:

- **servidor1** y **servidor2** — dos instancias idénticas de una app Flask que muestra un mapa interactivo de las 24 provincias del Ecuador (datos leídos desde una base de datos SQLite compartida).
- **balanceador** — un servidor Nginx que actúa como único punto de entrada y reparte el tráfico entre los dos servidores en modo round-robin.

```
Usuario → Nginx (puerto 80) → servidor1 (Flask)
                            → servidor2 (Flask)
                                   ↕
                            provincias.db (volumen)
```

---

# Estructura del proyecto

```
balanceador-tarea3/
├── app/
│   ├── app.py              ← Aplicación Flask
│   ├── init_db.py          ← Script que crea y puebla la base de datos
│   ├── requirements.txt    ← Dependencias de Python
│   ├── Dockerfile          ← Receta para construir la imagen Docker de Flask
│   ├── .dockerignore       ← Archivos que Docker debe ignorar
│   ├── provincias.db       ← Base de datos SQLite (generada por init_db.py)
│   └── templates/
│       └── index.html      ← Página con el mapa Leaflet/OpenStreetMap
├── nginx/
│   └── nginx.conf          ← Configuración del balanceador
├── docker-compose.yml      ← Orquesta los 3 contenedores
└── README.md
```

---

## Conceptos clave

### ¿Qué es Flask?
Framework web minimalista para Python. Recibe peticiones HTTP, ejecuta código Python y devuelve una respuesta (HTML, JSON, etc.). No incluye base de datos ni autenticación por defecto — tú decides qué añadir.

### ¿Qué es Docker?
Plataforma que empaqueta una aplicación junto a **todo su entorno** (sistema operativo, dependencias, configuración) en un **contenedor** — una unidad aislada que corre igual en cualquier máquina.

### ¿Qué es docker-compose?
Herramienta que define y levanta **múltiples contenedores** con un solo archivo (`docker-compose.yml`) y un solo comando. Gestiona la red entre contenedores, los volúmenes y el orden de arranque.

### ¿Qué es un balanceador de carga?
Un intermediario que recibe todas las peticiones entrantes y las distribuye entre varios servidores backend. Objetivos: alta disponibilidad (si un servidor falla, el otro sigue), escalabilidad y mejor rendimiento.

### ¿Qué es round-robin?
El algoritmo de balanceo más simple: cada petición va al siguiente servidor de la lista, en rotación. Petición 1 → servidor1, Petición 2 → servidor2, Petición 3 → servidor1, y así sucesivamente.

### ¿Qué es un volumen en Docker?
Un mecanismo para **persistir datos fuera del contenedor**. Si el contenedor se elimina, los datos del volumen sobreviven. También permite que múltiples contenedores compartan el mismo archivo (como la base de datos en esta tarea).

---

# Comandos utilizados y organizados por cada fase desarrollada en la Tarea

### Entorno de desarrollo local (Python)

```powershell
# Verificar versión de Python instalada
python --version

# Crear un entorno virtual (aísla las dependencias del proyecto)
python -m venv venv

# Activar el entorno virtual en Windows
venv\Scripts\activate

# Instalar Flask dentro del entorno virtual
pip install flask

# Guardar las dependencias instaladas en un archivo
# (Docker usará este archivo para instalar lo mismo dentro del contenedor)
pip freeze > requirements.txt

# Correr la app Flask directamente (fuera de Docker)
python app.py

# Crear la base de datos y poblarla con las 24 provincias
python init_db.py
```

---

### Docker — imágenes y contenedores

```powershell
# Verificar que Docker está instalado y corriendo
docker --version

# Verificar que Docker Compose está disponible
docker compose version

# Construir una imagen Docker desde el Dockerfile de la carpeta actual
# -t le da un nombre (tag) a la imagen
docker build -t provincias-app .

# Correr un contenedor a partir de una imagen
# -p 5000:5000 → mapea el puerto del host al del contenedor
docker run -p 5000:5000 provincias-app

# Ver las imágenes que tienes construidas localmente
docker images

# Ver los contenedores que están corriendo ahora mismo
docker ps

# Ver todos los contenedores (incluyendo los detenidos)
docker ps -a

# Detener un contenedor por su ID o nombre
docker stop <container_id>

# Eliminar un contenedor detenido
docker rm <container_id>

# Eliminar una imagen
docker rmi provincias-app
```

---

### Docker Compose — orquestación de múltiples contenedores

```powershell
# Levantar todos los servicios definidos en docker-compose.yml
# --build → reconstruye las imágenes si el código cambió
docker compose up --build

# Levantar en modo "detached" (en segundo plano, libera la terminal)
docker compose up --build -d

# Ver el estado de los contenedores del compose activo
docker compose ps

# Ver los logs de todos los servicios en tiempo real
docker compose logs -f

# Ver los logs de un servicio específico
docker compose logs -f server1

# Detener y eliminar los contenedores Y la red creada por compose
docker compose down

# Detener y eliminar contenedores + eliminar también los volúmenes
docker compose down -v
```

---

### Verificar el balanceador de carga Nginx

```
# Abre http://localhost (sin puerto ya que se usa el 80 por defecto) y refresca varias veces la pagina. Se debe observar el badge alternar entre "Atendido por: servidor1" y "Atendido por: servidor2".
```
## o desde la CLI

```powershell
# Hacer 6 peticiones seguidas y mostrar qué servidor respondió cada vez
# Deberías ver alternar servidor1 / servidor2 en round-robin
1..6 | ForEach-Object {
    (Invoke-WebRequest -Uri http://localhost -UseBasicParsing).Content |
    Select-String "Atendido por"
}
```

---

## Archivos importantes 
# Qué hace cada uno en este caso?

### `app/app.py`
```python
from flask import Flask, render_template
import sqlite3, socket, os

app = Flask(__name__)
DB_PATH = os.path.join(os.path.dirname(__file__), "provincias.db")

def obtener_provincias():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row          # Acceso a columnas por nombre
    cursor = conn.cursor()
    cursor.execute("SELECT nombre, capital, area_km2, poblacion, latitud, longitud FROM provincias")
    filas = cursor.fetchall()
    conn.close()
    return [dict(fila) for fila in filas]  # Convierte a lista de diccionarios

@app.route("/")
def inicio():
    provincias = obtener_provincias()
    servidor = socket.gethostname()        # Obtiene el hostname del contenedor
    return render_template("index.html", provincias=provincias, servidor=servidor)

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=5000)
    # host="0.0.0.0" → escucha en todas las interfaces (obligatorio dentro de Docker)
```

---

### `app/Dockerfile`
```dockerfile
FROM python:3.12-slim
# Imagen base con Python ya instalado. "slim" = versión liviana.

WORKDIR /app
# Crea y fija /app como directorio de trabajo dentro del contenedor.

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
# Se copian las dependencias PRIMERO para aprovechar la caché de capas de Docker.
# Si el código cambia pero requirements.txt no, esta capa no se reconstruye.

COPY . .
# Copia el resto del código al contenedor.

EXPOSE 5000
# Documenta el puerto que usa la app (no lo abre por sí solo).

CMD ["python", "app.py"]
# Comando que se ejecuta cuando el contenedor arranca.
```

---

### `nginx/nginx.conf`
```nginx
events {}

http {
    upstream flask_servers {
        server servidor1:5000;   # Docker resuelve "servidor1" por hostname interno
        server servidor2:5000;   # Round-robin por defecto entre estos dos
    }

    server {
        listen 80;               # Nginx escucha en el puerto 80 (HTTP estándar)

        location / {
            proxy_pass http://flask_servers;        # Reenvía al upstream
            proxy_set_header Host $host;            # Preserva el host original
            proxy_set_header X-Real-IP $remote_addr; # Preserva la IP del cliente
        }
    }
}
```

**Algoritmos de balanceo disponibles en Nginx:**
- (ninguno / por defecto) → **round-robin** — turnos rotativos entre servidores
- `least_conn` → envía al servidor con menos conexiones activas
- `ip_hash` → el mismo cliente siempre cae al mismo servidor

---

### `docker-compose.yml`
```yaml
services:
  server1:
    build: ./app             # Construye la imagen desde ese Dockerfile
    container_name: servidor1
    hostname: servidor1      # Nombre que socket.gethostname() devolverá
    volumes:
      - ./app/provincias.db:/app/provincias.db:ro
      # Monta el archivo local dentro del contenedor (:ro = solo lectura)
    networks:
      - balanceador-net      # Red interna — los contenedores se ven entre sí

  server2:
    build: ./app
    container_name: servidor2
    hostname: servidor2
    volumes:
      - ./app/provincias.db:/app/provincias.db:ro
    networks:
      - balanceador-net

  nginx:
    image: nginx:alpine      # Imagen oficial de Nginx (no necesita Dockerfile propio)
    container_name: balanceador
    ports:
      - "80:80"              # ÚNICO puerto expuesto al exterior
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
    depends_on:
      - server1              # Arranca después de server1 y server2
      - server2
    networks:
      - balanceador-net

networks:
  balanceador-net:
    driver: bridge           # Red privada virtual entre los contenedores
```

---

# Modelo de la Base de datos en SQLite

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | INTEGER PK | Identificador único, autoincremental |
| `nombre` | TEXT | Nombre de la provincia |
| `capital` | TEXT | Ciudad capital de la provincia |
| `area_km2` | REAL | Superficie en kilómetros cuadrados |
| `poblacion` | INTEGER | Población estimada |
| `latitud` | REAL | Coordenada geográfica para el marcador en el mapa |
| `longitud` | REAL | Coordenada geográfica para el marcador en el mapa |

---

## Flujo completo de una petición

```
1. El usuario abre http://localhost en el navegador
2. La petición llega a Nginx (puerto 80)
3. Nginx decide — según round-robin — a cuál Flask enviarla
4. Flask ejecuta la función inicio()
5. Consulta provincias.db y obtiene las 24 filas
6. socket.gethostname() devuelve "servidor1" o "servidor2"
7. render_template() combina los datos con index.html
8. El HTML resultante viaja de vuelta al navegador
9. El navegador carga Leaflet y dibuja los marcadores en el mapa
```

---

## Errores frecuentes y soluciones

| Error | Causa | Solución |
|---|---|---|
| `unknown directive "//"` en nginx.conf | Nginx no acepta `//` como comentario | Usar `#` o eliminar el comentario |
| `ERR_CONNECTION_REFUSED` en localhost | Nginx no arrancó (revisa sus logs) | `docker compose logs nginx` para ver el error |
| El badge siempre dice el mismo servidor | El navegador reutiliza la conexión | Usar el loop de PowerShell para probar |
| `provincias.db` no encontrado en el contenedor | El archivo no existe antes del `docker compose up` | Correr `python init_db.py` primero |
| `venv\Scripts\activate` bloqueado | PowerShell tiene restricción de scripts | `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned` |

---

---

## APIs y fuentes de datos utilizadas

### 🗺️ Leaflet.js — Librería de mapas interactivos

**Sitio oficial:** https://leafletjs.com  
**Versión usada:** 1.9.4  
**Tipo:** Open Source — sin API Key, sin registro, sin costo

Leaflet es una librería JavaScript de código abierto para mapas interactivos. Se integra directamente en HTML mediante etiquetas `<script>` y `<link>`, sin necesidad de crear cuentas ni gestionar claves de acceso.

Se usó para:
- Renderizar el mapa centrado en Ecuador
- Colocar un marcador (`L.marker`) por cada provincia
- Mostrar un popup con los datos de la provincia al hacer clic en el marcador

Cómo se incluyó en el proyecto (sin instalación, directo desde CDN):
```html
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
```

Fragmento de uso en `index.html`:
```javascript
const map = L.map('map').setView([-1.83, -78.18], 7); // Centro de Ecuador, zoom 7

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

L.marker([latitud, longitud])
    .addTo(map)
    .bindPopup(`<b>Provincia</b><br>Capital: ...`);
```

---

### 🌍 OpenStreetMap — Proveedor de teselas (tiles) del mapa

**Sitio oficial:** https://www.openstreetmap.org  
**Tipo:** Datos geográficos abiertos — sin API Key, sin costo

OpenStreetMap (OSM) es un proyecto colaborativo que proporciona cartografía libre del mundo. Leaflet lo usa como proveedor de las imágenes del mapa (llamadas "tiles" o teselas) — cada pequeña imagen cuadrada que forma el mapa visible en pantalla proviene de los servidores de OSM.

No requiere ninguna configuración adicional más allá de la URL del tile layer ya incluida en el código de Leaflet.

> **Nota:** A diferencia de Google Maps, OpenStreetMap no requiere tarjeta de crédito, cuenta de desarrollador ni API Key de ningún tipo, lo que lo hace ideal para proyectos académicos.

---

## Datos de las provincias del Ecuador

**Fuente de referencia:** Instituto Nacional de Estadística y Censos del Ecuador (INEC)  
**Sitio oficial:** https://www.ecuadorencifras.gob.ec  

Los datos almacenados en la base de datos `provincias.db` corresponden a las **24 provincias del Ecuador** e incluyen:

| Campo | Fuente |
|---|---|
| Nombre y capital | División político-administrativa oficial del Ecuador |
| Área (km²) | Datos territoriales de referencia pública |
| Población | Proyecciones referenciales basadas en datos del INEC |
| Latitud y longitud | Coordenadas geográficas de las capitales provinciales |

> Los valores de población y área son de referencia para fines académicos y demostrativos. Para datos oficiales actualizados consultar directamente el portal del INEC: https://www.ecuadorencifras.gob.ec/estadisticas/

*Tarea#3 · EPN 2026-A*
*María Girón -- Tecnología Superior en Desarrollo de Software*
