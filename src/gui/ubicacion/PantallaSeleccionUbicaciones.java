package gui.ubicacion;

import entidades.Ubicacion;
import gui.PanelManager;
import gui.base.PantallaSeleccionBase;
import service.ServiceUbicacion;
import service.ServiceException;

import java.util.List;


public class PantallaSeleccionUbicaciones extends PantallaSeleccionBase {
    //ATRIBUTOS ESPECÍFICOS DE UBICACION -----
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion(); //para consultar las ubicaciones en la BD

    //CONSTRUCTOR -----
    public PantallaSeleccionUbicaciones(PanelManager panel) {
        super(panel); //llama al constructor de PantallaSeleccionBase
        inicializar(); //carga las ubicaciones en el ComboBox
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----
    @Override
    public String getTitulo() {
        return "SELECCIONAR UBICACIÓN A MODIFICAR";
    }

    @Override
    public String getInstruccion() {
        return "Seleccione una ubicación";
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
        return "No hay ubicaciones disponibles para modificar";
    }

    @Override
    public void abrirFormularioModificarConElemento(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion

        //Crear FormularioUbicacion en modo modificar
        FormularioUbicacion formulario = new FormularioUbicacion(getPanelManager(), ubicacion);
        getPanelManager().mostrar(formulario);
    }

    @Override
    public int getCodigoMenuPrincipal() {
        return 30; //código 30 = MenuGestionUbicaciones
    }
}