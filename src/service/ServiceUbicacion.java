package service;
import dao.DaoException;
import dao.DaoUbicacion;
import entidades.Ubicacion;


import java.util.List;

public class ServiceUbicacion {
    private DaoUbicacion daoUbicacion;

    public ServiceUbicacion() {
        daoUbicacion = new DaoUbicacion();
    }

    public void insertar(Ubicacion ubicacion) throws ServiceException {
        try {
            daoUbicacion.insertar(ubicacion);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }

}
