package gui.estadio;

import entidades.Estadio;
import gui.PanelManager;
import gui.base.PantallaEliminarBase;
import service.ServiceEstadio;
import service.ServiceException;

import java.util.List;

public class PantallaEliminarEstadio extends PantallaEliminarBase {
    //ATRIBUTOS ESPECÍFICOS DE ESTADIO -----
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para consultar y eliminar estadios en la BD

    //CONSTRUCTOR -----
    public PantallaEliminarEstadio(PanelManager panel) {
        super(panel);
        inicializar();
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----
    @Override
    public String getTitulo() {
        return "ELIMINAR ESTADIO";
    }

    @Override
    public String getInstruccion() {
        return "Seleccione el estadio que desea eliminar:";
    }

    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEstadio.consultarTodos(); //devuelve List<Estadio>
    }

    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio
        return estadio.getIdEstadio() + " - " + estadio.getNombre();
    }

    @Override
    public String getMensajeSinElementos() {
        return "No hay estadios disponibles para eliminar";
    }


    @Override
    public String obtenerInformacionElemento(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio

        return "ID: " + estadio.getIdEstadio() + "\n" +
                "Nombre: " + estadio.getNombre() + "\n" +
                "Dirección: " + estadio.getDireccion() + "\n" +
                "Capacidad Total: " + estadio.getCapacidadTotal();
    }


    @Override
    public int obtenerIdElemento(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio
        return estadio.getIdEstadio();
    }

    @Override
    public void eliminarEnBD(int id) throws ServiceException {
        serviceEstadio.eliminar(id);
    }

    @Override
    public int getCodigoMenuPrincipal() {
        return 1;
    }
}