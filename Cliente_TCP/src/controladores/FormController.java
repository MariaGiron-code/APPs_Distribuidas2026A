package controladores;

import clases.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FormController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextArea txtResultado;

    // Mapa de código → nombre legible del timbre
    private String getNombreTipo(String tipo) {
        switch (tipo) {
            case "1": return "Ingreso";
            case "2": return "Salida almuerzo";
            case "3": return "Entrada almuerzo";
            case "4": return "Salida";
            default:  return tipo;
        }
    }

    private boolean nombreValido() {
        if (txtNombre.getText().trim().isEmpty()) {
            txtResultado.setText("Ingrese su nombre");
            return false;
        }
        return true;
    }

    // Registra un timbre y muestra ventana emergente con el resultado
    private void registrarTimbre(String tipoTimbre) {
        if (!nombreValido()) return;
        String nombre = txtNombre.getText().trim();
        try {
            Cliente cliente = new Cliente();
            String respuesta = cliente.enviar(nombre, tipoTimbre);

            // Ventana emergente de confirmación
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Timbre registrado");
            alerta.setHeaderText("✔ Timbre realizado con éxito");
            alerta.setContentText(
                "Empleado : " + nombre + "\n" +
                "Tipo     : " + getNombreTipo(tipoTimbre) + "\n" +
                "Hora     : " + respuesta
            );
            alerta.showAndWait();

        } catch (Exception e) {
            txtResultado.setText("Error al conectar con el servidor: " + e.getMessage());
        }
    }

    @FXML
    public void handleIngreso() {
        registrarTimbre("1");
    }

    @FXML
    public void handleSalidaAlmuerzo() {
        registrarTimbre("2");
    }

    @FXML
    public void handleEntradaAlmuerzo() {
        registrarTimbre("3");
    }

    @FXML
    public void handleSalida() {
        registrarTimbre("4");
    }

    @FXML
    public void handleConsultar() {
        if (!nombreValido()) return;
        String nombre = txtNombre.getText().trim();
        try {
            Cliente cliente = new Cliente();
            String respuesta = cliente.consultar(nombre);
            // Mostrar la lista en el TextArea (soporta múltiples líneas)
            txtResultado.setText(respuesta);
        } catch (Exception e) {
            txtResultado.setText("Error al conectar con el servidor: " + e.getMessage());
        }
    }
}
