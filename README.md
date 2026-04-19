# AppQuestion - Aplicación de Preguntas

Aplicación de cliente-servidor UDP para un cuestionario de preguntas con interfaz JavaFX.

## Descripción

AppQuestion es una aplicación que permite a los usuarios responder preguntas de opción múltiple. El servidor envía preguntas y el cliente las presenta en una interfaz gráfica, validando las respuestas del usuario.

## Estructura del Proyecto

```
AppQuestion/
├── src/
│   ├── Main.java                    # Punto de entrada JavaFX
│   ├── Server/
│   │   ├── Server.java              # Servidor UDP
│   │   └── Modelo.java              # Lógica de preguntas y respuestas
│   ├── Client/
│   │   ├── Controller/
│   │   │   └── UserController.java  # Controlador de la UI
│   │   ├── Entities/
│   │   │   └── User.java            # Cliente UDP
│   │   └── View/
│   │       └── VistaAppQ.fxml       # Interfaz JavaFX
│   └── Test/
│       └── RunServer.java           # Ejecutable del servidor
├── AppQuestion.iml                  # Configuración IntelliJ
└── .gitignore                       # Archivos ignorados por Git
```

## Requisitos

- Java JDK 8 o superior
- JavaFX SDK
- IntelliJ IDEA (opcional)

## Cómo Ejecutar

### 1. Iniciar el Servidor

Ejecutar desde el IDE o línea de comandos:
```bash
cd AppQuestion/src
java Test.RunServer
```

El servidor escuchará en el puerto 5000.

### 2. Iniciar la Interfaz Gráfica

En una terminal separada:
```bash
cd AppQuestion/src
java Main
```

O ejecutar la clase `Main` desde tu IDE.

## Preguntas del Cuestionario

| # | Pregunta | Respuesta Correcta |
|---|----------|-------------------|
| 1 | ¿Qué es UDP? | A |
| 2 | ¿Cuál es la principal función de una API? | B |
| 3 | ¿Qué es TCP? | A |
| 4 | ¿Cuántos bits tiene una dirección IPv4? | A |
| 5 | ¿Qué puerto usa HTTPS por defecto? | B |

## Protocolo de Comunicación

El servidor utiliza UDP con el siguiente formato de mensajes:

- **Pedir pregunta:** `PEDIR PREGUNTA`
- **Responder:** `RESPONDER|ID_Pregunta|Respuesta`
- **Respuesta del servidor:** `CORRECTO` o `INCORRECTO`

## Tecnologías Utilizadas

- Java
- JavaFX (UI)
- UDP (Comunicación cliente-servidor)
- IntelliJ IDEA

## Autores

Desarrollado por María Girón - EPN 