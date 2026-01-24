package entidades;

//PRIMER ENTIDAD HECHA

public class Estadio {
    private int idEstadio;
    private String nombre;
    private String direccion;
    private int capacidadTotal;


    //constructores
    public Estadio() {
    }


    public Estadio( String nombre, String direccion, int capacidadTotal) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadTotal = capacidadTotal;
    }



    //setters y getters
    public int getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(int idEstadio) {
        this.idEstadio = idEstadio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getCapacidadTotal() {
        return capacidadTotal;
    }

    public void setCapacidadTotal(int capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
    }



    //tostring

    @Override
    public String toString() {
        return "Estadio\n" +
                "idEstadio: " + idEstadio +
                "\nNombre del estadio: " + nombre +
                "\nDireccion: '" + direccion +
                "\nCapacidad Total=" + capacidadTotal;
    }


}
