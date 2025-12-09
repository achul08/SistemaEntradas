package service;

//SERVICE UBICACION - Lógica de negocio para la entidad Ubicacion
//Valida datos y llama al DAO

import dao.DaoUbicacion;
import dao.DaoEstadio;
import dao.DaoException;
import entidades.Ubicacion;
import entidades.Estadio;
import java.util.List;


public class ServiceUbicacion {
    //ATRIBUTOS -----
    private DaoUbicacion daoUbicacion;
    private DaoEstadio daoEstadio; //necesitamos el DAO de Estadio para verificar que el estadio exista. Esto es porque una Ubicacion depende de un Estadio (FK)

    //CONSTRUCTOR -----
    public ServiceUbicacion() {
        daoUbicacion = new DaoUbicacion();
        daoEstadio = new DaoEstadio();
    }


    //METODO 1 - INSERTAR (Crear una ubicación nueva) -----
    public void insertar(Ubicacion ubicacion) throws ServiceException {
        try {
            //VALIDACIÓN 1: Nombre obligatorio
            if(ubicacion.getNombre() == null || ubicacion.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre de la ubicación es obligatorio");
            }

            //VALIDACIÓN 2: Precio válido (mayor a 0)
            if(ubicacion.getPrecio() <= 0) {
                throw new ServiceException("El precio debe ser mayor a 0");
            }

            //VALIDACIÓN 3: Capacidad válida (mayor a 0)
            if(ubicacion.getCapacidad() <= 0) {
                throw new ServiceException("La capacidad debe ser mayor a 0");
            }

            //VALIDACIÓN 4: El estadio debe existir
            //Busco el estadio en la BD para verificar que exista
            Estadio estadio = daoEstadio.consultar(ubicacion.getIdEstadio());
            if(estadio == null || estadio.getIdEstadio() == 0) {
                throw new ServiceException("El estadio con ID " + ubicacion.getIdEstadio() + " no existe");
            }

            //VALIDACIÓN 5: Nombre único por estadio
            //No puede haber dos ubicaciones con el mismo nombre en el mismo estadio
            //Ejemplo: puede haber "Platea" en estadio 1 y "Platea" en estadio 2, pero no dos "Platea" en estadio 1
            List<Ubicacion> ubicaciones = daoUbicacion.consultarTodos();

            for(Ubicacion u : ubicaciones) {
                //comparo nombres ignorando mayus/minus Y verifico que sea del mismo estadio
                if(u.getNombre().trim().equalsIgnoreCase(ubicacion.getNombre().trim())
                        && u.getIdEstadio() == ubicacion.getIdEstadio()) {
                    throw new ServiceException("Ya existe una ubicación con ese nombre en este estadio");
                }
            }

            //VALIDACIÓN 6: La suma de capacidades de las ubicaciones no debe superar la capacidad total del estadio
            //Esto es opcional, pero es una buena validación de negocio
            int capacidadUsada = 0;
            for(Ubicacion u : ubicaciones) {
                //sumo solo las ubicaciones del mismo estadio
                if(u.getIdEstadio() == ubicacion.getIdEstadio()) {
                    capacidadUsada += u.getCapacidad();
                }
            }

            //ahora sumo la capacidad de la ubicación que quiero insertar
            capacidadUsada += ubicacion.getCapacidad();

            //verifico que no supere la capacidad total del estadio
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


    //METODO 2 - MODIFICAR (Actualizar una ubicación existente) -----
    public void modificar(Ubicacion ubicacion) throws ServiceException {
        try {
            //VALIDACIÓN 0: Verificar que la ubicación EXISTA
            Ubicacion existente = daoUbicacion.consultar(ubicacion.getIdUbicacion());
            if(existente == null || existente.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación con ID " + ubicacion.getIdUbicacion() + " no existe");
            }

            //VALIDACIÓN 1: Nombre obligatorio
            if(ubicacion.getNombre() == null || ubicacion.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre de la ubicación es obligatorio");
            }

            //VALIDACIÓN 2: Precio válido
            if(ubicacion.getPrecio() <= 0) {
                throw new ServiceException("El precio debe ser mayor a 0");
            }

            //VALIDACIÓN 3: Capacidad válida
            if(ubicacion.getCapacidad() <= 0) {
                throw new ServiceException("La capacidad debe ser mayor a 0");
            }

            //VALIDACIÓN 4: El estadio debe existir
            Estadio estadio = daoEstadio.consultar(ubicacion.getIdEstadio());
            if(estadio == null || estadio.getIdEstadio() == 0) {
                throw new ServiceException("El estadio con ID " + ubicacion.getIdEstadio() + " no existe");
            }

            //VALIDACIÓN 5: Nombre único por estadio (pero puede mantener su propio nombre)
            List<Ubicacion> ubicaciones = daoUbicacion.consultarTodos();

            for(Ubicacion u : ubicaciones) {
                //si el nombre existe en otra ubicación del mismo estadio (que no sea esta misma)
                if(u.getNombre().trim().equalsIgnoreCase(ubicacion.getNombre().trim())
                        && u.getIdEstadio() == ubicacion.getIdEstadio()
                        && u.getIdUbicacion() != ubicacion.getIdUbicacion()) {
                    throw new ServiceException("Existe otra ubicación con ese nombre en este estadio. Cambiar nombre");
                }
            }

            //VALIDACIÓN 6: Verificar capacidad total del estadio
            int capacidadUsada = 0;
            for(Ubicacion u : ubicaciones) {
                //sumo las ubicaciones del mismo estadio, EXCEPTO esta que estoy modificando
                if(u.getIdEstadio() == ubicacion.getIdEstadio() && u.getIdUbicacion() != ubicacion.getIdUbicacion()) {
                    capacidadUsada += u.getCapacidad();
                }
            }

            //ahora sumo la nueva capacidad de la ubicación que estoy modificando
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


    //METODO 3 - ELIMINAR (Borrar una ubicación) -----
    public void eliminar(int id) throws ServiceException {
        try {
            //VALIDACIÓN: Verificar que la ubicación EXISTA
            Ubicacion ubicacion = daoUbicacion.consultar(id);
            if(ubicacion == null || ubicacion.getIdUbicacion() == 0) {
                throw new ServiceException("La ubicación con ID " + id + " no existe");
            }

            //VALIDACIÓN EXTRA: Verificar que no haya ventas asociadas a esta ubicación
            //Si ya se vendieron entradas para esta ubicación, NO se debe poder eliminar
            //Esto lo vamos a implementar más adelante cuando tengamos la entidad VENTA
            //Por ahora lo dejamos comentado:
            /*
            DaoVenta daoVenta = new DaoVenta();
            List<Venta> ventas = daoVenta.consultarTodos();
            for(Venta v : ventas) {
                if(v.getIdUbicacion() == id) {
                    throw new ServiceException("No se puede eliminar la ubicación porque ya tiene ventas registradas");
                }
            }
            */

            //si pasó las validaciones, eliminar
            daoUbicacion.eliminar(id);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //METODO 4 - CONSULTAR (Buscar una ubicación por ID) -----
    public Ubicacion consultar(int id) throws ServiceException {
        try {
            return daoUbicacion.consultar(id);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //METODO 5 - CONSULTAR TODOS (Obtener todas las ubicaciones) -----
    public List<Ubicacion> consultarTodos() throws ServiceException {
        try {
            return daoUbicacion.consultarTodos();
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //METODO EXTRA - CONSULTAR POR ESTADIO (Obtener las ubicaciones de un estadio específico) -----
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