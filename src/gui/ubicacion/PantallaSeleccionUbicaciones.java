package gui.ubicacion;

import entidades.Ubicacion;
import gui.PanelManager;
import gui.base.PantallaSeleccionBase;
import service.ServiceUbicacion;
import service.ServiceException;

import java.util.List;

//PANTALLA SELECCIÓN UBICACIONES - Hereda de PantallaSeleccionBase
//Para elegir cuál ubicación modificar

//RELACIÓN CON OTRAS CLASES:
//- Hereda de PantallaSeleccionBase (extends)
//- ServiceUbicacion: para obtener todas las ubicaciones de la BD
//- PanelManager: para cambiar a FormularioUbicacion


public class PantallaSeleccionUbicaciones extends PantallaSeleccionBase {
    //ATRIBUTOS ESPECÍFICOS DE UBICACION -----
    private ServiceUbicacion serviceUbicacion; //para consultar las ubicaciones en la BD


    //CONSTRUCTOR -----
    public PantallaSeleccionUbicaciones(PanelManager panel) {
        super(panel); //llama al constructor de PantallaSeleccionBase
        serviceUbicacion = new ServiceUbicacion(); //inicializar el service
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "SELECCIONAR UBICACIÓN A MODIFICAR";
    }


    //METODO 2 - getInstruccion() -----
    @Override
    public String getInstruccion() {
        return "Seleccione una ubicación";
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todas las ubicaciones de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUbicacion.consultarTodos(); //devuelve List<Ubicacion>
    }


    //METODO 4 - formatearElementoParaCombo() -----
    //Define cómo se muestra cada ubicación en el ComboBox
    //Formato: "ID - Nombre (Estadio: nombre_estadio)"
    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion
        return ubicacion.getIdUbicacion() + " - " + ubicacion.getNombre();
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay ubicaciones disponibles para modificar";
    }


    //METODO 6 - abrirFormularioModificarConElemento() -----
    //Abre FormularioUbicacion en modo modificar con la ubicación seleccionada
    @Override
    public void abrirFormularioModificarConElemento(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion

        //Crear FormularioUbicacion en modo modificar
        FormularioUbicacion formulario = new FormularioUbicacion(getPanelManager(), ubicacion);
        getPanelManager().mostrar(formulario);
    }


    //METODO 7 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 30; //código 30 = MenuGestionUbicaciones
    }
}