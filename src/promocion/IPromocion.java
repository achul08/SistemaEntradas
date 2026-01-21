package promocion;

//INTERFAZ IPromocion
//Define el CONTRATO que todas las promociones deben cumplir
//
//CONCEPTO POO: INTERFAZ
//Una interfaz define QUÉ debe hacer una clase, pero NO CÓMO lo hace
//
//RELACIÓN CON OTRAS CLASES:
//- PromocionHappyHour implements IPromocion
//- SinPromocion implements IPromocion
//
//¿POR QUÉ USAR UNA INTERFAZ?
//Porque queremos tratar TODAS las promociones de la misma forma,
//sin importar si es HappyHour, descuento de estudiante, etc.
//Esto se llama POLIMORFISMO


/*
¿Qué es una INTERFAZ?
Es como un contrato que dice:

"Toda promoción DEBE tener estos 3 métodos"
"Pero cada promoción decide CÓMO los implementa"

¿Por qué 3 métodos?

aplicarDescuento() → hace el cálculo del precio
getNombre() → para mostrar "Happy Hour" en reportes
getDescripcion() → para mostrar "20% de descuento"

¿Por qué NO tienen cuerpo (implementación)?
Porque cada clase hija va a tener su propia versión:

PromocionHappyHour → descuento de 20%
SinPromocion → descuento de 0%
 */

/*
public interface IPromocion {

    //MÉTODO 1: aplicarDescuento()
    //Calcula el precio final después de aplicar el descuento
    //
    //Parámetro:
    // - precioOriginal: el precio base de la entrada (sin descuento)
    //
    //Devuelve:
    // - el precio final (con descuento aplicado)
    //
    //Ejemplo:
    // Si precioOriginal = 1000 y hay 20% de descuento
    // Devuelve: 800
    //
    //IMPORTANTE: Este método NO tiene cuerpo (no tiene implementación)
    //Cada clase que implemente IPromocion debe escribir su propia versión
    public double aplicarDescuento(double precioOriginal);


    //MÉTODO 2: getNombre()
    //Devuelve el nombre de la promoción para mostrar en reportes
    //
    //Devuelve:
    // - un String con el nombre (ejemplo: "Happy Hour 5am-6am")
    //
    //IMPORTANTE: También es un método abstracto (sin cuerpo)
    public String getNombre();


    //MÉTODO 3: getDescripcion()
    //Devuelve una descripción detallada de la promoción
    //
    //Devuelve:
    // - un String con la descripción (ejemplo: "20% de descuento")
    //
    //IMPORTANTE: También es un método abstracto (sin cuerpo)
    public String getDescripcion();
}
*/

public interface IPromocion {

    public double aplicarDescuento(double precioOriginal);

    public String getNombre();

    public String getDescripcion();
}