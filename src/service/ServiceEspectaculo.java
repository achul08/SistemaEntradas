package service;
import dao.DaoException;
import dao.DaoEspectaculo;
import entidades.Espectaculo;


import java.util.List;

public class ServiceEspectaculo {
    private DaoEspectaculo daoEspectaculo;

    public ServiceEspectaculo() {
        daoEspectaculo = new DaoEspectaculo();
    }

    public void insertar(Espectaculo espectaculo) throws ServiceException {
        try {
                daoEspectaculo.insertar(espectaculo);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }
}
