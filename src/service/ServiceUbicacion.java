package service;

//SERVICE UBICACION - Lógica de negocio para la entidad Ubicacion
//Valida datos y llama al DAO

import dao.DaoUbicacion;
import dao.DaoEstadio;
import dao.DaoException;
import entidades.Ubicacion;
import entidades.Estadio;
import java.util.List;
import dao.DaoVenta;
import entidades.Venta;


public class ServiceUbicacion {
    //ATRIBUTOS -----
    private DaoUbicacion daoUbicacion;
    private DaoEstadio daoEstadio;
    private DaoVenta daoVenta;

    //CONSTRUCTOR -----
    public ServiceUbicacion() {
        daoUbicacion = new DaoUbicacion();
        daoEstadio = new DaoEstadio();
        daoVenta = new DaoVenta();
    }


    //METODOS -------------
    //INSERTAR (Crear una ubicación nueva) -----
    public void insertar(Ubicacion ubicacion) throws ServiceException {
        try {
            if(ubicacion.getNombre() == null || ubicacion.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre de la ubicación es obligatorio");
            }
            if(ubicacion.getPrecio() <= 0) {
                throw new ServiceException("El precio debe ser mayor a 0");
            }
            if(ubicacion.getCapacidad() <= 0) {
                throw new ServiceException("La capacidad debe ser mayor a 0");
            }
            Estadio estadio = daoEstadio.consultar(ubicacion.getIdEstadio());
            if(estadio == null || estadio.getIdEstadio() == 0) {
                throw new ServiceException("El estadio con ID " + ubicacion.getIdEstadio() + " no existe");
            }

            List<Ubicacion> ubicaciones = daoUbicacion.consultarTodos();

            for(Ubicacion u : ubicaciones) {
                if(u.getNombre().trim().equalsIgnoreCase(ubicacion.getNombre().trim())
                        && u.getIdEstadio() == ubicacion.getIdEstadio()) {
                    throw new ServiceException("Ya existe una ubicación con ese nombre en este estadio");
                }
            }

            int capacidadUsada = 0;
            for(Ubicacion u : ubicaciones) {
                if(u.getIdEstadio() == ubicacion.getIdEstadio()) {
                    capacidadUsada += u.getCapacidad();
                }
            }
            capacidadUsada += ubicacion.getCapacidad();

            if(capacidadUsada > estadio.getCapacidadTotal()) {
                throw new ServiceException("La capacidad de las ubicaciones supera la capacidad total del estadio. " +
                        "Capacidad disponible: " + (estadio.getCapacidadTotal() - (capacidadUsada - ubicacion.getCapacidad())));
            }

            //si pasó todas las validaciones, insertar
            daoUbicacion.insertar(ubicacion);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }



    //MODIFICAR (Actualizar una ubicación existente)
    public void modificar(Ubicacion ubicacion) throws ServiceException {
        try {
            Ubicacion existente = daoUbicacion.consultar(ubicacion.getIdUbicacion());
            if(existente == null || existente.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación con ID " + ubicacion.getIdUbicacion() + " no existe");
            }
            if(ubicacion.getNombre() == null || ubicacion.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre de la ubicación es obligatorio");
            }
            if(ubicacion.getPrecio() <= 0) {
                throw new ServiceException("El precio debe ser mayor a 0");
            }
            if(ubicacion.getCapacidad() <= 0) {
                throw new ServiceException("La capacidad debe ser mayor a 0");
            }
            Estadio estadio = daoEstadio.consultar(ubicacion.getIdEstadio());
            if(estadio == null || estadio.getIdEstadio() == 0) {
                throw new ServiceException("El estadio con ID " + ubicacion.getIdEstadio() + " no existe");
            }
            List<Ubicacion> ubicaciones = daoUbicacion.consultarTodos();

            for(Ubicacion u : ubicaciones) {
                if(u.getNombre().trim().equalsIgnoreCase(ubicacion.getNombre().trim())
                        && u.getIdEstadio() == ubicacion.getIdEstadio()
                        && u.getIdUbicacion() != ubicacion.getIdUbicacion()) {
                    throw new ServiceException("Existe otra ubicación con ese nombre en este estadio. Cambiar nombre");
                }
            }

            int capacidadUsada = 0;
            for(Ubicacion u : ubicaciones) {
                if(u.getIdEstadio() == ubicacion.getIdEstadio() && u.getIdUbicacion() != ubicacion.getIdUbicacion()) {
                    capacidadUsada += u.getCapacidad();
                }
            }
            capacidadUsada += ubicacion.getCapacidad();

            if(capacidadUsada > estadio.getCapacidadTotal()) {
                throw new ServiceException("La capacidad de las ubicaciones supera la capacidad total del estadio. " +
                        "Capacidad disponible: " + (estadio.getCapacidadTotal() - (capacidadUsada - ubicacion.getCapacidad())));
            }

            //si pasó todas las validaciones, modificar
            daoUbicacion.modificar(ubicacion);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }



    //ELIMINAR (Borrar una ubicación)
    public void eliminar(int id) throws ServiceException {
        try {
            Ubicacion ubicacion = daoUbicacion.consultar(id);
            if(ubicacion == null || ubicacion.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación con ID " + id + " no existe");
            }
            List<Venta> ventasDeLaUbicacion = daoVenta.consultarPorUbicacion(id);

            if (!ventasDeLaUbicacion.isEmpty()) {
                throw new ServiceException(
                        "No se puede eliminar la ubicación porque tiene " +
                                ventasDeLaUbicacion.size() +
                                " venta(s) registrada(s). " +
                                "Los registros de ventas deben conservarse por motivos contables."
                );
            }

            daoUbicacion.eliminar(id);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //CONSULTAR (Buscar una ubicación por ID)
    public Ubicacion consultar(int id) throws ServiceException {
        try {
            return daoUbicacion.consultar(id);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //CONSULTAR TODOS (Obtener todas las ubicaciones)
    public List<Ubicacion> consultarTodos() throws ServiceException {
        try {
            return daoUbicacion.consultarTodos();
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //CONSULTAR POR ESTADIO (Obtener las ubicaciones de un estadio específico)
    //Este método es útil para:
    //- Mostrar las ubicaciones de un estadio en el formulario de venta
    //- Listar las ubicaciones al gestionar un estadio
    public List<Ubicacion> consultarPorEstadio(int idEstadio) throws ServiceException {
        try {
            //traigo todas las ubicaciones
            List<Ubicacion> todasLasUbicaciones = daoUbicacion.consultarTodos();

            //filtro solo las del estadio que me piden
            List<Ubicacion> ubicacionesDelEstadio = new java.util.ArrayList<>();
            for(Ubicacion u : todasLasUbicaciones) {
                if(u.getIdEstadio() == idEstadio) {
                    ubicacionesDelEstadio.add(u);
                }
            }

            return ubicacionesDelEstadio;
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }
}