package entidades;

import java.util.Date;


//HACER ESTA 4TA (tambien depende de estadio)



public class Espectaculo {
    private int idEspectaculo;
    private String nombre;
    private Date fecha;
    private int idEstadio;
    private boolean activo;


    //constructores
    public Espectaculo() {
    }

    public Espectaculo(String nombre, Date fecha, int idEstadio, boolean activo) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.idEstadio = idEstadio;
        this.activo = activo;
    }

    //getters y setters
    public int getIdEspectaculo() {
        return idEspectaculo;
    }

    public void setIdEspectaculo(int idEspectaculo) {
        this.idEspectaculo = idEspectaculo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(int idEstadio) {
        this.idEstadio = idEstadio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    //tostring


    @Override
    public String toString() {
        return "Espectaculo\n" +
                "id del espectaculo: " + idEspectaculo +
                "\nNombre: " + nombre +
                "\nFecha: " + fecha +
                "\nid del estadio: " + idEstadio +
                "\nSe encuentra activo? " + activo;
    }
}
