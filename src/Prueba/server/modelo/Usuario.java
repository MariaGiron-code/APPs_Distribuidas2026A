package Prueba.server.modelo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {

    private final String CEDULA;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private boolean preferencial;

    private Tarjeta tarjeta;

    public Usuario(String cedula, String nombre, String apellido,
                   String correo, String telefono, boolean preferencial) {
        this.CEDULA = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.preferencial = preferencial;
    }

    public String serialize() {
        return String.join("|",
                CEDULA, nombre, apellido, correo, telefono,
                String.valueOf(preferencial),
                tarjeta != null ? String.valueOf(tarjeta.getSaldo()) : "0.0"
        );
    }
}