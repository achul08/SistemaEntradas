package gui.ubicacion;

import entidades.Ubicacion;
import gui.PanelManager;
import gui.base.PantallaEliminarBase;
import service.ServiceUbicacion;
import service.ServiceException;

import java.util.List;



public class PantallaEliminarUbicacion extends PantallaEliminarBase {
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion(); //para consultar y eliminar ubicaciones en la BD

    //CONSTRUCTOR -----
    public PantallaEliminarUbicacion(PanelManager panel) {
        super(panel); //llama al constructor de PantallaEliminarBase
        inicializar(); //carga las ubicaciones en el ComboBox
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----
    @Override
    public String getTitulo() {
        return "ELIMINAR UBICACIÓN";
    }

    @Override
    public String getInstruccion() {
        return "Seleccione la ubicación que desea eliminar:";
    }

    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUbicacion.consultarTodos(); //devuelve List<Ubicacion>
    }

    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion
        return ubicacion.getIdUbicacion() + " - " + ubicacion.getNombre();
    }

    @Override
    public String getMensajeSinElementos() {
        return "No hay ubicaciones disponibles para eliminar";
    }

    @Override
    public String obtenerInformacionElemento(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion

        //formato de la información que se muestra
        return "ID: " + ubicacion.getIdUbicacion() + "\n" +
                "Nombre: " + ubicacion.getNombre() + "\n" +
                "ID Estadio: " + ubicacion.getIdEstadio() + "\n" +
                "Precio: $" + ubicacion.getPrecio() + "\n" +
                "Capacidad: " + ubicacion.getCapacidad();
    }


    @Override
    public int obtenerIdElemento(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion
        return ubicacion.getIdUbicacion();
    }

    @Override
    public void eliminarEnBD(int id) throws ServiceException {
        serviceUbicacion.eliminar(id);
    }


    @Override
    public int getCodigoMenuPrincipal() {
        return 30;
    }
}