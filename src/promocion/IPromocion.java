package promocion;

//¿POR QUÉ USAR UNA INTERFAZ?
//Porque queremos tratar TODAS las promociones de la misma forma,
//sin importar si es HappyHour, descuento de estudiante, etc.
//Esto se llama POLIMORFISMO


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