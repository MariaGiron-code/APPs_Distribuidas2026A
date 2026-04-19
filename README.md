# Apps Distribuidas 2026A

Proyecto de aplicaciones distribuidas con comunicación UDP y JavaFX.

## Estructura del Proyecto

```
APPs_Distribuidas2026A/
├── AppQuestion/          # Aplicación de preguntas con servidor UDP y UI JavaFX
├── ClienteUDP/           # Cliente UDP genérico
├── ServerUDP/            # Servidor UDP genérico
├── .gitignore            # Archivos ignorados por Git
└── README.md             # Este archivo
```

## Proyectos Incluidos

### 1. AppQuestion
Aplicación de cuestionario cliente-servidor con interfaz JavaFX.

**Características:**
- Servidor UDP que envía preguntas de opción múltiple
- Cliente con interfaz gráfica JavaFX
- 5 preguntas sobre protocolos de red
- Validación de respuestas en tiempo real

**Tecnologías:** Java, JavaFX, UDP

### 2. ClienteUDP
Cliente UDP genérico para comunicación con servidores.

### 3. ServerUDP
Servidor UDP genérico para recibir y procesar solicitudes.

## Cómo Ejecutar

### AppQuestion

**Iniciar el servidor:**
```bash
cd AppQuestion/src
java Test.RunServer
```

**Iniciar la interfaz:**
```bash
cd AppQuestion/src
java Main
```

## Requisitos

- Java JDK 8 o superior
- JavaFX SDK (para AppQuestion)
- IntelliJ IDEA (opcional)

## Autores

María Girón - EPN 2026A
