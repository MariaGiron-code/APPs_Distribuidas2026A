package controller;

import entidades.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Controlador de la vista de la calculadora cliente-servidor.
 * 
 * <p>Esta clase actúa como intermediaria entre la interfaz gráfica (definida en
 * vistaCalculadora.fxml) y la lógica de negocio (clase Cliente). Sigue el patrón
 * MVC (Modelo-Vista-Controlador) donde:</p>
 * 
 * <ul>
 *   <li><strong>Modelo:</strong> La clase Cliente (entidades/Cliente.java) que maneja la comunicación UDP</li>
 *   <li><strong>Vista:</strong> El archivo FXML (vistaCalculadora.fxml) creado con Scene Builder</li>
 *   <li><strong>Controlador:</strong> Esta clase que conecta la vista con el modelo</li>
 * </ul>
 * 
 * <h2>Integración con Scene Builder:</h2>
 * <p>Scene Builder es una herramienta visual de JavaFX que permite diseñar interfaces
 * gráficas arrastrando y soltando componentes. El archivo FXML es el XML generado por
 * Scene Builder. La integración funciona así:</p>
 * 
 * <ol>
 *   <li><strong>fx:controller</strong> (línea 13 del FXML): Especifica que esta clase
 *       (controller.ClienteController) es el controlador de la vista.</li>
 *   <li><strong>@FXML annotation</strong>: Marca los campos y métodos que pueden ser
 *       inyectados o invocados desde el FXML.</li>
 *   <li><strong>fx:id</strong> (FXML líneas 21, 24, 27): Asocia un componente de la
 *       interfaz con una variable del controlador. Por ejemplo, txtNumero1 en el FXML
 *       se enlaza con {@code @FXML private TextField txtNumero1}.</li>
 *   <li><strong>onAction</strong> (FXML líneas 29-33): Asocia un evento de clic de botón
 *       con un método del controlador. Por ejemplo, el botón "Sumar" llama al método
 *       {@code sumar()}.</li>
 * </ol>
 * 
 * <h2>Flujo de ejecución:</h2>
 * <ol>
 *   <li>AppCliente (la aplicación principal) carga el FXML y crea la interfaz.</li>
 *   <li>JavaFX inyecta automáticamente las referencias a los TextField (txtNumero1,
 *       txtNumero2, txtResultado) definidos en el FXML.</li>
 *   <li>Cuando el usuario hace clic en un botón, se invoca al método correspondiente
 *       (@FXML public void sumar(), restar(), etc.).</li>
 *   <li>Estos métodos llaman a enviarOperacion() que:</li>
 *   <ul>
 *     <li>Lee los valores de los TextField (que vienen de la entrada del usuario en la vista).</li>
 *     <li>Crea un objeto Cliente (la lógica de negocio).</li>
 *     <li>Invoca cliente.enviar() que envía los datos por UDP al servidor.</li>
 *     <li>Muestra el resultado en txtResultado.</li>
 *   </ul>
 * </ol>
 * 
 * <h2>Origen de la lógica:</h2>
 * <p>La lógica de comunicación UDP está en la clase {@link Cliente} (entidades/Cliente.java).
 * Esta clase:</p>
 * <ul>
 *   <li>Crea un DatagramSocket para enviar y recibir paquetes UDP.</li>
 *   <li>Convierte los números y operación en un string "n1,n2,operador".</li>
 *   <li>Envía el paquete al servidor en localhost:5000.</li>
 *   <li>Espera la respuesta (con timeout de 5 segundos).</li>
 *   <li>Retorna el resultado como String.</li>
 * </ul>
 * 
 * <p>El controlador solo se preocupa de la interfaz y la validación básica,
 * delegando la comunicación de red a la clase Cliente.</p>
 */
public class ClienteController {
    // ========================================================================
    // COMPONENTES DE LA VISTA (INYECTADOS DESDE FXML)
    // ========================================================================
    // Estos campos son inyectados automáticamente por JavaFX desde el FXML
    // cuando se usa fx:id="txtNumero1" en el archivo vistaCalculadora.fxml.
    // Scene Builder genera estos fx:id al nombrar los componentes en la vista.
    
    @FXML private TextField txtNumero1;   // Campo para el primer número (FXML línea 21: fx:id="txtNumero1")
    @FXML private TextField txtNumero2;   // Campo para el segundo número (FXML línea 24: fx:id="txtNumero2")
    @FXML private TextField txtResultado; // Campo de solo lectura para mostrar resultado (FXML línea 27: fx:id="txtResultado")
    
    // ========================================================================
    // CONFIGURACIÓN DE COMUNICACIÓN CON EL SERVIDOR
    // ========================================================================
    // Estos valores definen a dónde se enviarán las operaciones UDP.
    // SERVER_IP: "localhost" significa que el servidor debe estar en la misma máquina.
    // SERVER_PUERTO: 5000 es el puerto donde el servidor UDP escucha.
    // NOTA: En una aplicación real, estos valores deberían venir de un archivo de configuración.
    
