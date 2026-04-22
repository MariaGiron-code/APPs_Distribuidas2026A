package Prueba.UI;

import Prueba.cliente.ClienteUDP;
import Prueba.cliente.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RecargasController {

    private ClienteUDP cliente;
    private Usuario usuario;

    @FXML
    private TextArea txtAInfoUsuario;
    @FXML
    private Label lbSaldo;
    @FXML
    private TextField txtMonto;
    @FXML
    private Button btnRecargarTarjeta;
    @FXML
    private Button btnPayPasaje;
    @FXML
    private Label lblFeedback;

    public void init(ClienteUDP cliente, Usuario usuario) {
        this.cliente = cliente;
        this.usuario = usuario;
        mostrarInfo();
        actualizarSaldo();
    }

    private void mostrarInfo() {
        txtAInfoUsuario.setText(
                "Nombre:     " + usuario.getNombreCompleto() + "\n" +
                        "Cédula:     " + usuario.getCedula() + "\n" +
                        "Correo:     " + usuario.getCorreo() + "\n" +
                        "Preferencial: " + (usuario.isPreferencial() ? "Sí" : "No")
        );
    }

    private void actualizarSaldo() {
        try {
            double saldo = cliente.consultarSaldo(usuario.getCedula());
            lbSaldo.setText(String.format("Saldo: $%.2f", saldo));
            usuario.setSaldo(saldo);
        } catch (Exception e) {
            lbSaldo.setText("Error al obtener saldo");
        }
    }

    @FXML
    public void recargar() {
        String montoStr = txtMonto.getText().trim();
        if (montoStr.isEmpty()) {
            lblFeedback.setText("Ingresa un monto.");
            return;
        }

        try {
            double monto = Double.parseDouble(montoStr);
            double nuevoSaldo = cliente.recargarTarjeta(usuario.getCedula(), monto);
            lbSaldo.setText(String.format("Saldo: $%.2f", nuevoSaldo));
            lblFeedback.setText("Recarga exitosa.");
            txtMonto.clear();
        } catch (NumberFormatException e) {
            lblFeedback.setText("Monto inválido.");
        } catch (Exception e) {
            lblFeedback.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void pagarPasaje() {
        try {
            double nuevoSaldo = cliente.pagarPasaje(usuario.getCedula());
            lbSaldo.setText(String.format("Saldo: $%.2f", nuevoSaldo));
            lblFeedback.setText("Pasaje cobrado ($0.35).");
        } catch (Exception e) {
            lblFeedback.setText("Error: " + e.getMessage());
        }
    }
}