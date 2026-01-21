package promocion;

//CLASE SinPromocion
//Implementa la interfaz IPromocion
//NO aplica ningún descuento (0%)
//
//CONCEPTO POO: IMPLEMENTACIÓN DE INTERFAZ
//Esta clase también implementa IPromocion, pero devuelve 0% de descuento
//
//RELACIÓN CON OTRAS CLASES:
//- Implementa IPromocion (implements)
//- Se usa en FormularioVenta cuando NO hay Happy Hour
//
//¿POR QUÉ CREAR ESTA CLASE?
//Para usar POLIMORFISMO: tanto PromocionHappyHour como SinPromocion
//implementan la misma interfaz, entonces podemos tratarlas igual


public class SinPromocion implements IPromocion {

    //CONSTRUCTOR -----
    //Constructor vacío porque no necesita atributos
    public SinPromocion() {
        //No hace nada, solo existe para crear objetos SinPromocion
    }


    //MÉTODO 1: aplicarDescuento() -----
    //Este método es OBLIGATORIO porque está en la interfaz
    //@Override indica que estamos implementando un método de la interfaz
    @Override
    public double aplicarDescuento(double precioOriginal) {
        //NO hay descuento → devolver el precio original sin modificar
        //Es decir: descuento de 0%
        return precioOriginal;
    }


    //MÉTODO 2: getNombre() -----
    //Devuelve el nombre de la "promoción" (en este caso, ninguna)
    @Override
    public String getNombre() {
        return "Sin promoción";
    }


    //MÉTODO 3: getDescripcion() -----
    //Devuelve la descripción
    @Override
    public String getDescripcion() {
        return "Precio normal";
    }


    //MÉTODO toString() -----
    //Para debugging o mostrar en consola
    @Override
    public String toString() {
        return getNombre();
    }
}

/*
¿Por qué crear una clase que NO hace nada?
Para usar POLIMORFISMO.
En FormularioVenta vamos a escribir:
javaIPromocion promocion;  // Variable de tipo INTERFAZ

if (esHappyHour) {
    promocion = new PromocionHappyHour();  // 20% descuento
} else {
    promocion = new SinPromocion();        // 0% descuento
}

// Usar la promoción (sin importar cuál es)
double precioFinal = promocion.aplicarDescuento(precioOriginal);
Ventaja: El código que usa la promoción NO necesita saber si es Happy Hour o no. Simplemente llama a aplicarDescuento() y la clase correcta hace su trabajo.
 */
