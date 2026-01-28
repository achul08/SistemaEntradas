package promocion;

import java.util.Calendar;

//CLASE PromocionHappyHour
//Implementa la interfaz IPromocion
//Aplica 20% de descuento SOLO de 5am a 6am
//
//CONCEPTO POO: IMPLEMENTACIÓN DE INTERFAZ
//Esta clase IMPLEMENTA los 3 métodos que definió IPromocion
//
//RELACIÓN CON OTRAS CLASES:
//- Implementa IPromocion (implements)
//- Se usa en FormularioVenta para calcular el precio final


public class PromocionHappyHour implements IPromocion {

    //ATRIBUTOS -----
    private final int HORA_INICIO = 5;  //5am
    private final int HORA_FIN = 6;     //6am
    private final double PORCENTAJE_DESCUENTO = 0.20; //20% = 0.20


    //CONSTRUCTOR -----
    public PromocionHappyHour() {

    }



    @Override
    public double aplicarDescuento(double precioOriginal) {
        if (esHorarioHappyHour()) {
            double descuento = precioOriginal * PORCENTAJE_DESCUENTO;
            double precioFinal = precioOriginal - descuento;
            return precioFinal;

        } else {
            return precioOriginal;
        }
    }


    @Override
    public String getNombre() {
        return "Happy Hour";
    }


    @Override
    public String getDescripcion() {
        return "20% de descuento de 5am a 6am";
    }


    private boolean esHorarioHappyHour() {
        Calendar ahora = Calendar.getInstance(); //obtiene fecha/hora actual
        int horaActual = ahora.get(Calendar.HOUR_OF_DAY);
        return horaActual >= HORA_INICIO && horaActual < HORA_FIN;
    }



    @Override
    public String toString() {
        return getNombre() + " - " + getDescripcion();
    }
}