# 📋 Informe del Proyecto: Calculadora Cliente-Servidor UDP

---

## 1. Introducción

### ¿Qué es esta práctica?
Esta práctica consiste en desarrollar una **aplicación de calculadora** que funcione bajo una arquitectura **cliente-servidor** utilizando el protocolo **UDP** (User Datagram Protocol). A diferencia de una calculadora normal donde todo se hace en la misma computadora, aquí el cálculo lo realiza un programa separado (el servidor) al que se le envían los datos a través de la red.

### Objetivo
El objetivo principal es comprender cómo se comunican dos aplicaciones independientes mediante sockets UDP, donde:
- El **cliente** (interfaz gráfica) captura los datos del usuario y los envía
- El **servidor** (lógica de negocio) recibe, procesa y devuelve el resultado

### Tecnologías utilizadas
- **Java 17+**: Lenguaje de programación
- **JavaFX**: Para la interfaz gráfica (FXML)
- **UDP**: Protocolo de comunicación sin conexión
- **IntelliJ IDEA**: IDE de desarrollo

---

## 2. Integración de la lógica del servidor con el cliente y la vista FXML

### Arquitectura implementada

```
┌─────────────────────────────────────────────────────────────┐
│                        CLIENTE (Este proyecto)              │
├─────────────────────────────────────────────────────────────┤
│  Vista (FXML)        →   Controlador   →   Cliente UDP     │
│  vistaCalculadora    ←   ClienteCtrl   ←   (Socket)        │
│  .fxml               ←   @FXML methods  ←                  │
└─────────────────────────────────────────────────────────────┘
                            │
                            │ UDP (puerto 5000)
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                       SERVIDOR (Proyecto aparte)            │
├─────────────────────────────────────────────────────────────┤
│                      Server + Modelo                       │
│  (Recibe) ← Datos: "n1,n2,operador" → (Procesa) → (Envía) │
└─────────────────────────────────────────────────────────────┘
```

### Paso a paso de la integración

#### **Paso 1: La Vista (FXML)**
- Archivo: `src/vista/vistaCalculadora.fxml`
- Define la interfaz: 2 campos de texto, 1 campo de resultado, 5 botones
- **Cambio clave**: Se modificó `fx:controller` para que apunte a `controller.ClienteController` (antes apuntaba a `entidades.Cliente` que no es un controlador)

#### **Paso 2: El Controlador (`ClienteController.java`)**
- Recibe los eventos de los botones (`@FXML public void sumar()`)
- **NO contiene la lógica de cálculo** (eso está en el servidor)
- Su trabajo:
  1. Leer los números de los `TextField`
  2. Llamar a `cliente.enviar(IP, puerto, n1, n2, operador)`
  3. Recibir el resultado y colocarlo en `txtResultado`

#### **Paso 3: El Cliente UDP (`Cliente.java`)**
- Establece la comunicación real con el servidor
- **Protocolo de comunicación**:
  - Envía: `String datos = n1 + "," + n2 + "," + operador` (ej: `"10,5,+"`)
  - Recibe: `String resultado` (ej: `"50.0"`)
- Usa `DatagramSocket` para enviar/recibir paquetes UDP
- **Timeouts**: Configurado `socket.setSoTimeout(5000)` para esperar máximo 5 segundos la respuesta

#### **Paso 4: El Servidor (proyecto separado)**
- Clase `Server` con método `operar(int puerto)`
- Escucha en puerto 5000
- Recibe el string `"n1,n2,operador"`
- Usa `Modelo` para calcular: `modelo.sumar(n1, n2)`, etc.
- Devuelve el resultado como string

### Comunicación entre componentes

```
Usuario hace clic en "Sumar"
        ↓
ClienteController.sumar() es invocado
        ↓
Lee txtNumero1.getText() y txtNumero2.getText()
        ↓
cliente.enviar("localhost", 5000, 10.0, 5.0, "+")
        ↓
Cliente.java crea DatagramSocket
        ↓
Empaqueta: "10.0,5.0,+" en bytes
        ↓
socket.send(packet) → RED UDP
        ↓
Server.operar() recibe el paquete
        ↓
Modelo.sumar(10.0, 5.0) → 15.0
        ↓
Convierte a string "15.0"
        ↓
socket.send(respuesta) → RED UDP
        ↓
Cliente.java recibe el paquete (con timeout de 5s)
        ↓
Convierte bytes a String: "15.0"
        ↓
Devuelve "15.0" a ClienteController
        ↓
txtResultado.setText("15.0")
```

---

## 3. Resultados de la ejecución

### **Escenario A: Servidor CORRIENDO** ✅

**Preparación**:
1. Se ejecuta `java Server` en consola
2. Consola del servidor muestra: `"Servidor conectado y ejecutado en el puerto 5000"`

**Ejecución del cliente**:
1. Se ejecuta `AppCliente.main()` en IntelliJ
2. Se abre la ventana de la calculadora

**Prueba**:
- Usuario ingresa: `25` y `4`
- Haz clic en **"Multiplicar"**
- **Resultado en pantalla**: `100.0`
- En la consola del servidor aparece: `Operación: * | 25.0 y 4.0 = 100.0`

**Observaciones**:
- ✅ Respuesta inmediata (milisegundos)
- ✅ Resultado correcto
- ✅ No hay errores
- ✅ Comunicación UDP exitosa
- ✅ Timeout configurado (5s) pero no se activa porque el servidor responde rápido

---

### **Escenario B: Servidor NO corriendo** ❌

**Preparación**:
- Solo se ejecuta el cliente (`AppCliente`)
- El servidor **NO** está activo

**Ejecución**:
1. Cliente se abre normalmente
2. Usuario ingresa números: `10` y `5`
3. Haz clic en **"Sumar"**

**Comportamiento CON timeout**:
- La aplicación espera **5 segundos** (timeout configurado)
- Después de 5 segundos, muestra en pantalla: `"Error: Servidor no disponible (timeout)"`
- La aplicación **no se bloquea eternamente**, se recupera automáticamente

**Observaciones**:
- ✅ Mensaje de error claro para el usuario
- ✅ La aplicación sigue siendo usable después del error
- ✅ No requiere intervención manual para cerrar
- ⚠️ El timeout de 5 segundos puede parecer largo, pero es suficiente para redes locales

---

### **Posibles mejoras adicionales**

1. **Timeout configurable**: Permitir ajustar el tiempo de espera
2. **Indicador visual**: Mostrar "Conectando..." mientras se espera
3. **Reintento automático**: Intentar enviar de nuevo si falla
4. **Modo offline**: Calcular localmente si el servidor no está disponible

---

## 📊 Conclusión

- ✅ **Funcionamiento correcto** cuando servidor está activo
- ✅ **Manejo de error mejorado** con timeout de 5 segundos
- ✅ **Arquitectura limpia**: separación de responsabilidades (Vista-Controlador-Cliente-Servidor)
- ✅ **Protocolo UDP** funciona adecuadamente para este caso de uso simple
- ✅ **Experiencia de usuario**: No se bloquea eternamente, muestra mensaje de error
- 📚 **Documentación completa** en README.md e INFORME.md

---

**Fin del informe**
