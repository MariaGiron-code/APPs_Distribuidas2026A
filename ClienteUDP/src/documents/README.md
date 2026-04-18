# Calculadora Cliente-Servidor UDP

## Descripcion

Aplicacion de calculadora que utiliza arquitectura cliente-servidor con protocolo UDP. La interfaz grafica envia operaciones a un servidor separado, quien realiza el calculo y devuelve el resultado.

## Estructura del proyecto

```
ClientUDP/
├── src/
│   ├── controller/
│   │   ├── AppCliente.java          # Clase principal (ejecutable)
│   │   └── ClienteController.java   # Controlador de la vista
│   ├── entidades/
│   │   └── Cliente.java             # Logica de comunicacion UDP
│   ├── vista/
│   │   └── vistaCalculadora.fxml    # Interfaz grafica
│   └── test/
│       └── Test.java                # Cliente de consola (opcional)
└── README.md
```

## Componentes

### Cliente (este proyecto)
- **Vista**: Archivo FXML con campos de texto y botones
- **Controlador**: Maneja eventos de interfaz y coordina el envio de datos
- **Comunicacion**: Clase Cliente que implementa socket UDP

### Servidor (proyecto separado)
El servidor debe implementarse en un proyecto independiente con:
- Clase Server que escuche en puerto 5000
- Clase Modelo con las operaciones matematicas
- Logica para recibir datos, calcular y enviar respuesta

## Instrucciones de ejecucion

### Requisitos
- Java JDK 17 o superior
- IntelliJ IDEA (recomendado)
- JavaFX SDK (ya configurado en el proyecto)
- Servidor UDP ejecutandose en puerto 5000

### Pasos

1. **Compilar**:
   - Abrir proyecto en IntelliJ
   - Build -> Build Project

2. **Ejecutar servidor** (proyecto separado):
   ```
   java Server
   ```
   El servidor debe mostrar: "Servidor conectado y ejecutado en el puerto 5000"

3. **Ejecutar cliente** (este proyecto):
   - En IntelliJ, clic derecho en AppCliente.java
   - Seleccionar "Run 'AppCliente.main()'"
   - Se abrira la ventana de la calculadora

4. **Usar la aplicacion**:
   - Ingresar dos numeros
   - Seleccionar operacion (Sumar, Restar, Multiplicar, Dividir)
   - El resultado se muestra automaticamente

## Configuracion

### Puerto de comunicacion
Valor: 5000 (definido en ClienteController.java)
```java
private final int SERVER_PUERTO = 5000;
```

### IP del servidor
Valor por defecto: localhost
```java
private final String SERVER_IP = "localhost";
```
Si el servidor esta en otra maquina, cambiar esta IP.

### Timeout
El cliente espera maximo 5 segundos la respuesta del servidor.
Si no hay respuesta, muestra: "Error: Servidor no disponible (timeout)"

## Protocolo de comunicacion

Formato de mensaje: numero1,numero2,operador

Ejemplos:
- "10,5,+" -> resultado esperado: 15.0
- "25,4,*" -> resultado esperado: 100.0
- "20,5,-" -> resultado esperado: 15.0
- "30,6,/" -> resultado esperado: 5.0

Operadores soportados: +, -, *, /

## Solucion de problemas

### La aplicacion se bloquea
- Causa: Servidor no esta ejecutandose
- Solucion: Iniciar el servidor antes que el cliente

### Error "método principal no encontrado"
- Causa: Se esta ejecutando la clase incorrecta
- Solucion: Ejecutar AppCliente.java, no ClienteController.java

### No se puede conectar al servidor
- Verificar que el servidor este activo
- Verificar que el puerto 5000 este libre
- Verificar que la IP sea correcta

## Notas tecnicas

- Protocolo: UDP (sin conexion, no garantiza entrega)
- Lenguaje: Java 17+
- GUI: JavaFX con FXML
- Patron: MVC (Modelo-Vista-Controlador) simplificado
