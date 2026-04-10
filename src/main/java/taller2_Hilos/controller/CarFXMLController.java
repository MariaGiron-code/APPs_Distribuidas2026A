package taller2_Hilos.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import taller2_Hilos.model.Modelo;
import taller2_Hilos.thread.VentanaThread;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * ControladorFXML - Conecta la vista (viewCar.fxml) con el código Java
 * Este clase maneja los eventos de la UI y conecta con el hilo
 */
public class CarFXMLController implements Initializable {

    // Elementos del FXML - inyectados automáticamente por @FXML
    @FXML
    private TextField txtCode;
    @FXML
    private TextField txtMark;

    @FXML
    private TextField txtModel;

    @FXML
    private TextField txtPriceCar;

    // TextArea para mostrar mensajes de los hilos
    @FXML
    private TextArea txtMensajes;

    // Modelo - conexión a MongoDB
    private Modelo modelo;
    public void setMensaje(String mensaje) {
        Platform.runLater(() -> {
            txtMensajes.appendText(mensaje + "\n");
        });
    }

    /**
     * Inicializa el controlador después de cargar el FXML
     * Se ejecuta automáticamente cuando se carga la vista
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar el modelo (conexión a MongoDB)
        modelo = new Modelo();
    }

    /**
     * Metodo que se ejecuta cuando el usuario hace click en "Guardar"
     * Este metodo está conectado al botón en el FXML con el evento onAction="#saveCar"
     */
    @FXML
    public void saveCar() {
        try {
            // 1. Obtener los valores de los TextField
            String codigoTexto = txtCode.getText();
            String marca = txtMark.getText();
            String modeloTexto = txtModel.getText();
            String precioTexto = txtPriceCar.getText();

            // 2. Validar que los campos no estén vacíos
            if (codigoTexto.isEmpty() || marca.isEmpty() || 
                modeloTexto.isEmpty() || precioTexto.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios");
                return;
            }

            // 3. Convertir tipos de datos
            int codigo = Integer.parseInt(codigoTexto);
            double precio = Double.parseDouble(precioTexto);

            // 4. Crear el objeto CarController con los datos del usuario
            CarController car = new CarController(codigo, marca, modeloTexto, precio);

            // 5. Crear e iniciar el hilo
            VentanaThread hilo = new VentanaThread(modelo, car, this);
            hilo.start();

            mostrarAlerta("Éxito", "Vehículo guardado exitosamente");

            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El código y precio deben ser números");
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtCode.clear();
        txtMark.clear();
        txtModel.clear();
        txtPriceCar.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}