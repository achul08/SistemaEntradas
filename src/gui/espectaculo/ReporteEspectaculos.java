package gui.espectaculo;

import entidades.Espectaculo;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceEspectaculo;
import service.ServiceException;

import java.text.SimpleDateFormat;
import java.util.List;

//REPORTE ESPECTACULOS - Hereda de ReporteBase
//Muestra una tabla con todos los espectáculos

//RELACIÓN CON OTRAS CLASES:
//- Hereda de ReporteBase (extends)
//- ServiceEspectaculo: para obtener todos los espectáculos de la BD
//- PanelManager: para cambiar de pantalla


public class ReporteEspectaculos extends ReporteBase {
    //ATRIBUTOS ESPECÍFICOS DE ESPECTACULO -----
    private ServiceEspectaculo serviceEspectaculo = new ServiceEspectaculo(); //para consultar los espectáculos
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); //para mostrar fechas

    //CONSTRUCTOR -----
    public ReporteEspectaculos(PanelManager panelManager) {
        super(panelManager); //llama al constructor de ReporteBase
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "REPORTE DE ESPECTÁCULOS";
    }


    //METODO 2 - obtenerNombresColumnas() -----
    //Define los nombres de las columnas de la tabla
    @Override
    public String[] obtenerNombresColumnas() {
        return new String[]{
                "ID Espectáculo",
                "Nombre",
                "Fecha",
                "ID Estadio",
                "Estado"
        };
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todos los espectáculos de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEspectaculo.consultarTodos(); //devuelve List<Espectaculo>
    }


    //METODO 4 - convertirElementoAFila() -----
    //Convierte un espectáculo en un array de Object para mostrar en la tabla
    //El ORDEN debe coincidir con el orden de las columnas
    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Espectaculo espectaculo = (Espectaculo) elemento; //cast de Object a Espectaculo

        //convertir fecha a String
        String fecha = formatoFecha.format(espectaculo.getFecha());

        //convertir boolean a String con if-else
        String estado;
        if (espectaculo.isActivo()) {
            estado = "Activo";
        } else {
            estado = "Inactivo";
        }

        //crear el array con los datos del espectáculo
        //IMPORTANTE: el orden debe ser igual al de obtenerNombresColumnas()
        return new Object[]{
                espectaculo.getIdEspectaculo(),    // columna 0: "ID Espectáculo"
                espectaculo.getNombre(),           // columna 1: "Nombre"
                fecha,                             // columna 2: "Fecha"
                espectaculo.getIdEstadio(),        // columna 3: "ID Estadio"
                estado                             // columna 4: "Estado"
        };
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay espectáculos registrados";
    }


    //METODO 6 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 40; //código 40 = MenuGestionEspectaculos
    }
}