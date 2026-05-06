package clases;

public class RegistroTimbre {

    private String nombre;
    private String tipoTimbre;
    private String fechaHora;

    public RegistroTimbre(String nombre, String tipoTimbre, String fechaHora) {
        this.nombre = nombre;
        this.tipoTimbre = tipoTimbre;
        this.fechaHora = fechaHora;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoTimbre() {
        return tipoTimbre;
    }

    public String getFechaHora() {
        return fechaHora;
    }
}
