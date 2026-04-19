package Client.Controller;

import Client.Entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;

public class UserController {
    //Vinculacion con los fx:id de la UI del Cuestionario
    @FXML
    private Label lblPregunta; // Se vincula con fx:id="lblPregunta"
    @FXML
    private Button btnOpcionA; // Se vincula con fx:id="btnOpcionA"
    @FXML
    private Button btnOpcionB;
    @FXML
    private Button btnOpcionC;
    @FXML
    private Button btnOpcionD;
    @FXML
    private Label lblEstado;   // Se vincula con fx:id="lblEstado"


    private final User user = new User();

    // 1. Configuración del servidor (IP y puerto)
    private final String SERVER_IP = "localhost";
    private final int SERVER_PUERTO = 5000;

    //Objeto para obtner el ID de la pregunta que el usuario va a responder
    private String idPreguntaActual = "";

    //2. Definicion de los métodos
    /**
     * Este metodo se ejecuta automáticamente cuando se abre la ventana.
     * La pregunta aparece al arrancar la UI.
     */
    @FXML
    public void initialize() throws Exception {
        getNextQ();
    }

    // Metodo para el botón "Siguiente" y para el arranque inicial.
    @FXML
    public void siguiente() throws Exception {
        getNextQ();
    }

    private void getNextQ() throws Exception {
        // Le pedimos al servidor una pregunta
        String respuesta = user.ServerConect(SERVER_IP, SERVER_PUERTO, "PEDIR PREGUNTA");
        String[] partes = respuesta.split("\\|"); //Dividimos la respuesta en un array de sub-strings usando el carácter | como delimitador.

        //validamos la respuesta del server
        if (partes.length == 6) {
            idPreguntaActual = partes[0];
            lblPregunta.setText(partes[1]);
            btnOpcionA.setText(partes[2]);
            btnOpcionB.setText(partes[3]);
            btnOpcionC.setText(partes[4]);
            btnOpcionD.setText(partes[5]);

            lblEstado.setText("Estado: Esperando que el usuario seleccione una respuesta...");
            bloquearBotones(false); // Habilitamos las opciones para que pueda responder
        } else {
            lblEstado.setText("Error al cargar la pregunta.");
        }
    }

    //Metodo para manejar las opciones de respuesta cuando el usuario las selecciona
    @FXML
    public void responderPregunta(ActionEvent event) throws Exception {
        // 1. Detectamos qué boton se seleccionó y gurdamos la opción de respuesta
        Button btnSeleccionado = (Button) event.getSource();
        String opcionElegida = btnSeleccionado.getText();

        // 2. Enviamos al server la respuesta
        String mensaje = "RESPONDER|" + idPreguntaActual + "|" + opcionElegida;
        String resultado = user.ServerConect(SERVER_IP, SERVER_PUERTO, mensaje);

        //Procesamos la respuesta del server
        lblEstado.setText("Estado: " + resultado);
        bloquearBotones(true); // Bloqueamos para que no pueda responder 2 veces a la misma pregunta
    }
    // Metodo auxiliar para bloquear o desbloquear las opciones de espuesta 
    private void bloquearBotones(boolean bloquear) {
        btnOpcionA.setDisable(bloquear);
        btnOpcionB.setDisable(bloquear);
        btnOpcionC.setDisable(bloquear);
        btnOpcionD.setDisable(bloquear);
    }
}
