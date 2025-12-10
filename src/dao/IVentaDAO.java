package dao;

import entidades.Venta;
import java.util.Date;
import java.util.List;

//IVentaDAO - Interfaz específica para operaciones de Venta
//Hereda de IDAO<Venta> (tiene los 5 métodos básicos)
//Y agrega métodos específicos para consultas de ventas

//RELACIÓN CON OTRAS CLASES:
//- Hereda de IDAO<Venta> (extends)
//- DaoVenta implementa IVentaDAO (implements)

public interface IVentaDAO extends IDAO<Venta> {

    //MÉTODOS BÁSICOS (heredados de IDAO):
    //- insertar(Venta elemento)
    //- modificar(Venta elemento)
    //- eliminar(int id)
    //- consultar(int id)
    //- consultarTodos()


    //MÉTODOS ESPECÍFICOS DE VENTA -----

    //consultarPorVendedor() - Obtiene todas las ventas realizadas por un vendedor específico
    //Esto es útil para que cada vendedor pueda ver sus propias ventas
    //Parámetro: idVendedor - el ID del vendedor
    //Devuelve: List<Venta> con todas las ventas de ese vendedor
    public List<Venta> consultarPorVendedor(int idVendedor) throws DaoException;


    //consultarPorEspectaculo() - Obtiene todas las ventas de un espectáculo específico
    //Esto es útil para reportes de cuánto se vendió por evento
    //Parámetro: idEspectaculo - el ID del espectáculo
    //Devuelve: List<Venta> con todas las ventas de ese espectáculo
    public List<Venta> consultarPorEspectaculo(int idEspectaculo) throws DaoException;


    //consultarPorFecha() - Obtiene todas las ventas en un rango de fechas
    //Esto es útil para reportes mensuales, semanales, etc.
    //Parámetros:
    //  fechaInicio - fecha desde (inclusive)
    //  fechaFin - fecha hasta (inclusive)
    //Devuelve: List<Venta> con todas las ventas en ese rango
    public List<Venta> consultarPorFecha(Date fechaInicio, Date fechaFin) throws DaoException;


    //consultarPorEspectaculoYFecha() - Obtiene las ventas de un espectáculo en un rango de fechas
    //Esto es útil para reportes específicos como "ventas del concierto X entre marzo y abril"
    //Parámetros:
    //  idEspectaculo - el ID del espectáculo
    //  fechaInicio - fecha desde (inclusive)
    //  fechaFin - fecha hasta (inclusive)
    //Devuelve: List<Venta> con las ventas que cumplan ambas condiciones
    public List<Venta> consultarPorEspectaculoYFecha(int idEspectaculo, Date fechaInicio, Date fechaFin) throws DaoException;
}