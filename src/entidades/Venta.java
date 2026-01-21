package entidades;

import java.sql.Timestamp;

//ENTIDAD VENTA - MODIFICADA
//Ahora incluye el atributo tipoPromocion para saber qué descuento se aplicó


public class Venta {
    //ATRIBUTOS ORIGINALES -----
    private int idVenta;
    private int idEspectaculo;
    private int idUbicacion;
    private int idVendedor;
    private Timestamp fechaVenta;
    private double precioFinal;
    private String nombreCliente;
    private String dniCliente;

    //ATRIBUTO NUEVO -----
    //Guarda el nombre de la promoción que se aplicó
    //Ejemplos: "Happy Hour", "Sin promoción"
    private String tipoPromocion;


    //CONSTRUCTOR VACÍO -----
    public Venta() {
    }

    //CONSTRUCTOR CON PARÁMETROS (SIN ID porque es auto_increment) -----
    //MODIFICADO: Ahora incluye tipoPromocion
    public Venta(int idEspectaculo, int idUbicacion, int idVendedor, Timestamp fechaVenta,
                 double precioFinal, String nombreCliente, String dniCliente, String tipoPromocion) {
        this.idEspectaculo = idEspectaculo;
        this.idUbicacion = idUbicacion;
        this.idVendedor = idVendedor;
        this.fechaVenta = fechaVenta;
        this.precioFinal = precioFinal;
        this.nombreCliente = nombreCliente;
        this.dniCliente = dniCliente;
        this.tipoPromocion = tipoPromocion; //NUEVO
    }


    //GETTERS Y SETTERS ORIGINALES -----

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

    //GETTER Y SETTER NUEVOS -----
    //Para el atributo tipoPromocion

    public String getTipoPromocion() {
        return tipoPromocion;
    }

    public void setTipoPromocion(String tipoPromocion) {
        this.tipoPromocion = tipoPromocion;
    }


    //toString MODIFICADO -----
    //Ahora incluye la promoción aplicada
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
                "\nDNI del cliente: " + dniCliente +
                "\nPromoción aplicada: " + tipoPromocion; //NUEVO
    }
}