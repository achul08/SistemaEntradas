package entidades;

import java.sql.Timestamp;


//HACER ESTA ULTIMA (depende de todo)


public class Venta {
    private int idVenta;
    private int idEspectaculo;
    private int idUbicacion;
    private int idVendedor;
    private Timestamp fechaVenta;
    private double precioFinal;
    private String nombreCliente;
    private String dniCliente;


    //constructores

    public Venta() {
    }

    public Venta(int idEspectaculo, int idUbicacion, int idVendedor, Timestamp fechaVenta, double precioFinal, String nombreCliente, String dniCliente) {
        this.idEspectaculo = idEspectaculo;
        this.idUbicacion = idUbicacion;
        this.idVendedor = idVendedor;
        this.fechaVenta = fechaVenta;
        this.precioFinal = precioFinal;
        this.nombreCliente = nombreCliente;
        this.dniCliente = dniCliente;
    }


//getters y setters

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdEspectaculo() {
        return idEspectaculo;
    }

    public void setIdEspectaculo(int idEspectaculo) {
        this.idEspectaculo = idEspectaculo;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Timestamp getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Timestamp fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }


    //tostring
    @Override
    public String toString() {
        return "Venta\n" +
                "id de la venta: " + idVenta +
                "\nid del espectaculo: " + idEspectaculo +
                "\nid de la ubicacion: " + idUbicacion +
                "\nid del vendedor: " + idVendedor +
                "\nFecha de la venta: " + fechaVenta +
                "\nPrecio final: " + precioFinal +
                "\nNombre del cliente: " + nombreCliente +
                "\nDNI del cliente: " + dniCliente ;
    }
}
