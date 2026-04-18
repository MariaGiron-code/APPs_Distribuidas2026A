package entidades;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {

    private Modelo modelo = new Modelo();

    // Logica del server
    // Definición del metodo
    public void operar(int puerto) throws Exception {
        //1.- CREAR EL SOCKE DEL DATAGRAMA
        DatagramSocket socket = new DatagramSocket(puerto);
        System.out.println("Servidor conectado y ejecutado en el puerto " + puerto);

        //2.- RECIBIR PETICIÓN DEL CLIENT
        while (true) {
            //recibimos los datos (petición del cliente) siempre en bytes en un arreglo de un mismo tipo
            byte[] bufferEnter = new byte[1024]; //reservar espacio de 1024 bytes para recibir los datos

            //recibir el paquete con los datos de entrada
            DatagramPacket packetEnter = new DatagramPacket(bufferEnter, bufferEnter.length);
            socket.receive(packetEnter); //el server sigue corriendo hasta obtener los datos de entrada

            //3.- PROCESAR los datos que vienen en bytes(dentro del Datagrama) y lo convertimos a String o tipo que necesitemos
            String recibido = new String(packetEnter.getData(), 0, packetEnter.getLength());
            String[] partes = recibido.trim().split(",");

            double n1 = Double.parseDouble(partes[0]);
            double n2 = Double.parseDouble(partes[1]);

            String operacion = partes[2];
            double respuesta = 0.0;

            // Sumamos las respuestas que llegan del cliente con las del modelo en el server
            //double respuesta = n1 + n2;
            //double respuesta = sumar(2, 3);
            if (operacion.equals("+")){respuesta = modelo.sumar(n1, n2);}
            if (operacion.equals("-")){respuesta = modelo.restar(n1, n2);}
            if (operacion.equals("*")){respuesta = modelo.multiplicar(n1, n2);}
            if (operacion.equals("/")){ respuesta = modelo.dividir(n1, n2);}

            //4.- RESOLVER LA SOLICITUD
            byte[] bufferSalida = String.valueOf(respuesta).getBytes();  // crear el buffer de tipo String de salida de la response
            // en el length poner dos parámetros más para enviar al cliente colocar su respectivo ip y su puerto
            DatagramPacket salida = new DatagramPacket(bufferSalida, bufferSalida.length, packetEnter.getAddress(), packetEnter.getPort());
            socket.send(salida); //enviamos info al cliente
        }
    }
}
