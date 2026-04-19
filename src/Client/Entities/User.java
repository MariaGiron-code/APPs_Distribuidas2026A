package Client.Entities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

/**
 * Clase que representa a un usuario (cliente) en el sistema
 * Se identifica de forma única por su dirección IP y puerto, envía el mensaje(datos de la petición)
 */
public class User {
   //Metodo para conectar con el server y recibir la respuesta del server
    public String ServerConect (String IP, int puerto, String mensaje) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5000);
        InetAddress direccionIP = InetAddress.getByName(IP);

        // 1. Procesar y enviar el datagrama de salida (el mensaje del protocolo)
        byte[] bufferSalida = mensaje.getBytes(); //String a bytes(datos)
        DatagramPacket salida = new DatagramPacket(bufferSalida, bufferSalida.length, direccionIP, puerto);
        socket.send(salida);

        //2. Procesar y recibir la respuesta
        //RECIBIR
        byte[] bufferRespuesta = new byte[1024]; //Contenedor para la pregunta/resultado
        DatagramPacket respuesta = new DatagramPacket(bufferRespuesta, bufferRespuesta.length);

        try {
            socket.receive(respuesta);
            //PROCESAR
            // Una vez obtenemos la respuesta convertimos los datos recibidos a String y los devolvemos
            String resultado = new String(respuesta.getData(), 0, respuesta.getLength());
            socket.close();
            return resultado;
        }catch (SocketTimeoutException e){
            socket.close();
            return "Error: Servidor no disponible (timeout)";
        }
    }

}
