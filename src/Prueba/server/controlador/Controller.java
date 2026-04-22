package Prueba.server.controlador;

import Prueba.server.interfaz.IController;
import Prueba.server.modelo.Tarjeta;
import Prueba.server.modelo.Usuario;

import java.util.ArrayList;

public class Controller implements IController {

    private final ArrayList<Usuario> usuarios = new ArrayList<>();
    private final ArrayList<Tarjeta> tarjetas = new ArrayList<>();

    @Override
    public Usuario buscarUsuario(String cedula) {
        for (Usuario u : usuarios) {
            if (u.getCEDULA().equals(cedula)) return u;
        }
        return null;
    }

    @Override
    public boolean crearUsuario(String cedula, String nombre, String apellido,
                                String correo, String telefono, boolean preferencial) {
        if (buscarUsuario(cedula) != null) return false;

        Usuario usuario = new Usuario(cedula, nombre, apellido, correo, telefono, preferencial);
        usuarios.add(usuario);

        Tarjeta tarjeta = new Tarjeta(usuario);
        tarjetas.add(tarjeta);

        return true;
    }

    @Override
    public boolean recargarTarjeta(String cedula) {
        Usuario u = buscarUsuario(cedula);
        if (u == null || u.getTarjeta() == null) return false;
        u.getTarjeta().cargarSaldo();
        return true;
    }

    @Override
    public String pagarPasaje(String cedula) {
        Usuario u = buscarUsuario(cedula);
        if (u == null) return "ERROR|USUARIO_NO_ENCONTRADO";
        if (u.getTarjeta() == null) return "ERROR|SIN_TARJETA";
        boolean ok = u.getTarjeta().pagarPasaje();
        return ok
                ? "OK|" + u.getTarjeta().getSaldo()
                : "ERROR|SALDO_INSUFICIENTE";
    }

    @Override
    public String consultarSaldo(String cedula) {
        Usuario u = buscarUsuario(cedula);
        if (u == null) return "ERROR|USUARIO_NO_ENCONTRADO";
        return "OK|" + u.getTarjeta().getSaldo();
    }

    public int totalUsuarios() {
        return usuarios.size();
    }

    public int totalTarjetas() {
        return tarjetas.size();
    }
}