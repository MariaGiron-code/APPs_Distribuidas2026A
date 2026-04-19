package Test;

import Server.Server;

public class RunServer {
    public static void main(String[] args) throws Exception {
        // Arrancamos el server
        (new Server()).operar(5000);
    }
}