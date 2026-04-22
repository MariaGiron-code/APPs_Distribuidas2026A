package Prueba.server.modelo;

import Prueba.server.interfaz.ITarjeta;
import lombok.Getter;

@Getter
public class Tarjeta implements ITarjeta {
    private final Usuario usuario;
    private double saldo;

    public Tarjeta(Usuario usuario) {
        this.usuario = usuario;
        this.saldo = 0.0;
        usuario.setTarjeta(this);
    }

    @Override
    public void cargarSaldo() {
        this.saldo += 1.50;
    }

    @Override
    public boolean pagarPasaje() {
        if (saldo < 0.35) return false;
        saldo -= 0.35;
        return true;
    }
}
