package entidades;

public class Ubicacion {
    private int idUbicacion;
    private int idEstadio;
    private String nombre;
    private double precio;
    private int capacidad;
    private String fotoUrl;


    //constructores
    public Ubicacion() {
    }

    public Ubicacion(int idEstadio, String nombre, double precio, int capacidad) {
        this.idEstadio = idEstadio;
        this.nombre = nombre;
        this.precio = precio;
        this.capacidad = capacidad;
    }

    //setters y getters

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }


    //tostring


    @Override
    public String toString() {
        return "Ubicacion\n" +
                "\nid de la ubicacion: " + idUbicacion +
                "\nid del estadio: " + idEstadio +
                "\nNombre: " + nombre + '\'' +
                "\nPrecio: " + precio +
                "\nCapacidad: " + capacidad +
                "\nFoto url: " + fotoUrl;
    }
}
