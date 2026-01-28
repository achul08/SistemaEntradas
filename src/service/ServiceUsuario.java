package service;

import dao.DaoException;
import dao.DaoUsuario;
import entidades.Usuario;
import entidades.Administrador;
import entidades.Vendedor;
import java.util.List;

/*
 * SERVICE USUARIO - modificado para trabajar con herencia

 */

public class ServiceUsuario {
    // ATRIBUTO: El DAO que va a usar este Service
    private DaoUsuario daoUsuario;

    // CONSTRUCTOR: Crea una instancia del DAO para poder usarlo
    public ServiceUsuario() {
        daoUsuario = new DaoUsuario();
    }


    // METODOS --------------
    // INSERTAR (Crear un usuario nuevo)
    public void insertar(Usuario usuario) throws ServiceException {
        try {
            if(usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre del usuario es obligatorio");
            }
            if(usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
                throw new ServiceException("El apellido del usuario es obligatorio");
            }

            if(usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                throw new ServiceException("El username es obligatorio");
            }

            if(usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                throw new ServiceException("La contraseña es obligatoria");
            }

            // getRol() devuelve "ADMINISTRADOR" o "VENDEDOR" según la clase
            String rol = usuario.getRol();

            if(!rol.equals("ADMINISTRADOR") && !rol.equals("VENDEDOR")) {
                throw new ServiceException("El rol debe ser ADMINISTRADOR o VENDEDOR");
            }

            List<Usuario> usuarios = daoUsuario.consultarTodos();

            for(Usuario u : usuarios) {
                if(u.getUsername().trim().equalsIgnoreCase(usuario.getUsername().trim())) {
                    throw new ServiceException("Ya existe un usuario con ese username");
                }
            }
            daoUsuario.insertar(usuario);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }



    // MODIFICAR (Actualizar un usuario existente)
    public void modificar(Usuario usuario) throws ServiceException {
        try {
            Usuario existente = daoUsuario.consultar(usuario.getIdUsuario());

            if(existente == null || existente.getIdUsuario() == 0) {
                throw new ServiceException("El usuario con ID " + usuario.getIdUsuario() + " no existe");
            }
            if(usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre del usuario es obligatorio");
            }
            if(usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
                throw new ServiceException("El apellido del usuario es obligatorio");
            }
            if(usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                throw new ServiceException("El username es obligatorio");
            }
            if(usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                throw new ServiceException("La contraseña es obligatoria");
            }
            String rol = usuario.getRol();

            if(!rol.equals("ADMINISTRADOR") && !rol.equals("VENDEDOR")) {
                throw new ServiceException("El rol debe ser ADMINISTRADOR o VENDEDOR");
            }
            List<Usuario> usuarios = daoUsuario.consultarTodos();

            for(Usuario u : usuarios) {
                if(u.getUsername().trim().equalsIgnoreCase(usuario.getUsername().trim())
                        && u.getIdUsuario() != usuario.getIdUsuario()) {
                    throw new ServiceException("Existe otro usuario con ese username. Cambiar username");
                }
            }

            // Si pasó todas las validaciones, modificar en la BD
            daoUsuario.modificar(usuario);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }



    //ELIMINAR (Borrar un usuario)
    public void eliminar(int id) throws ServiceException {
        try {
            Usuario usuario = daoUsuario.consultar(id);

            if(usuario == null || usuario.getIdUsuario() == 0) {
                throw new ServiceException("El usuario con ID " + id + " no existe");
            }
            if(usuario.getRol().equals("VENDEDOR")) {
                dao.DaoVenta daoVenta = new dao.DaoVenta();
                java.util.List<entidades.Venta> ventasDelVendedor = daoVenta.consultarPorVendedor(id);

                if(!ventasDelVendedor.isEmpty()) {
                    throw new ServiceException(
                            "No se puede eliminar el vendedor porque tiene " +
                                    ventasDelVendedor.size() +
                                    " venta(s) registrada(s). " +
                                    "Los registros de ventas deben conservarse por motivos contables."
                    );
                }
            }

            daoUsuario.eliminar(id);

        } catch (dao.DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }



    // CONSULTAR (Buscar un usuario por ID)
    public Usuario consultar(int id) throws ServiceException {
        try {
            // Le pido al DAO que busque el usuario con ese ID
            // El DAO devuelve un Administrador o Vendedor según lo que haya en la BD
            return daoUsuario.consultar(id);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //CONSULTAR TODOS (Obtener todos los usuarios)
    public List<Usuario> consultarTodos() throws ServiceException {
        try {
            // Le pido al DAO que traiga TODOS los usuarios de la BD
            // La lista puede contener objetos Administrador y Vendedor mezclados
            return daoUsuario.consultarTodos();

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }
}