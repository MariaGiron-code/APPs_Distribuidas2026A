package Prueba.UI;

import Prueba.cliente.ClienteUDP;
import Prueba.cliente.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainController {

    private final ClienteUDP cliente = new ClienteUDP("localhost", 5000);

    @FXML
    private TextField lbCedulaEnter;
    @FXML
    private Label lblWarning;
    @FXML
    private Button btnBuscarUser;
    @FXML
    private Button btnRegistrarUser;

    @FXML
    void initialize() {
        lblWarning.setText("");
        lbCedulaEnter.clear();
    }

    @FXML
    public void searchUser() {
        String cedula = lbCedulaEnter.getText().trim();
        if (cedula.isEmpty()) {
            lbCedulaEnter.requestFocus();
            return;
        }

        try {
            Usuario usuario = cliente.buscarUsuario(cedula);
            lblWarning.setText("Usuario no encontrado. Por favor regístrate.");
            return;
        } catch (Exception e) {
            lblWarning.setText("Error de conexión con el servidor.");
        }
    }

    @FXML
    public void openRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/registro.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            RegistroController rc = loader.getController();
            rc.setCliente(cliente);
            stage.setTitle("Registro de usuario");
            stage.show();
        } catch (Exception e) {
            lblWarning.setText("No se pudo abrir el registro.");
        }
    }

    private void abrirRecargas(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recargas.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            RecargasController rc = loader.getController();
            rc.init(cliente, usuario);
            stage.setTitle("Gestión de tarjeta");
            stage.show();
        } catch (Exception e) {
            lblWarning.setText("No se pudo abrir recargas.");
        }
    }
}