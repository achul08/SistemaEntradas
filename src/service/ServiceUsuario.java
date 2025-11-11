package service;

//recibe/usa el dao.
//Tiene metodos que llaman a los metodos del dao. Hago esto para cuando lo tenga que poner a trabajar con un servidor externo y no con h2
//uso el service como intermediario porque aca puedo cambiar rapidamente el dao sin que me afecte el programa
//un service por cada dao

import dao.DaoException;
import dao.DaoUsuario;
import entidades.Usuario;

import java.util.List;

public class ServiceUsuario {
    private DaoUsuario daoUsuario;

    public ServiceUsuario() {
        daoUsuario = new DaoUsuario();
    }

    public void insertar(Usuario usuario) throws ServiceException {
        try {
            daoUsuario.insertar(usuario);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }
}
