package entidades;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Cliente {
    public String enviar(String IP, int puerto, double n1, double n2, String operacion) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        // Configurar timeout de 5 segundos para no bloquearse eternamente
        socket.setSoTimeout(5000);
        
        InetAddress direccion = InetAddress.getByName(IP);

        String datos = n1 + "," + n2 + "," + operacion;
        byte[] bufferSalida = String.valueOf(datos).getBytes();
        DatagramPacket salida = new DatagramPacket(bufferSalida, bufferSalida.length, direccion, puerto);
        socket.send(salida);

        byte[] bufferRespuesta = new byte[1024];
        DatagramPacket respuesta = new DatagramPacket(bufferRespuesta, bufferRespuesta.length);
        
        try {
            socket.receive(respuesta);
            String resultado = new String(respuesta.getData(), 0, respuesta.getLength());
            socket.close();
            return resultado;
        } catch (SocketTimeoutException e) {
            socket.close();
            return "Error: Servidor no disponible (timeout)";
        }
    }
}
