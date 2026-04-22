package Prueba.cliente.modelo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {

    private String cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private boolean preferencial;
    private double saldo;

    public static Usuario deserializar(String datos) {
        String[] p = datos.split("\\|");
        if (p.length < 7) throw new IllegalArgumentException("Respuesta incompleta: " + datos);
        Usuario u = new Usuario();
        u.cedula = p[0];
        u.nombre = p[1];
        u.apellido = p[2];
        u.correo = p[3];
        u.telefono = p[4];
        u.preferencial = Boolean.parseBoolean(p[5]);
        u.saldo = Double.parseDouble(p[6]);
        return u;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}