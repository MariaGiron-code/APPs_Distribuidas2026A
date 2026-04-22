package Prueba.UI;


import Prueba.cliente.ClienteUDP;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

public class RegistroController {

    @Setter
    private ClienteUDP cliente;

    @FXML
    private TextField lbCedulaEnter;
    @FXML
    private TextField lbNombreEnter;
    @FXML
    private TextField lbApellidoEnter;
    @FXML
    private TextField lbCorreoEnter;
    @FXML
    private TextField lbTelefonoEnter;
    @FXML
    private CheckBox cbPreferencial;
    @FXML
    private Label lblFeedback;
    @FXML
    private Button btnCrearUser;

    @FXML
    public void crearUsuario() {
        String cedula = lbCedulaEnter.getText().trim();
        String nombre = lbNombreEnter.getText().trim();
        String apellido = lbApellidoEnter.getText().trim();
        String correo = lbCorreoEnter.getText().trim();
        String telefono = lbTelefonoEnter.getText().trim();
        boolean pref = cbPreferencial.isSelected();

        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            lblFeedback.setText("Cédula, nombre y apellido son obligatorios.");
            return;
        }

        try {
            boolean ok = cliente.registrarUsuario(cedula, nombre, apellido,
                    correo, telefono, pref);
            if (ok) {
                lblFeedback.setText("Usuario registrado correctamente.");
                // Cierra la ventana tras 1.5s para que el usuario vea el mensaje
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ignored) {
                    }
                    javafx.application.Platform.runLater(
                            () -> ((Stage) btnCrearUser.getScene().getWindow()).close()
                    );
                }).start();
            } else {
                lblFeedback.setText("La cédula ya está registrada.");
            }
        } catch (Exception e) {
            lblFeedback.setText("Error de conexión con el servidor.");
        }
    }
}