package Prueba.server;


import Prueba.server.controlador.Controller;
import Prueba.server.modelo.Usuario;

import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class ServidorUDP {
    private final Controller controller = new Controller();

    static void main() throws Exception {
        new ServidorUDP().iniciar();
    }

    public void iniciar() throws Exception {
        try (DatagramSocket socket = new DatagramSocket(5000)) {
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                socket.receive(peticion);

                String mensaje = new String(peticion.getData(), 0, peticion.getLength()).trim();
                String respuesta = procesar(mensaje);

                byte[] datos = respuesta.getBytes();
                DatagramPacket respPkt = new DatagramPacket(
                        datos, datos.length,
                        peticion.getAddress(), peticion.getPort());
                socket.send(respPkt);
            }
        }
    }


    public String procesar(String mensaje) {
        String[] partes = mensaje.split("\\|");
        if (partes.length == 0) return "ERROR|MENSAJE_VACIO";

        return switch (partes[0].toUpperCase()) {

            case "BUSCAR" -> {
                if (partes.length < 2) yield "ERROR|FALTAN_PARAMETROS";
                Usuario u = controller.buscarUsuario(partes[1]);
                yield u != null ? "OK|" + u.serialize() : "ERROR|NO_ENCONTRADO";
            }

            case "REGISTRAR" -> {
                if (partes.length < 7) yield "ERROR|FALTAN_PARAMETROS";
                boolean pref = Boolean.parseBoolean(partes[6]);
                boolean ok = controller.crearUsuario(
                        partes[1], partes[2], partes[3],
                        partes[4], partes[5], pref);
                yield ok ? "OK|REGISTRADO" : "ERROR|YA_EXISTE";
            }

            case "RECARGAR" -> {
                // RECARGAR|cedula|monto
                if (partes.length < 3) yield "ERROR|FALTAN_PARAMETROS";
                try {
                    boolean ok = controller.recargarTarjeta(partes[1]);
                    yield ok ? controller.consultarSaldo(partes[1]) : "ERROR|USUARIO_NO_ENCONTRADO";
                } catch (NumberFormatException e) {
                    yield "ERROR|MONTO_INVALIDO";
                }
            }

            case "PAGAR" -> {
                if (partes.length < 2) yield "ERROR|FALTAN_PARAMETROS";
                yield controller.pagarPasaje(partes[1]);
            }

            case "SALDO" -> {
                if (partes.length < 2) yield "ERROR|FALTAN_PARAMETROS";
                yield controller.consultarSaldo(partes[1]);
            }

            default -> "ERROR|OPERACION_DESCONOCIDA";
        };
    }
}