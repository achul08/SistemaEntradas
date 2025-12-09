package gui.ubicacion;

import entidades.Ubicacion;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceUbicacion;
import service.ServiceException;

import java.util.List;

//REPORTE UBICACIONES - Hereda de ReporteBase
//Muestra una tabla con todas las ubicaciones

//RELACIÓN CON OTRAS CLASES:
//- Hereda de ReporteBase (extends)
//- ServiceUbicacion: para obtener todas las ubicaciones de la BD
//- PanelManager: para cambiar de pantalla


public class ReporteUbicaciones extends ReporteBase {
    //ATRIBUTOS ESPECÍFICOS DE UBICACION -----
    private ServiceUbicacion serviceUbicacion; //para consultar las ubicaciones


    //CONSTRUCTOR -----
    public ReporteUbicaciones(PanelManager panelManager) {
        super(panelManager); //llama al constructor de ReporteBase
        serviceUbicacion = new ServiceUbicacion(); //inicializar el service
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "REPORTE DE UBICACIONES";
    }


    //METODO 2 - obtenerNombresColumnas() -----
    //Define los nombres de las columnas de la tabla
    @Override
    public String[] obtenerNombresColumnas() {
        return new String[]{
                "ID Ubicación",
                "ID Estadio",
                "Nombre",
                "Precio",
                "Capacidad"
        };
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todas las ubicaciones de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUbicacion.consultarTodos(); //devuelve List<Ubicacion>
    }


    //METODO 4 - convertirElementoAFila() -----
    //Convierte una ubicación en un array de Object para mostrar en la tabla
    //El ORDEN debe coincidir con el orden de las columnas
    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion

        //crear el array con los datos de la ubicación
        //IMPORTANTE: el orden debe ser igual al de obtenerNombresColumnas()
        return new Object[]{
                ubicacion.getIdUbicacion(),    // columna 0: "ID Ubicación"
                ubicacion.getIdEstadio(),      // columna 1: "ID Estadio"
                ubicacion.getNombre(),         // columna 2: "Nombre"
                "$" + ubicacion.getPrecio(),   // columna 3: "Precio" (con $)
                ubicacion.getCapacidad()       // columna 4: "Capacidad"
        };
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay ubicaciones registradas";
    }


    //METODO 6 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 30; //código 30 = MenuGestionUbicaciones
    }
}
