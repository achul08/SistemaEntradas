package service;

import dao.DaoException;
import dao.DaoUsuario;
import entidades.Usuario;
import java.util.List;

/*
 * SERVICE USUARIO
 * - Validar datos antes de guardar/modificar/eliminar
 * - Llamar al DAO para acceder a la BD
 * - Convertir DaoException a ServiceException
 *
 * TIENE 5 MÉTODOS
 * 1. insertar() - Crear un usuario nuevo
 * 2. modificar() - Actualizar un usuario existente
 * 3. eliminar() - Borrar un usuario
 * 4. consultar() - Buscar un usuario por ID
 * 5. consultarTodos() - Traer todos los usuarios
 */

public class ServiceUsuario {
    // ATRIBUTO: El DAO que va a usar este Service
    private DaoUsuario daoUsuario;

    // CONSTRUCTOR: Crea una instancia del DAO para poder usarlo
    public ServiceUsuario() {
        daoUsuario = new DaoUsuario();
    }


    // ═══════════════════════════════════════════════════════════════════════
    // METODO 1: INSERTAR (Crear un usuario nuevo)
    // ═══════════════════════════════════════════════════════════════════════
    public void insertar(Usuario usuario) throws ServiceException {
        try {
            // VALIDACIÓN 1: Nombre obligatorio
            // Verifico que no sea null y que después de sacar espacios no esté vacío
            if(usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre del usuario es obligatorio");
            }

            // VALIDACIÓN 2: Apellido obligatorio
            if(usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
                throw new ServiceException("El apellido del usuario es obligatorio");
            }

            // VALIDACIÓN 3: Username obligatorio
            if(usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                throw new ServiceException("El username es obligatorio");
            }

            // VALIDACIÓN 4: Password obligatorio
            if(usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                throw new ServiceException("La contraseña es obligatoria");
            }

            // VALIDACIÓN 5: Rol obligatorio
            if(usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
                throw new ServiceException("El rol es obligatorio");
            }

            // VALIDACIÓN 6: Rol válido (solo ADMINISTRADOR o VENDEDOR)
            // Convierto a mayúsculas para que no importe si escriben "administrador" o "ADMINISTRADOR"
            String rolMayuscula = usuario.getRol().trim().toUpperCase();

            // Verifico que sea una de las dos opciones válidas
            if(!rolMayuscula.equals("ADMINISTRADOR") && !rolMayuscula.equals("VENDEDOR")) {
                throw new ServiceException("El rol debe ser ADMINISTRADOR o VENDEDOR");
            }

            // Normalizo el rol (lo guardo en mayúsculas en la BD)
            usuario.setRol(rolMayuscula);

            // VALIDACIÓN 7: Username debe ser ÚNICO
            // Traigo TODOS los usuarios de la BD
            List<Usuario> usuarios = daoUsuario.consultarTodos();

            // Recorro cada usuario para ver si ya existe ese username
            for(Usuario u : usuarios) {
                // Comparo usernames ignorando mayúsculas/minúsculas
                // .equalsIgnoreCase() → "JUAN" es igual a "juan"
                if(u.getUsername().trim().equalsIgnoreCase(usuario.getUsername().trim())) {
                    throw new ServiceException("Ya existe un usuario con ese username");
                }
            }

            // Si pasó TODAS las validaciones, insertar en la BD
            daoUsuario.insertar(usuario);

        } catch (DaoException e) {
            // Si el DAO lanza una excepción (problema con H2),
            // la convierto a ServiceException
            throw new ServiceException("Error en base de datos");
        }
    }


    // ═══════════════════════════════════════════════════════════════════════
    // MÉTODO 2: MODIFICAR (Actualizar un usuario existente)
    // ═══════════════════════════════════════════════════════════════════════
    public void modificar(Usuario usuario) throws ServiceException {
        try {
            // VALIDACIÓN 0: Verificar que el usuario EXISTA antes de modificar
            // Busco en la BD si hay un usuario con ese ID
            Usuario existente = daoUsuario.consultar(usuario.getIdUsuario());

            // Si no existe (es null) o el ID es 0 (no se encontró), tiro error
            if(existente == null || existente.getIdUsuario() == 0) {
                throw new ServiceException("El usuario con ID " + usuario.getIdUsuario() + " no existe");
            }

            // VALIDACIÓN 1: Nombre obligatorio
            if(usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre del usuario es obligatorio");
            }

            // VALIDACIÓN 2: Apellido obligatorio
            if(usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
                throw new ServiceException("El apellido del usuario es obligatorio");
            }

            // VALIDACIÓN 3: Username obligatorio
            if(usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                throw new ServiceException("El username es obligatorio");
            }

            // VALIDACIÓN 4: Password obligatorio
            if(usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                throw new ServiceException("La contraseña es obligatoria");
            }

            // VALIDACIÓN 5: Rol obligatorio
            if(usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
                throw new ServiceException("El rol es obligatorio");
            }

            // VALIDACIÓN 6: Rol válido
            String rolMayuscula = usuario.getRol().trim().toUpperCase();
            if(!rolMayuscula.equals("ADMINISTRADOR") && !rolMayuscula.equals("VENDEDOR")) {
                throw new ServiceException("El rol debe ser ADMINISTRADOR o VENDEDOR");
            }
            usuario.setRol(rolMayuscula);

            // VALIDACIÓN 7: Username único (que no lo use OTRO usuario diferente)
            List<Usuario> usuarios = daoUsuario.consultarTodos();

            for(Usuario u : usuarios) {
                // Verifico si el username ya existe
                // PERO solo me importa si lo usa OTRO usuario (no el mismo que estoy modificando)
                // Por eso: && u.getIdUsuario() != usuario.getIdUsuario()
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


    // ═══════════════════════════════════════════════════════════════════════
    // MÉTODO 3: ELIMINAR (Borrar un usuario)
    // ═══════════════════════════════════════════════════════════════════════
    public void eliminar(int id) throws ServiceException {
        try {
            // VALIDACIÓN: Verificar que el usuario EXISTA antes de eliminar
            // Porque si intento eliminar un ID que no existe, puede dar error raro
            Usuario usuario = daoUsuario.consultar(id);

            if(usuario == null || usuario.getIdUsuario() == 0) {
                throw new ServiceException("El usuario con ID " + id + " no existe");
            }

            // Si existe, eliminar de la BD
            daoUsuario.eliminar(id);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    // ═══════════════════════════════════════════════════════════════════════
    // MÉTODO 4: CONSULTAR (Buscar un usuario por ID)
    // ═══════════════════════════════════════════════════════════════════════
    /*
     * Este método NO hace validaciones, solo busca.
     * Devuelve:
     * - El objeto Usuario si lo encuentra
     * - Un Usuario vacío (con ID=0) si no lo encuentra
     */
    public Usuario consultar(int id) throws ServiceException {
        try {
            // Le pido al DAO que busque el usuario con ese ID
            return daoUsuario.consultar(id);

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    // ═══════════════════════════════════════════════════════════════════════
    // MÉTODO 5: CONSULTAR TODOS (Obtener todos los usuarios)
    // ═══════════════════════════════════════════════════════════════════════
    /*
     * Este método NO hace validaciones, solo trae todos los usuarios.
     * Devuelve:
     * - Una lista con todos los usuarios
     * - Una lista vacía si no hay ninguno
     */
    public List<Usuario> consultarTodos() throws ServiceException {
        try {
            // Le pido al DAO que traiga TODOS los usuarios de la BD
            return daoUsuario.consultarTodos();

        } catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }
}