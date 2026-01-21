package entidades;

//CLASE Abono
//Representa un abono que un cliente puede tener
//El abono tiene un valor monetario que se descuenta del precio de la entrada
//
//CONCEPTO POO: ENCAPSULAMIENTO
//Los atributos son privados y se accede a ellos mediante getters/setters
//El cálculo del precio final está encapsulado en un método
//
//RELACIÓN CON OTRAS CLASES:
//- Se usa en Venta (composición)
//- Se usa en FormularioVenta para calcular el precio final


public class Abono {

    //ATRIBUTO -----
    //Valor monetario del abono
    //Ejemplo: si valorAbono = 500, el cliente tiene $500 de descuento
    //Si valorAbono = 0, el cliente no tiene abono
    private double valorAbono;


    //CONSTRUCTOR VACÍO -----
    //Crea un abono sin valor (0)
    public Abono() {
        this.valorAbono = 0;
    }


    //CONSTRUCTOR CON PARÁMETROS -----
    //Crea un abono con un valor específico
    //
    //Parámetro:
    // - valorAbono: el valor monetario del abono (ejemplo: 500)
    public Abono(double valorAbono) {
        this.valorAbono = valorAbono;
    }


    //MÉTODO: calcularPrecioFinal() -----
    //Calcula el precio que debe pagar el cliente después de aplicar el abono
    //
    //CONCEPTO POO: ENCAPSULAMIENTO
    //La lógica de cálculo está encapsulada en este método
    //El código que usa Abono no necesita saber CÓMO se calcula
    //
    //Parámetro:
    // - precioBase: el precio de la entrada sin descuentos
    //
    //Devuelve:
    // - el precio final que debe pagar el cliente
    //
    //Ejemplos:
    // - precioBase = 1000, valorAbono = 0 → devuelve 1000 (sin abono)
    // - precioBase = 1000, valorAbono = 500 → devuelve 500 (con abono de $500)
    // - precioBase = 1000, valorAbono = 1200 → devuelve 0 (abono cubre todo)
    public double calcularPrecioFinal(double precioBase) {
        //PASO 1: Restar el valor del abono al precio base
        double precioFinal = precioBase - valorAbono;

        //PASO 2: Verificar que no sea negativo
        //Si el abono vale más que la entrada, el precio final es $0
        //Ejemplo: entrada de $500 con abono de $800 → precio final = $0
        if (precioFinal < 0) {
            precioFinal = 0;
        }

        //PASO 3: Devolver el precio final
        return precioFinal;
    }


    //MÉTODO: tieneAbono() -----
    //Verifica si el cliente tiene un abono (valor mayor a 0)
    //
    //Devuelve:
    // - true si tiene abono (valorAbono > 0)
    // - false si no tiene abono (valorAbono = 0)
    //
    //Ejemplo de uso:
    // if (abono.tieneAbono()) {
    //     System.out.println("Cliente tiene abono");
    // }
    public boolean tieneAbono() {
        return valorAbono > 0;
    }


    //MÉTODO: getDescripcion() -----
    //Devuelve una descripción legible del abono
    //
    //Devuelve:
    // - "Sin abono" si valorAbono = 0
    // - "Abono de $XXX" si tiene valor
    //
    //Ejemplo:
    // - valorAbono = 0 → "Sin abono"
    // - valorAbono = 500 → "Abono de $500.00"
    public String getDescripcion() {
        if (valorAbono == 0) {
            return "Sin abono";
        } else {
            //Formatear el valor con 2 decimales
            return "Abono de $" + String.format("%.2f", valorAbono);
        }
    }


    //GETTERS Y SETTERS -----

    public double getValorAbono() {
        return valorAbono;
    }

    public void setValorAbono(double valorAbono) {
        this.valorAbono = valorAbono;
    }


    //MÉTODO toString() -----
    //Para debugging
    @Override
    public String toString() {
        return getDescripcion();
    }
}