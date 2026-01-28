package gui.estadio;

import entidades.Estadio;
import gui.PanelManager;
import gui.base.PantallaSeleccionBase;
import service.ServiceEstadio;
import service.ServiceException;

import java.util.List;



public class PantallaSeleccionEstadios extends PantallaSeleccionBase {
    //ATRIBUTOS ESPECÍFICOS DE ESTADIO -----
    private ServiceEstadio serviceEstadio = new ServiceEstadio();


    //CONSTRUCTOR -----
    public PantallaSeleccionEstadios(PanelManager panel) {
        super(panel); //llama al constructor de PantallaSeleccionBase
        inicializar();
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----
    @Override
    public String getTitulo() {
        return "SELECCIONAR ESTADIO A MODIFICAR";
    }

    @Override
    public String getInstruccion() {
        return "Seleccione un estadio";
    }

    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEstadio.consultarTodos(); //devuelve List<Estadio>
        //Este método es llamado por cargarElementosEnCombo() (que está en PantallaSeleccionBase)
        //cargarElementosEnCombo() recorre la lista y llama a formatearElementoParaCombo() para cada estadio
    }

    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio

        return estadio.getIdEstadio() + " - " + estadio.getNombre();
    }

    @Override
    public String getMensajeSinElementos() {
        return "No hay estadios disponibles para modificar";
    }

    @Override
    public void abrirFormularioModificarConElemento(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio

        //Usar el metodo de PanelManager para estadios
        getPanelManager().mostrarFormularioModificar(estadio);

    }


    @Override
    public int getCodigoMenuPrincipal() {
        return 1;
    }
}