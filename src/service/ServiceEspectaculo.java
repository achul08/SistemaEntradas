package service;

//SERVICE ESPECTACULO - Lógica de negocio para la entidad Espectaculo
//Valida datos y llama al DAO

import dao.DaoEspectaculo;
import dao.DaoEstadio;
import dao.DaoException;
import entidades.Espectaculo;
import entidades.Estadio;
import java.util.Date;
import java.util.List;
import dao.DaoVenta;
import entidades.Venta;


public class ServiceEspectaculo {
    //ATRIBUTOS -----
    private DaoEspectaculo daoEspectaculo;
    private DaoEstadio daoEstadio; //necesitamos el DAO de Estadio para verificar que el estadio exista
    private DaoVenta daoVenta;  // ← NUEVO

    //CONSTRUCTOR -----
    public ServiceEspectaculo() {
        daoEspectaculo = new DaoEspectaculo();
        daoEstadio = new DaoEstadio();
        daoVenta = new DaoVenta();
    }


    //METODOS---------------
    // INSERTAR (Crear un espectáculo nuevo)
    public void insertar(Espectaculo espectaculo) throws ServiceException {
        try {
            if(espectaculo.getNombre() == null || espectaculo.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre del espectáculo es obligatorio");
            }

            if(espectaculo.getFecha() == null) {
                throw new ServiceException("La fecha del espectáculo es obligatoria");
            }
            Date hoy = new Date();
            if(espectaculo.getFecha().before(hoy)) {
                throw new ServiceException("La fecha del espectáculo no puede ser en el pasado");
            }
            Estadio estadio = daoEstadio.consultar(espectaculo.getIdEstadio());
            if(estadio == null || estadio.getIdEstadio() == 0) {
                throw new ServiceException("El estadio con ID " + espectaculo.getIdEstadio() + " no existe");
            }
            List<Espectaculo> espectaculos = daoEspectaculo.consultarTodos();

            for(Espectaculo e : espectaculos) {
                if(e.getNombre().trim().equalsIgnoreCase(espectaculo.getNombre().trim())
                        && e.getIdEstadio() == espectaculo.getIdEstadio()
                        && mismoDia(e.getFecha(), espectaculo.getFecha())) {
                    throw new ServiceException("Ya existe un espectáculo con ese nombre en este estadio para esa fecha");
                }
            }
            daoEspectaculo.insertar(espectaculo);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //MODIFICAR (Actualizar un espectáculo existente) -----
    public void modificar(Espectaculo espectaculo) throws ServiceException {
        try {
            Espectaculo existente = daoEspectaculo.consultar(espectaculo.getIdEspectaculo());
            if(existente == null || existente.getIdEspectaculo() == 0) {
                throw new ServiceException("El espectáculo con ID " + espectaculo.getIdEspectaculo() + " no existe");
            }
            if(espectaculo.getNombre() == null || espectaculo.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre del espectáculo es obligatorio");
            }

            if(espectaculo.getFecha() == null) {
                throw new ServiceException("La fecha del espectáculo es obligatoria");
            }

            Date hoy = new Date();
            if(espectaculo.getFecha().before(hoy)) {
                throw new ServiceException("La fecha del espectáculo no puede ser en el pasado");
            }

            Estadio estadio = daoEstadio.consultar(espectaculo.getIdEstadio());
            if(estadio == null || estadio.getIdEstadio() == 0) {
                throw new ServiceException("El estadio con ID " + espectaculo.getIdEstadio() + " no existe");
            }
            List<Espectaculo> espectaculos = daoEspectaculo.consultarTodos();

            for(Espectaculo e : espectaculos) {
                if(e.getNombre().trim().equalsIgnoreCase(espectaculo.getNombre().trim())
                        && e.getIdEstadio() == espectaculo.getIdEstadio()
                        && mismoDia(e.getFecha(), espectaculo.getFecha())
                        && e.getIdEspectaculo() != espectaculo.getIdEspectaculo()) {
                    throw new ServiceException("Existe otro espectáculo con ese nombre en este estadio para esa fecha");
                }
            }

            daoEspectaculo.modificar(espectaculo);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //ELIMINAR (Borrar un espectáculo)
    public void eliminar(int id) throws ServiceException {
        try {
            Espectaculo espectaculo = daoEspectaculo.consultar(id);
            if(espectaculo == null || espectaculo.getIdEspectaculo() == 0) {
                throw new ServiceException("El espectáculo con ID " + id + " no existe");
            }

            List<Venta> ventasDelEspectaculo = daoVenta.consultarPorEspectaculo(id);

            if (!ventasDelEspectaculo.isEmpty()) {
                throw new ServiceException(
                        "No se puede eliminar el espectáculo porque tiene " +
                                ventasDelEspectaculo.size() +
                                " venta(s) registrada(s). " +
                                "Los registros de ventas deben conservarse por motivos contables."
                );
            }

            daoEspectaculo.eliminar(id);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //CONSULTAR (Buscar un espectáculo por ID)
    public Espectaculo consultar(int id) throws ServiceException {
        try {
            return daoEspectaculo.consultar(id);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //CONSULTAR TODOS (Obtener todos los espectáculos)
    public List<Espectaculo> consultarTodos() throws ServiceException {
        try {
            return daoEspectaculo.consultarTodos();
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //CONSULTAR ACTIVOS (Obtener solo los espectáculos activos)
    //Este método es útil para mostrar solo los espectáculos disponibles para venta
    public List<Espectaculo> consultarActivos() throws ServiceException {
        try {
            List<Espectaculo> todosLosEspectaculos = daoEspectaculo.consultarTodos();

            List<Espectaculo> espectaculosActivos = new java.util.ArrayList<>();
            for(Espectaculo e : todosLosEspectaculos) {
                if(e.isActivo()) {
                    espectaculosActivos.add(e);
                }
            }

            return espectaculosActivos;
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //CONSULTAR POR ESTADIO (Obtener los espectáculos de un estadio específico)
    public List<Espectaculo> consultarPorEstadio(int idEstadio) throws ServiceException {
        try {
            List<Espectaculo> todosLosEspectaculos = daoEspectaculo.consultarTodos();
            List<Espectaculo> espectaculosDelEstadio = new java.util.ArrayList<>();
            for(Espectaculo e : todosLosEspectaculos) {
                if(e.getIdEstadio() == idEstadio) {
                    espectaculosDelEstadio.add(e);
                }
            }

            return espectaculosDelEstadio;
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //mismoDia()
    //Compara si dos fechas son del mismo día (ignora hora, minutos, segundos)
    private boolean mismoDia(Date fecha1, Date fecha2) {
        if(fecha1 == null || fecha2 == null) {
            return false;
        }

        //usar Calendar para comparar solo año, mes y día
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        cal1.setTime(fecha1);

        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.setTime(fecha2);

        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR)
                && cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH)
                && cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }
}