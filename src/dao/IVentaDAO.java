package dao;

import entidades.Venta;
import java.util.Date;
import java.util.List;

public interface IVentaDAO extends IDAO<Venta> {

    //MÉTODOS BÁSICOS (heredados de IDAO):
    //- insertar(Venta elemento)
    //- modificar(Venta elemento)
    //- eliminar(int id)
    //- consultar(int id)
    //- consultarTodos()


    //MÉTODOS ESPECÍFICOS DE VENTA -----

    //consultarPorVendedor() - Obtiene todas las ventas realizadas por un vendedor específico
    public List<Venta> consultarPorVendedor(int idVendedor) throws DaoException;


    //consultarPorEspectaculo() - Obtiene todas las ventas de un espectáculo específico
    public List<Venta> consultarPorEspectaculo(int idEspectaculo) throws DaoException;


    //consultarPorUbicacion() - Obtiene todas las ventas de una ubicación específica
    //NUEVO - Agregado para validar eliminación de ubicaciones
    public List<Venta> consultarPorUbicacion(int idUbicacion) throws DaoException;


    //consultarPorFecha() - Obtiene todas las ventas en un rango de fechas
    public List<Venta> consultarPorFecha(Date fechaInicio, Date fechaFin) throws DaoException;


    //consultarPorEspectaculoYFecha() - Obtiene las ventas de un espectáculo en un rango de fechas
    public List<Venta> consultarPorEspectaculoYFecha(int idEspectaculo, Date fechaInicio, Date fechaFin) throws DaoException;
}