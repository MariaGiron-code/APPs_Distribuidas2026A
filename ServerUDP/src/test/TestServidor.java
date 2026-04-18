package test;

import entidades.Server;

public class TestServidor {
    public static void main(String[] args) throws Exception {
        (new Server()).operar(5000);
    }
}
