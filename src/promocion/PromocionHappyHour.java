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
    //Son constantes (final) porque nunca cambian
    private final int HORA_INICIO = 5;  //5am
    private final int HORA_FIN = 6;     //6am
    private final double PORCENTAJE_DESCUENTO = 0.20; //20% = 0.20


    //CONSTRUCTOR -----
    //No necesita parámetros porque los valores son fijos
    public PromocionHappyHour() {
        //Constructor vacío
        //Los atributos ya tienen sus valores por defecto
    }


    //MÉTODO 1: aplicarDescuento() -----
    //Este método es OBLIGATORIO porque está en la interfaz
    //@Override indica que estamos implementando un método de la interfaz
    @Override
    public double aplicarDescuento(double precioOriginal) {
        //PASO 1: Verificar si estamos en el horario de Happy Hour
        if (esHorarioHappyHour()) {
            //Estamos en Happy Hour → aplicar 20% de descuento
            //Fórmula: precioFinal = precioOriginal - (precioOriginal * 0.20)
            //Ejemplo: 1000 - (1000 * 0.20) = 1000 - 200 = 800
            double descuento = precioOriginal * PORCENTAJE_DESCUENTO;
            double precioFinal = precioOriginal - descuento;
            return precioFinal;

        } else {
            //NO estamos en Happy Hour → devolver precio original sin descuento
            return precioOriginal;
        }
    }


    //MÉTODO 2: getNombre() -----
    //Devuelve el nombre de la promoción para mostrar en reportes
    @Override
    public String getNombre() {
        return "Happy Hour";
    }


    //MÉTODO 3: getDescripcion() -----
    //Devuelve la descripción completa
    @Override
    public String getDescripcion() {
        return "20% de descuento de 5am a 6am";
    }


    //MÉTODO AUXILIAR PRIVADO: esHorarioHappyHour() -----
    //Verifica si la hora ACTUAL está dentro del rango de Happy Hour
    //
    //Devuelve:
    // - true si estamos entre las 5am y 6am
    // - false en cualquier otro horario
    //
    //IMPORTANTE: Este método es PRIVADO (solo lo usa esta clase)
    //NO está en la interfaz, es un método auxiliar interno
    private boolean esHorarioHappyHour() {
        //PASO 1: Obtener la hora actual del sistema
        //Calendar es una clase de Java que maneja fechas y horas
        Calendar ahora = Calendar.getInstance(); //obtiene fecha/hora actual

        //PASO 2: Extraer solo la HORA (0-23)
        //Calendar.HOUR_OF_DAY devuelve la hora en formato 24hs
        //Ejemplo: si son las 5:30am → horaActual = 5
        //Ejemplo: si son las 17:00 (5pm) → horaActual = 17
        int horaActual = ahora.get(Calendar.HOUR_OF_DAY);

        //PASO 3: Verificar si está en el rango [5, 6)
        //horaActual >= HORA_INICIO → es 5am o más tarde
        //horaActual < HORA_FIN → es antes de las 6am
        //
        //Ejemplos:
        // - 4:59am → false (es antes de las 5)
        // - 5:00am → true (es las 5)
        // - 5:30am → true (es entre 5 y 6)
        // - 6:00am → false (ya pasó la hora)
        // - 10:00am → false (es después de las 6)
        return horaActual >= HORA_INICIO && horaActual < HORA_FIN;
    }


    //MÉTODO toString() -----
    //Para debugging o mostrar la promo en consola
    @Override
    public String toString() {
        return getNombre() + " - " + getDescripcion();
    }
}