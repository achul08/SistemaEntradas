package gui.estadio;

import entidades.Estadio;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceEstadio;
import service.ServiceException;

import java.util.List;

//REPORTE ESTADIOS - Hereda de ReporteBase
//Muestra una tabla con todos los estadios

//RELACIÓN CON OTRAS CLASES:
//- Hereda de ReporteBase (extends)
//- ServiceEstadio: para obtener todos los estadios de la BD
//- PanelManager: para cambiar de pantalla


public class ReporteEstadios extends ReporteBase {
    //ATRIBUTOS ESPECÍFICOS DE ESTADIO -----
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para consultar los estadios

    //CONSTRUCTOR -----
    public ReporteEstadios(PanelManager panelManager) {
        super(panelManager); //llama al constructor de ReporteBase
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "REPORTE DE ESTADIOS";
    }


    //METODO 2 - obtenerNombresColumnas() -----
    //Define los nombres de las columnas de la tabla
    @Override
    public String[] obtenerNombresColumnas() {
        return new String[]{
                "ID Estadio",
                "Nombre",
                "Dirección",
                "Capacidad Total"
        };
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todos los estadios de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEstadio.consultarTodos(); //devuelve List<Estadio>
    }


    //METODO 4 - convertirElementoAFila() -----
    //Convierte un estadio en un array de Object para mostrar en la tabla
    //El ORDEN debe coincidir con el orden de las columnas
    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio

        //crear el array con los datos del estadio
        //IMPORTANTE: el orden debe ser igual al de obtenerNombresColumnas()
        return new Object[]{
                estadio.getIdEstadio(),      // columna 0: "ID Estadio"
                estadio.getNombre(),         // columna 1: "Nombre"
                estadio.getDireccion(),      // columna 2: "Dirección"
                estadio.getCapacidadTotal()  // columna 3: "Capacidad Total"
        };
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay estadios registrados";
    }


    //METODO 6 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 1; //código 1 = MenuPrincipal de estadios
    }
}