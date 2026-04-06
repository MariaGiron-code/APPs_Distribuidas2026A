import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controlador {

    @FXML private TextField txtNumero1;
    @FXML private TextField txtNumero2;
    @FXML private TextField txtResultado;

    private Modelo modelo = new Modelo();

    @FXML
    public void sumar() {
        double n1 = Double.parseDouble(txtNumero1.getText());
        double n2 = Double.parseDouble(txtNumero2.getText());
        double resultado = modelo.sumar(n1, n2);
        txtResultado.setText(String.valueOf(resultado));
    }

    @FXML
    public void restar() {
        double n1 = Double.parseDouble(txtNumero1.getText());
        double n2 = Double.parseDouble(txtNumero2.getText());
        double resultado = modelo.restar(n1, n2);
        txtResultado.setText(String.valueOf(resultado));
    }

    @FXML
    public void multiplicar() {
        double n1 = Double.parseDouble(txtNumero1.getText());
        double n2 = Double.parseDouble(txtNumero2.getText());
        double resultado = modelo.multiplicar(n1, n2);
        txtResultado.setText(String.valueOf(resultado));
    }

    @FXML
    public void dividir() {
        double n1 = Double.parseDouble(txtNumero1.getText());
        double n2 = Double.parseDouble(txtNumero2.getText());
        if (n2 == 0) {
            txtResultado.setText("Error: división por cero");
        } else {
            double resultado = modelo.dividir(n1, n2);
            txtResultado.setText(String.valueOf(resultado));
        }
    }

    @FXML
    public void limpiar() {
        txtNumero1.setText("");
        txtNumero2.setText("");
        txtResultado.setText("");
    }
}
