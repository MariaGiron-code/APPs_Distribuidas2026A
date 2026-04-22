package Prueba.cliente;


import Prueba.cliente.modelo.Usuario;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClienteUDP {

    private static final int TIMEOUT = 3000;   // ms
    private static final int BUFFER = 1024;
    private final String host;
    private final int puerto;

    public ClienteUDP(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public String enviar(String mensaje) throws Exception {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT);
            InetAddress dir = InetAddress.getByName(host);
            byte[] datos = mensaje.getBytes();
            socket.send(new DatagramPacket(datos, datos.length, dir, puerto));

            byte[] buffer = new byte[BUFFER];
            DatagramPacket resp = new DatagramPacket(buffer, buffer.length);
            socket.receive(resp);
            return new String(resp.getData(), 0, resp.getLength()).trim();
        }
    }

    public Usuario buscarUsuario(String cedula) throws Exception {
        String resp = enviar("BUSCAR|" + cedula);
        if (!resp.startsWith("OK|")) return null;
        return Usuario.deserializar(resp.substring(3));
    }

    public boolean registrarUsuario(String cedula, String nombre, String apellido,
                                    String correo, String telefono, boolean preferencial)
            throws Exception {
        String msg = String.join("|",
                "REGISTRAR", cedula, nombre, apellido, correo, telefono,
                String.valueOf(preferencial));
        String resp = enviar(msg);
        return resp.startsWith("OK|");
    }

    public double recargarTarjeta(String cedula, double monto) throws Exception {
        String resp = enviar("RECARGAR|" + cedula + "|" + monto);
        if (!resp.startsWith("OK|")) throw new RuntimeException(resp);
        return Double.parseDouble(resp.split("\\|")[1]);
    }

    public double pagarPasaje(String cedula) throws Exception {
        String resp = enviar("PAGAR|" + cedula);
        if (!resp.startsWith("OK|")) throw new RuntimeException(resp);
        return Double.parseDouble(resp.split("\\|")[1]);
    }

    public double consultarSaldo(String cedula) throws Exception {
        String resp = enviar("SALDO|" + cedula);
        if (!resp.startsWith("OK|")) throw new RuntimeException(resp);
        return Double.parseDouble(resp.split("\\|")[1]);
    }
}