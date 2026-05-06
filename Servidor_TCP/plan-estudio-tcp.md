# 📡 Plan de Estudio: Servidores TCP en Java

> **Para:** Estudiante de Desarrollo de Software  
> **Práctica:** Comunicación Cliente-Servidor con TCP  
> **Proyectos:** `Servidor_TCP` · `Cliente_TCP`

---

## 🗺️ Visión General del Recorrido

```
BLOQUE 1          BLOQUE 2          BLOQUE 3          BLOQUE 4          BLOQUE 5
Fundamentos  ──►  Clases Java  ──►  Servidor.java ──►  Cliente.java ──►  Flujo Completo
de Redes          de la práctica    línea a línea       línea a línea     + Mejoras
```

---

## 🟦 Bloque 1 — Fundamentos de Redes

> *La base teórica. Antes de tocar código, necesitas entender por qué existe.*

### 1.1 ¿Cómo se comunican dos computadoras?
- Qué es una **dirección IP** y para qué sirve
- Qué es un **puerto** y por qué los programas los usan
- Analogía: IP = dirección de una casa · Puerto = número de apartamento

### 1.2 ¿Qué es el protocolo TCP?
- Qué significa **TCP** *(Transmission Control Protocol)*
- Diferencia entre **TCP** y **UDP** — conexión confiable vs. no confiable
- Cómo funciona la conexión: el **handshake de 3 pasos**

### 1.3 Modelo Cliente-Servidor
- Qué es un **servidor** y qué es un **cliente**
- Quién espera y quién inicia la comunicación
- Ejemplos del mundo real: navegador web, WhatsApp, correo electrónico

---

## 🟩 Bloque 2 — Clases de Java Usadas en la Práctica

> *Conectamos la teoría con las herramientas concretas del lenguaje.*

### 2.1 `Socket` — El enchufe del cliente
```java
Socket socket = new Socket("172.31.116.72", 2025);
```
- Qué es un socket *(el "enchufe" de la comunicación)*
- Qué pasa exactamente cuando se ejecuta esa línea
- Parámetros: IP del servidor + número de puerto

### 2.2 `ServerSocket` — El enchufe del servidor
```java
ServerSocket servidor = new ServerSocket(puerto);
Socket cliente = servidor.accept(); // ← el programa se pausa aquí
```
- Diferencia entre `Socket` y `ServerSocket`
- Qué hace `accept()` y por qué el servidor "espera" ahí
- Por qué el `while(true)` mantiene el servidor siempre activo

### 2.3 Streams: `InputStream` y `OutputStream`
```java
InputStream in   = socket.getInputStream();
OutputStream out = socket.getOutputStream();
```
- Qué es un **stream** *(flujo de datos)*
- Por qué necesitamos uno para leer y otro para escribir
- Cómo se obtienen desde un socket

### 2.4 `DataInputStream` y `DataOutputStream`
```java
DataOutputStream dos = new DataOutputStream(out);
dos.writeUTF("texto");

DataInputStream dis = new DataInputStream(in);
String texto = dis.readUTF();
```
- Por qué no usamos `InputStream` directamente
- Qué hacen `writeUTF()` y `readUTF()`
- Qué significa **UTF** y por qué importa para texto en español

---

## 🟨 Bloque 3 — Análisis de `Servidor.java`

> *Diseccionamos el servidor línea por línea.*

### 3.1 El método `getFecha()`
```java
public String getFecha(String nombre) {
    Date fecha = new Date();
    DateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    return formato.format(fecha);
}
```
- Qué hace `new Date()` — captura el momento actual
- Qué es `SimpleDateFormat` y cómo define el patrón de formato
- Por qué este método está separado del resto de la lógica

### 3.2 El método `servicio(int puerto)`
- Por qué recibe el puerto como parámetro y no lo tiene fijo
- Qué significa `throws Exception` en la firma del método
- El flujo completo paso a paso:

```
1. Crear ServerSocket  →  2. Esperar cliente (accept)  →  3. Leer mensaje
        ↑                                                         ↓
6. Volver a esperar   ←   5. Cerrar conexión cliente   ←  4. Enviar respuesta
```

