package controller;

import entidades.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ClienteController {
    @FXML private TextField txtNumero1;
    @FXML private TextField txtNumero2;
    @FXML private TextField txtResultado;

    private final Cliente cliente = new Cliente();
    private final String SERVER_IP = "localhost";
    private final int SERVER_PUERTO = 5000;

    @FXML
    public void sumar() {
        enviarOperacion("+");
    }

    @FXML
    public void restar() {
        enviarOperacion("-");
    }

    @FXML
    public void multiplicar() {
        enviarOperacion("*");
    }

    @FXML
    public void dividir() {
        enviarOperacion("/");
    }

    private void enviarOperacion(String operador) {
        try {
            double n1 = Double.parseDouble(txtNumero1.getText());
            double n2 = Double.parseDouble(txtNumero2.getText());
            String resultado = cliente.enviar(SERVER_IP, SERVER_PUERTO, n1, n2, operador);
            txtResultado.setText(resultado);
        } catch (NumberFormatException e) {
            txtResultado.setText("Error: números inválidos");
        } catch (Exception e) {
            txtResultado.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void limpiar() {
        txtNumero1.setText("");
        txtNumero2.setText("");
        txtResultado.setText("");
    }
}
