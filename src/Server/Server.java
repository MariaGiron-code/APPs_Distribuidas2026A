package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {

    private Modelo modelo;

    public Server() {
        this.modelo = new Modelo();
    }
    //Metodo principal del server recibir y responder los request del usuario
    public void operar(int puerto) throws Exception {
        System.out.println("--- Servidor de Preguntas UDP ---");
        System.out.println("Iniciando en el puerto: " + puerto);

        try (DatagramSocket socket = new DatagramSocket(puerto)) {
            System.out.println("Servidor esperando conexiones del usuario...");

            byte[] bufferRecepcion = new byte[1024];

            while (true) {
                // Crear paquete para recibir datos del cliente
                DatagramPacket paqueteRecibido = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);

                // Esperar a recibir un mensaje
                socket.receive(paqueteRecibido);

                // Obtener datos del cliente
                InetAddress direccionCliente = paqueteRecibido.getAddress();
                int puertoCliente = paqueteRecibido.getPort();

                // Convertir mensaje a string
                String mensajeRecibido = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength()).trim();

                System.out.println("Recibido de " + direccionCliente + ":" + puertoCliente + " -> " + mensajeRecibido);

                // Procesar el mensaje y obtener respuesta
                String respuesta = procesarMensaje(mensajeRecibido);
                System.out.println("Enviando respuesta al usuario: " + respuesta);

                // Enviar respuesta al cliente
                byte[] bufferRespuesta = respuesta.getBytes();
                DatagramPacket paqueteEnvio = new DatagramPacket(bufferRespuesta, bufferRespuesta.length, direccionCliente, puertoCliente);

                socket.send(paqueteEnvio);
            }
        }
    }


     // Procesa el mensaje recibido del usuario (solicitud de preguntas y las respuestas de las mismas)

    private String procesarMensaje(String mensaje) {
        try {
            // Dividir el mensaje por con el simbolo |
            String[] partes = mensaje.split("\\|");

            if (partes.length == 0) {
                return "Error: Mensaje vacío";
            }

            String opcion = partes[0].trim().toUpperCase();

            switch (opcion) {
                case "PEDIR":
                case "PEDIR PREGUNTA":
                    // El cliente pide una nueva pregunta
                    return modelo.obtenerPregunta();

                case "RESPONDER":
                    // El cliente responde una pregunta
                    // Formato esperado: RESPONDER|ID_Pregunta|Respuesta
                    if (partes.length < 3) {
                        return "Error! Formato incorrecto";
                    }

                    String idPregunta = partes[1].trim();
                    String respuestaSeleccionada = partes[2].trim();

                    return modelo.validarRespuesta(idPregunta, respuestaSeleccionada);

                default:
                    return "Error: Opción no encontrada ";
            }

        } catch (Exception e) {
            return "Error al procesar: " + e.getMessage();
        }
    }

}


