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


    //METODO 1 - INSERTAR (Crear un espectáculo nuevo) -----
    public void insertar(Espectaculo espectaculo) throws ServiceException {
        try {
            //VALIDACIONES
            // Nombre obligatorio
            if(espectaculo.getNombre() == null || espectaculo.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre del espectáculo es obligatorio");
            }

            //Fecha obligatoria
            if(espectaculo.getFecha() == null) {
                throw new ServiceException("La fecha del espectáculo es obligatoria");
            }

            //La fecha no puede ser en el pasado
            Date hoy = new Date();
            if(espectaculo.getFecha().before(hoy)) {
                throw new ServiceException("La fecha del espectáculo no puede ser en el pasado");
            }

            //El estadio debe existir
            Estadio estadio = daoEstadio.consultar(espectaculo.getIdEstadio());
            if(estadio == null || estadio.getIdEstadio() == 0) {
                throw new ServiceException("El estadio con ID " + espectaculo.getIdEstadio() + " no existe");
            }

            //No puede haber dos espectáculos con el mismo nombre en el mismo estadio el mismo día
            List<Espectaculo> espectaculos = daoEspectaculo.consultarTodos();

            for(Espectaculo e : espectaculos) {
                //comparar nombre, estadio y fecha
                if(e.getNombre().trim().equalsIgnoreCase(espectaculo.getNombre().trim())
                        && e.getIdEstadio() == espectaculo.getIdEstadio()
                        && mismoDia(e.getFecha(), espectaculo.getFecha())) {
                    throw new ServiceException("Ya existe un espectáculo con ese nombre en este estadio para esa fecha");
                }
            }

            //si pasó todas las validaciones, insertar
            daoEspectaculo.insertar(espectaculo);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //METODO 2 - MODIFICAR (Actualizar un espectáculo existente) -----
    public void modificar(Espectaculo espectaculo) throws ServiceException {
        try {
            //VALIDACIONES
            // Verificar que el espectáculo EXISTA
            Espectaculo existente = daoEspectaculo.consultar(espectaculo.getIdEspectaculo());
            if(existente == null || existente.getIdEspectaculo() == 0) {
                throw new ServiceException("El espectáculo con ID " + espectaculo.getIdEspectaculo() + " no existe");
            }

            //Nombre obligatorio
            if(espectaculo.getNombre() == null || espectaculo.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre del espectáculo es obligatorio");
            }

            //Fecha obligatoria
            if(espectaculo.getFecha() == null) {
                throw new ServiceException("La fecha del espectáculo es obligatoria");
            }

            //La fecha no puede ser en el pasado
            Date hoy = new Date();
            if(espectaculo.getFecha().before(hoy)) {
                throw new ServiceException("La fecha del espectáculo no puede ser en el pasado");
            }

            //El estadio debe existir
            Estadio estadio = daoEstadio.consultar(espectaculo.getIdEstadio());
            if(estadio == null || estadio.getIdEstadio() == 0) {
                throw new ServiceException("El estadio con ID " + espectaculo.getIdEstadio() + " no existe");
            }

            //No puede haber otro espectáculo con el mismo nombre en el mismo estadio el mismo día
            List<Espectaculo> espectaculos = daoEspectaculo.consultarTodos();

            for(Espectaculo e : espectaculos) {
                //si el nombre existe en otro espectáculo del mismo estadio y fecha (que no sea este mismo)
                if(e.getNombre().trim().equalsIgnoreCase(espectaculo.getNombre().trim())
                        && e.getIdEstadio() == espectaculo.getIdEstadio()
                        && mismoDia(e.getFecha(), espectaculo.getFecha())
                        && e.getIdEspectaculo() != espectaculo.getIdEspectaculo()) {
                    throw new ServiceException("Existe otro espectáculo con ese nombre en este estadio para esa fecha");
                }
            }

            //si pasó todas las validaciones, modificar
            daoEspectaculo.modificar(espectaculo);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //METODO 3 - ELIMINAR (Borrar un espectáculo) -----
    public void eliminar(int id) throws ServiceException {
        try {
            //VALIDACIONES
            // Verificar que el espectáculo EXISTA
            Espectaculo espectaculo = daoEspectaculo.consultar(id);
            if(espectaculo == null || espectaculo.getIdEspectaculo() == 0) {
                throw new ServiceException("El espectáculo con ID " + id + " no existe");
            }

            //VALIDACIÓN EXTRA: Verificar que no haya ventas asociadas a este espectáculo
            //Si ya se vendieron entradas para este espectáculo, NO se debe poder eliminar
            List<Venta> ventasDelEspectaculo = daoVenta.consultarPorEspectaculo(id);

            if (!ventasDelEspectaculo.isEmpty()) {
                throw new ServiceException(
                        "No se puede eliminar el espectáculo porque tiene " +
                                ventasDelEspectaculo.size() +
                                " venta(s) registrada(s). " +
                                "Los registros de ventas deben conservarse por motivos contables."
                );
            }

            //si pasó las validaciones, eliminar
            daoEspectaculo.eliminar(id);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //METODO 4 - CONSULTAR (Buscar un espectáculo por ID) -----
    public Espectaculo consultar(int id) throws ServiceException {
        try {
            return daoEspectaculo.consultar(id);
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //METODO 5 - CONSULTAR TODOS (Obtener todos los espectáculos) -----
    public List<Espectaculo> consultarTodos() throws ServiceException {
        try {
            return daoEspectaculo.consultarTodos();
        }
        catch (DaoException e) {
            throw new ServiceException("Error en base de datos");
        }
    }


    //METODO EXTRA - CONSULTAR ACTIVOS (Obtener solo los espectáculos activos) -----
    //Este método es útil para mostrar solo los espectáculos disponibles para venta
    public List<Espectaculo> consultarActivos() throws ServiceException {
        try {
            //traer todos los espectáculos
            List<Espectaculo> todosLosEspectaculos = daoEspectaculo.consultarTodos();

            //filtrar solo los activos
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


    //METODO EXTRA - CONSULTAR POR ESTADIO (Obtener los espectáculos de un estadio específico) -----
    public List<Espectaculo> consultarPorEstadio(int idEstadio) throws ServiceException {
        try {
            //traer todos los espectáculos
            List<Espectaculo> todosLosEspectaculos = daoEspectaculo.consultarTodos();

            //filtrar solo los del estadio solicitado
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


    //METODO AUXILIAR - mismoDia() -----
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