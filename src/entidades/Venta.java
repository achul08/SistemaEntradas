package entidades;

import java.sql.Timestamp;

//ENTIDAD VENTA - MODIFICADA
//Ahora incluye:
// - tipoPromocion: qué promoción se aplicó (Happy Hour, etc.)
// - valorAbono: cuánto vale el abono del cliente
// - precioOriginal: precio de la entrada sin descuentos (para reportes)


public class Venta {
    //ATRIBUTOS ORIGINALES -----
    private int idVenta;
    private int idEspectaculo;
    private int idUbicacion;
    private int idVendedor;
    private Timestamp fechaVenta;
    private double precioFinal; //precio que pagó el cliente (con descuentos aplicados)
    private String nombreCliente;
    private String dniCliente;

    //ATRIBUTOS NUEVOS (BONUS POINT 1: PROMOCIONES) -----
    private String tipoPromocion; //ejemplo: "Happy Hour", "Sin promoción"

    //ATRIBUTOS NUEVOS (BONUS POINT 2: ABONOS) -----
    private double valorAbono; //cuánto vale el abono del cliente (0 si no tiene)
    private double precioOriginal; //precio de la ubicación SIN descuentos (para prorrateo)
    //Ejemplo: Si la entrada cuesta $1000, el abono vale $500, y hay happy hour (20% desc):
    // - precioOriginal = 1000 (precio base de la ubicación)
    // - valorAbono = 500 (lo que descuenta el abono)
    // - precioFinal = 400 (después de aplicar promoción 20% a los $500 restantes)


    //CONSTRUCTOR VACÍO -----
    public Venta() {
    }

    //CONSTRUCTOR CON PARÁMETROS (SIN ID porque es auto_increment) -----
    //MODIFICADO: Ahora incluye tipoPromocion, valorAbono, precioOriginal
    public Venta(int idEspectaculo, int idUbicacion, int idVendedor, Timestamp fechaVenta,
                 double precioFinal, String nombreCliente, String dniCliente,
                 String tipoPromocion, double valorAbono, double precioOriginal) {
        this.idEspectaculo = idEspectaculo;
        this.idUbicacion = idUbicacion;
        this.idVendedor = idVendedor;
        this.fechaVenta = fechaVenta;
        this.precioFinal = precioFinal;
        this.nombreCliente = nombreCliente;
        this.dniCliente = dniCliente;
        this.tipoPromocion = tipoPromocion;
        this.valorAbono = valorAbono;
        this.precioOriginal = precioOriginal;
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

    //GETTERS Y SETTERS NUEVOS (PROMOCIONES) -----

    public String getTipoPromocion() {
        return tipoPromocion;
    }

    public void setTipoPromocion(String tipoPromocion) {
        this.tipoPromocion = tipoPromocion;
    }

    //GETTERS Y SETTERS NUEVOS (ABONOS) -----

    public double getValorAbono() {
        return valorAbono;
    }

    public void setValorAbono(double valorAbono) {
        this.valorAbono = valorAbono;
    }

    public double getPrecioOriginal() {
        return precioOriginal;
    }

    public void setPrecioOriginal(double precioOriginal) {
        this.precioOriginal = precioOriginal;
    }


    //toString MODIFICADO -----
    //Ahora incluye la promoción y el abono
    @Override
    public String toString() {
        return "Venta\n" +
                "id de la venta: " + idVenta +
                "\nid del espectaculo: " + idEspectaculo +
                "\nid de la ubicacion: " + idUbicacion +
                "\nid del vendedor: " + idVendedor +
                "\nFecha de la venta: " + fechaVenta +
                "\nPrecio original: " + precioOriginal +
                "\nValor del abono: " + valorAbono +
                "\nPromoción aplicada: " + tipoPromocion +
                "\nPrecio final: " + precioFinal +
                "\nNombre del cliente: " + nombreCliente +
                "\nDNI del cliente: " + dniCliente;
    }
}