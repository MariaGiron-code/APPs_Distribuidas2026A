package Prueba.server.interfaz;

import Prueba.server.modelo.Usuario;

public interface IController {
    Usuario buscarUsuario(String cedula);

    boolean crearUsuario(String cedula, String nombre, String apellido,
                         String correo, String telefono, boolean preferencial);

    boolean recargarTarjeta(String cedula);

    String pagarPasaje(String cedula);

    String consultarSaldo(String cedula);
}