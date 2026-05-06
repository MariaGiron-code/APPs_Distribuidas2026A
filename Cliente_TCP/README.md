# Cliente TCP — Sistema de Timbres

Aplicación JavaFX que permite a los empleados registrar sus marcaciones de asistencia diaria conectándose al servidor TCP.

## ¿Qué hace?

- Presenta un formulario gráfico para registrar los 4 timbres del día
- Envía los datos al servidor a través de una conexión TCP
- Muestra la respuesta del servidor en pantalla (fecha/hora del timbre o lista de registros)

## Estructura del proyecto

```
src/
├── clases/
│   └── Cliente.java              ← maneja la conexión TCP con el servidor
├── controladores/
│   └── FormController.java       ← controller del formulario JavaFX
├── vista/
│   └── vistaForm.fxml            ← diseño de la interfaz gráfica
└── test/
    └── TestCliente.java          ← clase main para lanzar la aplicación
```

## Cómo ejecutar

1. Asegúrate de que el **servidor esté corriendo** antes de abrir el cliente
2. Corre `TestCliente.java` como aplicación Java — se abre la ventana del formulario

## Uso del formulario

1. Escribe tu nombre en el campo de texto
2. Presiona el botón del timbre que corresponde al momento del día
3. El resultado aparece en la parte inferior de la ventana

| Botón              | Timbre que registra  |
|--------------------|----------------------|
| Ingreso            | Entrada al trabajo   |
| Salida almuerzo    | Salida a almorzar    |
| Entrada almuerzo   | Regreso del almuerzo |
| Salida             | Fin de jornada       |

El botón **"Consultar mis timbres del día"** muestra todos los timbres registrados para el nombre ingresado.

## Configuración de conexión

La IP y el puerto del servidor están definidos en `Cliente.java`:

```java
private static final String IP_SERVIDOR = "172.31.116.72";
private static final int PUERTO = 2025;
```

Cambia `IP_SERVIDOR` si el servidor corre en otra máquina.

## Tecnologías

- Java SE + JavaFX
- `Socket`, `DataInputStream`, `DataOutputStream`
- FXML para la interfaz gráfica
