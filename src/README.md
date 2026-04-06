# Calculadora Básica con JavaFX

Este proyecto es una práctica de introducción a las aplicaciones de escritorio en Java usando JavaFX, desarrollado como parte del curso de Aplicaciones Distribuidas.

## Vista General del Proyecto

Esta es una calculadora de escritorio con cuatro operaciones básicas (suma, resta, multiplicación y división), construida con **Java** y **JavaFX**. La arquitectura sigue el patrón MVC con las siguientes capas:

| Clase / Archivo          | Responsabilidad                                |
|--------------------------|------------------------------------------------|
| `Main`                  | Punto de entrada y configuración de la ventana |
| `Controlador`           | Manejo de eventos de la UI                    |
| `Modelo`                | Lógica de negocio (operaciones matemáticas)    |
| `vistaCalculadora.fxml` | Definición declarativa de la interfaz gráfica  |

---

## Características Implementadas

- **Suma, resta, multiplicación y división:** Las cuatro operaciones están completamente implementadas y conectadas a sus respectivos botones en la UI.
- **Manejo de errores:** Se valida que los inputs sean números válidos y se controla la división por cero.
- **Botón de limpieza:** Resetea ambos campos de texto y el campo de resultado con un solo clic.

---

## Cómo Instalar

### Prerrequisitos

Antes de empezar, asegúrate de tener instalado lo siguiente:

| Herramienta                                    | Versión recomendada |
|-----------------------------------------------|---------------------|
| [JDK](https://www.oracle.com/java/technologies/downloads/) | 11 o superior       |
| [JavaFX SDK](https://gluonhq.com/products/javafx/) | 11 o superior       |
| [IntelliJ IDEA](https://www.jetbrains.com/idea/) | Cualquier edición (Community o Ultimate) |
| [SceneBuilder](https://gluonhq.com/products/scene-builder/) | Compatible con JavaFX |

### Pasos

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/tu-repositorio.git
   cd Calculadora
   ```

2. **Configura el SDK en IntelliJ IDEA**
    - Ve a `File → Project Structure → SDKs`
    - Agrega el JDK instalado

3. **Agrega el módulo de JavaFX**
    - En `File → Project Structure → Libraries`, agrega la carpeta `lib` del JavaFX SDK descargado y descomprimido
    - Asegúrate de que los módulos necesarios estén en las VM Options:
   ```
   --module-path /ruta/a/javafx-sdk/lib
   --add-modules=javafx.controls,javafx.fxml
   ```

4. **Ejecuta la aplicación**
    - Corre la clase `Main`

---

**Casos de error contemplados:**

| Situación                        | Mensaje mostrado                  |
|----------------------------------|-----------------------------------|
| Texto no numérico en algún campo | `NumberFormatException`           |
| División por cero                | `Error: división por cero`        |