### 3.3 Ciclo de vida de una conexión
- Qué pasa internamente cuando llega un cliente
- Por qué se cierra `cliente.close()` pero **no** `servidor.close()` dentro del loop
- La condición de salida: `if(nombre.equals("x")) break` — cómo se detiene el servidor

---

## 🟧 Bloque 4 — Análisis de `Cliente.java`

> *Entendemos el lado del cliente y encontramos un detalle importante en el código.*

### 4.1 El método `enviar(String empleado)`
```java
public void enviar(String empleado) throws Exception {
    Socket socket = new Socket("172.31.116.72", 2025);
    // ...
}
```
- Por qué se conecta a una IP específica y al puerto `2025`
- El orden de operaciones y **por qué el orden importa**:

| Paso | Acción | Si lo cambias... |
|------|--------|-----------------|
| 1 | Conectar al servidor | El programa falla de entrada |
| 2 | Enviar el nombre | El servidor queda esperando |
| 3 | Recibir la fecha | Leerías datos vacíos |
| 4 | Cerrar el socket | Fuga de recursos |

### 4.2 🔍 Detalle a analizar juntos
```java
// El método recibe "empleado" como parámetro...
public void enviar(String empleado)

// ...pero envía siempre el mismo texto fijo
dos.writeUTF("Odaliz Balseca");  // ← ¿no debería ser "empleado"?
```
- Identificaremos este comportamiento y entenderemos qué debería hacer el código correcto

---

## 🟥 Bloque 5 — El Flujo Completo y Mejoras

> *Unimos todo y vemos el panorama completo de la práctica.*

### 5.1 Diagrama de la comunicación completa

```
CLIENTE                              SERVIDOR
  │                                     │
  │  new Socket("ip", 2025)             │  ServerSocket.accept()
  │ ──────── CONECTAR ─────────────────►│
  │                                     │
  │  dos.writeUTF("Odaliz Balseca")     │  dis.readUTF()
  │ ──────── ENVIAR NOMBRE ────────────►│
  │                                     │  getFecha() → "05/05/2026 10:30:00"
  │  dis.readUTF()                      │  dos.writeUTF(resultado)
  │ ◄──────── RECIBIR FECHA ────────────│
  │                                     │
  │  socket.close()                     │  cliente.close()
  │ ──────── CERRAR ────────────────────│
```

### 5.2 Manejo de errores con `throws` y `try-catch`
- Por qué casi todo en redes puede lanzar excepciones
- Tipos de errores comunes:
  - Servidor no disponible → `ConnectException`
  - Puerto ya ocupado → `BindException`
  - Conexión interrumpida → `SocketException`
- Diferencia entre declarar `throws` y manejar con `try-catch`

### 5.3 Buenas prácticas y mejoras posibles
- **Try-with-resources** — la forma moderna de cerrar sockets automáticamente
- **Servidores multi-hilo** — para atender varios clientes al mismo tiempo
- **Constantes para IP y puerto** — en lugar de valores fijos en el código
- **Validación de datos** — qué pasa si el cliente envía datos inesperados

---

## 📋 Resumen de Temas por Bloque

| Bloque | Tema Principal | Tipo |
|--------|---------------|------|
| 🟦 1 | Redes, TCP, IP, Puertos, Cliente-Servidor | Teoría |
| 🟩 2 | Socket, ServerSocket, Streams, DataStreams | Clases Java |
| 🟨 3 | Análisis completo de `Servidor.java` | Código |
| 🟧 4 | Análisis completo de `Cliente.java` | Código |
| 🟥 5 | Flujo completo, errores y mejoras | Integración |

---

## ⏱️ Estimado de Estudio

| Bloque | Dificultad | Tiempo estimado |
|--------|-----------|----------------|
| Bloque 1 | ⭐ Básico | 20–30 min |
| Bloque 2 | ⭐⭐ Medio | 30–40 min |
| Bloque 3 | ⭐⭐ Medio | 30–40 min |
| Bloque 4 | ⭐⭐ Medio | 20–30 min |
| Bloque 5 | ⭐⭐⭐ Avanzado | 30–40 min |

---

> 💡 **Consejo:** No avances al siguiente bloque sin entender el anterior.  
> Cada concepto construye sobre el que viene antes.  
> ¡Pregunta todo lo que no entiendas!
