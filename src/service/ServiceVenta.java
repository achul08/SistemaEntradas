package service;
import dao.DaoException;
import dao.DaoVenta;
import entidades.Venta;


import java.util.List;

public class ServiceVenta {
    private DaoVenta daoVenta;

    public ServiceVenta() {
        daoVenta = new DaoVenta();
    }

    public void insertar(Venta venta) throws ServiceException {
        try {
            daoVenta.insertar(venta);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }
}