    private final Cliente cliente = new Cliente(); // Lógica de comunicación UDP (ver entidades/Cliente.java)
    private final String SERVER_IP = "localhost";  // IP del servidor (definida por el desarrollador)
    private final int SERVER_PUERTO = 5000;        // Puerto del servidor (definida por el desarrollador)
    
    // ========================================================================
    // MÉTODOS DE EVENTOS (VINCULADOS CON LOS BOTONES EN FXML)
    // ========================================================================
    // Cada método @FXML es invocado cuando el usuario hace clic en un botón.
    // La vinculación se hace en el FXML con onAction="#nombreDelMetodo".
    // Ejemplo: onAction="#sumar" en el botón Sumar (FXML línea 29) llama a este método.
    // Scene Builder permite asignar estos eventos de forma visual en la pestaña "Code".
    
    @FXML
    public void sumar() {
        // Delega la operación al método genérico enviarOperacion con el operador "+"
        enviarOperacion("+");
    }
    
    @FXML
    public void restar() {
        // Delega la operación al método genérico enviarOperacion con el operador "-"
        enviarOperacion("-");
    }
    
    @FXML
    public void multiplicar() {
        // Delega la operación al método genérico enviarOperacion con el operador "*"
        enviarOperacion("*");
    }
    
    @FXML
    public void dividir() {
        // Delega la operación al método genérico enviarOperacion con el operador "/"
        enviarOperacion("/");
    }
    
    // ========================================================================
    // LÓGICA DE ENVÍO DE OPERACIONES AL SERVIDOR
    // ========================================================================
    // Este método centraliza la lógica de:
    // 1. Leer los datos de la interfaz (txtNumero1, txtNumero2)
    // 2. Validar que sean números (Double.parseDouble puede lanzar NumberFormatException)
    // 3. Enviar la operación al servidor mediante la clase Cliente
    // 4. Mostrar el resultado o un mensaje de error
    // 
    // La ventaja de tener un método privado enviarOperacion es que evita duplicar
    // el código de lectura, validación y manejo de errores en cada método de operación.
    // Este es un ejemplo de refactorización "Don't Repeat Yourself" (DRY).
    
    private void enviarOperacion(String operador) {
        try {
            // --- PASO 1: Obtener datos de la vista ---
            // txtNumero1.getText() y txtNumero2.getText() leen el texto ingresado
            // por el usuario en los campos de texto del FXML.
            // Estos TextField fueron inyectados automáticamente gracias a @FXML.
            double n1 = Double.parseDouble(txtNumero1.getText());
            double n2 = Double.parseDouble(txtNumero2.getText());
            
            // --- PASO 2: Enviar al servidor ---
            // Se invoca al método enviar() de la clase Cliente.
            // Esta es la lógica de negocio que:
            //   a) Convierte n1, n2, operador en string "n1,n2,operador"
            //   b) Crea un DatagramSocket y envía el paquete UDP a SERVER_IP:SERVER_PUERTO
            //   c) Espera la respuesta del servidor
            //   d) Retorna el resultado como String
            // Ver entidades/Cliente.java para la implementación detallada del protocolo UDP.
            String resultado = cliente.enviar(SERVER_IP, SERVER_PUERTO, n1, n2, operador);
            
            // --- PASO 3: Mostrar resultado ---
            // txtResultado.setText() actualiza el campo de texto en la vista.
            // Como txtResultado tiene editable="false" en el FXML, el usuario no puede editarlo.
            txtResultado.setText(resultado);
            
        } catch (NumberFormatException e) {
            // Error de validación: el usuario ingresó algo que no es un número válido.
            // Por ejemplo: texto vacío, letras, símbolos no numéricos.
            // Se muestra un mensaje de error en el campo resultado.
            txtResultado.setText("Error: números inválidos");
        } catch (Exception e) {
            // Error de comunicación: puede ser "Servidor no disponible (timeout)", 
            // "Connection refused", u otras excepciones de red.
            // El mensaje viene de la excepción lanzada por cliente.enviar().
            txtResultado.setText("Error: " + e.getMessage());
        }
    }
    
    // ========================================================================
    // MÉTODO DE LIMPIEZA DE CAMPOS
    // ========================================================================
    // Vinculado al botón "Limpiar" (FXML línea 33) con onAction="#limpiar".
    // Simplemente borra el contenido de los tres campos de texto.
    // Es un método de utilidad para mejorar la experiencia de usuario.
    
    @FXML
    public void limpiar() {
        txtNumero1.setText("");      // Borra el primer número
        txtNumero2.setText("");      // Borra el segundo número
        txtResultado.setText("");    // Borra el resultado/error anterior
    }
}
