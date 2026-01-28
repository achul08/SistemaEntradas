package entidades;


public class Abono {
    private double valorAbono;

    public Abono() {
        this.valorAbono = 0;
    }


    public Abono(double valorAbono) {
        this.valorAbono = valorAbono;
    }


    public double calcularPrecioFinal(double precioBase) {
        //Restar el valor del abono al precio base
        double precioFinal = precioBase - valorAbono;

        //Verificar que no sea negativo
        //Si el abono vale más que la entrada, el precio final es $0
        if (precioFinal < 0) {
            precioFinal = 0;
        }

        //Devolver el precio final
        return precioFinal;
    }



    public boolean tieneAbono() {
        return valorAbono > 0;
    }


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


    @Override
    public String toString() {
        return getDescripcion();
    }
}