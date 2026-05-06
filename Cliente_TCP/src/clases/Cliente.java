package clases;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Cliente {

    private static final String IP_SERVIDOR = "192.168.56.1";
    private static final int PUERTO = 7000;

    // Registra un timbre en el servidor y devuelve la respuesta
    public String enviar(String empleado, String tipoTimbre) throws Exception {
        Socket socket = new Socket(IP_SERVIDOR, PUERTO);

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        DataOutputStream dos = new DataOutputStream(out);
        DataInputStream dis = new DataInputStream(in);

        // Protocolo: operación → nombre → tipo de timbre
        dos.writeUTF("TIMBRE");
        dos.writeUTF(empleado);
        dos.writeUTF(tipoTimbre);

        // Recibir respuesta del servidor (fecha/hora o mensaje de error)
        String respuesta = dis.readUTF();

        System.out.println("Timbre registrado — Empleado: " + empleado + " | Respuesta: " + respuesta);

        socket.close();
        return respuesta;
    }

    // Consulta los timbres del día de un empleado
    public String consultar(String empleado) throws Exception {
        Socket socket = new Socket(IP_SERVIDOR, PUERTO);

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        DataOutputStream dos = new DataOutputStream(out);
        DataInputStream dis = new DataInputStream(in);

        // Protocolo: operación → nombre
        dos.writeUTF("GET");
        dos.writeUTF(empleado);

        // Recibir la lista de timbres o mensaje de sin registros
        String respuesta = dis.readUTF();

        System.out.println("Consulta — Empleado: " + empleado + "\n" + respuesta);

        socket.close();
        return respuesta;
    }
}
