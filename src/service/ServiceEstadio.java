package service;

//recibe/usa el dao.
//Tiene metodos que llaman a los metodos del dao. Hago esto para cuando lo tenga que poner a trabajar con un servidor externo y no con h2
//uso el service como intermediario porque aca puedo cambiar rapidamente el dao sin que me afecte el programa
//un service por cada dao

import dao.DaoEstadio;
import dao.DaoException;
import entidades.Estadio;
import java.util.List;

// ATRIBUTO: El DAO que va a usar este Service
// Este DAO es el que realmente habla con H2


public class ServiceEstadio {
    private DaoEstadio daoEstadio;

    public ServiceEstadio() {
        daoEstadio = new DaoEstadio();
    }   // Crea una instancia del DAO para poder usarlo


    //.trim() saca espacios en blanco del principio y final, es para evitar que
    //el usuario ponga solo espacios y parezca que tiene texto
    //.isEmpty() es para verificar que este vacio, si es true, esta vacio
    //necesito los dos porque el isempty si tiene espacios me lo toma como false, entonces el trim le saca los espacios


    // METODOS ------------------
    // INSERTAR (Crear un estadio nuevo)
    public void insertar(Estadio estadio) throws ServiceException {
        try {
            if(estadio.getNombre()==null || estadio.getNombre().trim().isEmpty()){
                throw new ServiceException("El nombre del estadio es obligatorio");
            }

            if(estadio.getDireccion()==null){
                throw new ServiceException("La direccion del estadio es obligatoria");
            }
            if(estadio.getCapacidadTotal()<=0){
                throw new ServiceException("La capacidad debe ser mayor a 0");
            }
            List<Estadio> estadios = daoEstadio.consultarTodos();

            for(Estadio e : estadios){ //compara nombres ignorando mayus minus
                if (e.getNombre().trim().equalsIgnoreCase(estadio.getNombre().trim())){
                    throw new ServiceException("Ya existe un estadio con ese nombre");
                }
            }


            for(Estadio e : estadios){
                if(e.getDireccion().trim().equalsIgnoreCase(estadio.getDireccion().trim())){
                    throw new ServiceException("Ya existe un estadio con esa direccion");
                }
            }
            daoEstadio.insertar(estadio);
        }
        catch (DaoException e) {
            // Si el DAO falla (problema con H2), convertimos la excepción
            // de DaoException a ServiceException
            throw new ServiceException("Error en base de datos");
        }
    }


    //MODIFICAR (Actualizar un estadio)
    public void modificar(Estadio estadio) throws ServiceException{
        try{
            Estadio existente = daoEstadio.consultar(estadio.getIdEstadio()); //busco en h2 que exista el estadio con ese id
            if (existente == null || existente.getIdEstadio() == 0) { //si es null (porque hubo un error en el dao) o es 0 (porque no existe)
                throw new ServiceException("El estadio con ID " + estadio.getIdEstadio() + " no existe");
            }

            if(estadio.getNombre() == null || estadio.getNombre().trim().isEmpty()){
                throw new ServiceException("El nombre del estadio es obligatorio");
            }

            if(estadio.getDireccion() == null || estadio.getDireccion().trim().isEmpty()){
                throw new ServiceException("La direccion del estadio es obligatoria");
            }
            if(estadio.getCapacidadTotal()<=0){
                throw new ServiceException("La capacidad debe ser mayor a 0");
            }

            List<Estadio> estadios = daoEstadio.consultarTodos();

            for(Estadio e : estadios){
                if(e.getNombre().trim().equalsIgnoreCase(estadio.getNombre().trim()) && e.getIdEstadio() != estadio.getIdEstadio()){
                    throw new ServiceException("Existe otro estadio con ese nombre. Cambiar nombre");
                }
            }

            for(Estadio e : estadios){
                if(e.getDireccion().trim().equalsIgnoreCase(estadio.getDireccion().trim()) && e.getIdEstadio() != estadio.getIdEstadio()){
                    throw new ServiceException("Existe otro estadio con esa direccion. Cambiar direccion");
                }
            }

            daoEstadio.modificar(estadio);
        }
        catch(DaoException e){
            throw new ServiceException("Error en base de datos");
        }
    }


    //ELIMINAR (borrar un estadio)
    public void eliminar(int id) throws ServiceException{
        try{
            Estadio estadio = daoEstadio.consultar(id);
            if (estadio == null || estadio.getIdEstadio() == 0) {
                throw new ServiceException("El estadio con ID " + id + " no existe");
            }

            daoEstadio.eliminar(id);
        }
        catch(DaoException e){
            throw new ServiceException("Error en base de datos");
        }
    }


    //CONSULTAR
    public Estadio consultar(int id) throws ServiceException{
        try{
            return daoEstadio.consultar(id);
        }

        catch(DaoException e){
            throw new ServiceException("Error en base de datos");
        }
        }


        //CONSULTAR TODOS
    public List<Estadio> consultarTodos() throws ServiceException{
        try{
            return daoEstadio.consultarTodos();
        }
        catch (DaoException e){
            throw new ServiceException("Error en base de datos");
        }
    }



}
