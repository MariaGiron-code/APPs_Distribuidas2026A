package clases;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Servidor {

    // Almacena los timbres del día: clave = nombre del empleado, valor = lista de timbres
    private HashMap<String, ArrayList<String>> timbres = new HashMap<>();

    public String getFecha(String nombre) {
        Date fecha = new Date();
        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formato.format(fecha);
    }

    public String registrarTimbre(String nombre, String tipoTimbre) {
        // Validar que el tipo de timbre sea válido
        String nombreTipo;
        switch (tipoTimbre) {
            case "1": nombreTipo = "Ingreso";           break;
            case "2": nombreTipo = "Salida almuerzo";   break;
            case "3": nombreTipo = "Entrada almuerzo";  break;
            case "4": nombreTipo = "Salida";            break;
            default:
                return "Tipo de timbre inválido";
        }


        String fechaHora = getFecha(nombre);

        String hora = fechaHora.split(" ")[1];

        // Construir el formato de entrada de la fecha: "NombreTipo: HH:mm:ss"
        String entrada = nombreTipo + ": " + hora;

        // Agregar al HashMap (creamos la lista si no existe)
        if (!timbres.containsKey(nombre)) {
            timbres.put(nombre, new ArrayList<>());
        }
        timbres.get(nombre).add(entrada);

        System.out.println("Timbre registrado " + nombre + " | " + entrada);

        return fechaHora;
    }

    public String getTimbre(String nombre) {
        // Si el empleado no tiene timbres registrados
        if (!timbres.containsKey(nombre) || timbres.get(nombre).isEmpty()) {
            return "No ha realizado ningún timbre durante el día";
        }

        // Concatenar todos los timbres del empleado
        ArrayList<String> lista = timbres.get(nombre);
        String resultado = "Timbres de " + nombre + ":\n";
        for (String timbre : lista) {
            resultado += "  - " + timbre + "\n";
        }
        return resultado.trim();
    }

    public void servicio(int puerto) throws Exception {
        ServerSocket servidor = new ServerSocket(puerto);
        System.out.println("Servidor corriendo en el puerto: " + puerto);

        while (true) {
            Socket cliente = servidor.accept();
            System.out.println("----------");
            System.out.println("Cliente conectado exitosamente");

            // Objetos de entrada y salida
            InputStream in = cliente.getInputStream();
            OutputStream out = cliente.getOutputStream();

            DataInputStream dis = new DataInputStream(in);
            DataOutputStream dos = new DataOutputStream(out);

            // Leer la operación que envía el cliente
            String operacion = dis.readUTF();

            if (operacion.equals("x")) {
                cliente.close();
                break;
            }

            String respuesta;

            if (operacion.equals("TIMBRE")) {
                // Leer nombre y tipo de timbre
                String nombre = dis.readUTF();
                String tipoTimbre = dis.readUTF();
                respuesta = registrarTimbre(nombre, tipoTimbre);
                System.out.println("Timbre procesado para: " + nombre);

            } else if (operacion.equals("GET")) {
                // Leer nombre del empleado a consultar
                String nombre = dis.readUTF();
                respuesta = getTimbre(nombre);
                System.out.println("Consulta procesada para: " + nombre);

            } else {
                respuesta = "Operación desconocida";
            }

            // Devolver la respuesta al cliente
            dos.writeUTF(respuesta);
            cliente.close();
        }

        servidor.close();
    }
}
