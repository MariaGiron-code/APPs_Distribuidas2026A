# Servidor TCP — Sistema de Timbres

Servidor Java que recibe y almacena las marcaciones de asistencia de los empleados a través de una conexión TCP.

## ¿Qué hace?

- Escucha conexiones en el **puerto 2025**
- Registra hasta 4 timbres por empleado por día
- Consulta los timbres registrados de un empleado
- Guarda los datos **en memoria** (se pierden al reiniciar)

## Estructura del proyecto

```
src/
├── clases/
│   ├── Servidor.java        ← lógica principal del servidor
│   └── RegistroTimbre.java  ← clase de datos de un timbre
└── test/
    └── TestServidor.java    ← clase main para arrancar el servidor
```

## Cómo ejecutar

Corre `TestServidor.java` como aplicación Java. El servidor queda escuchando:

```
Servidor corriendo en el puerto: 2025
```

Para detenerlo, el cliente debe enviar la cadena `"x"` como operación.

## Protocolo de comunicación

El servidor espera que el cliente envíe los mensajes en este orden:

**Registrar timbre:**
```
Cliente → "TIMBRE"
Cliente → nombre del empleado
Cliente → tipo de timbre ("1", "2", "3" o "4")
Servidor → fecha/hora del registro  (ej: "05/05/2026 08:01:23")
```

**Consultar timbres:**
```
Cliente → "GET"
Cliente → nombre del empleado
Servidor → lista de timbres del día (o mensaje si no hay registros)
```

## Tipos de timbre

| Código | Descripción       |
|--------|-------------------|
| `1`    | Ingreso           |
| `2`    | Salida almuerzo   |
| `3`    | Entrada almuerzo  |
| `4`    | Salida            |

## Validaciones

- Tipo de timbre inválido → responde `"Tipo de timbre inválido"`
- Empleado sin timbres en consulta → responde `"No ha realizado ningún timbre durante el día"`

## Tecnologías

- Java SE (sin dependencias externas)
- `ServerSocket` / `Socket`
- `DataInputStream` / `DataOutputStream`
- `HashMap` + `ArrayList` para almacenamiento en memoria
