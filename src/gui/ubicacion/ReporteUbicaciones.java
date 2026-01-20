package gui.ubicacion;

import entidades.Ubicacion;
import entidades.Estadio;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceUbicacion;
import service.ServiceEstadio;
import service.ServiceException;

import java.util.List;

//REPORTE UBICACIONES - Hereda de ReporteBase
//Muestra una tabla con todas las ubicaciones
//MEJORA: Ahora muestra el NOMBRE del estadio, no solo el ID

//RELACIÓN CON OTRAS CLASES:
//- Hereda de ReporteBase (extends)
//- ServiceUbicacion: para obtener todas las ubicaciones de la BD
//- ServiceEstadio: para obtener el nombre del estadio de cada ubicación
//- PanelManager: para cambiar de pantalla


public class ReporteUbicaciones extends ReporteBase {
    //ATRIBUTOS ESPECÍFICOS DE UBICACION -----
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion(); //para consultar las ubicaciones
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //NUEVO: para obtener nombres de estadios
    //Ambos Services se crean ANTES del constructor
    //Por eso cuando llamemos a inicializar() ya van a existir


    //CONSTRUCTOR -----
    public ReporteUbicaciones(PanelManager panelManager) {
        super(panelManager); //llama al constructor de ReporteBase
        //En este momento:
        //1. serviceUbicacion y serviceEstadio ya existen
        //2. ReporteBase ya armó toda la estructura visual
        //3. La tabla todavía está vacía

        inicializar(); //carga las columnas y los datos
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
                "Estadio",           // ← CAMBIO: antes era "ID Estadio", ahora es "Estadio"
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

        //NUEVO: Obtener el nombre del estadio en lugar de solo mostrar el ID
        String nombreEstadio = "(Desconocido)"; //valor por defecto por si hay error

        try {
            //Consultar el estadio por su ID
            Estadio estadio = serviceEstadio.consultar(ubicacion.getIdEstadio());

            //Verificar que el estadio exista (consultar puede devolver un objeto vacío)
            if(estadio != null && estadio.getIdEstadio() != 0) {
                //Si existe, usar su nombre
                nombreEstadio = estadio.getNombre();
            }
        } catch (ServiceException e) {
            //Si hay error al consultar, usar el valor por defecto
            //No mostramos error al usuario porque no es crítico
        }

        //crear el array con los datos de la ubicación
        //IMPORTANTE: el orden debe ser igual al de obtenerNombresColumnas()
        return new Object[]{
                ubicacion.getIdUbicacion(),    // columna 0: "ID Ubicación"
                nombreEstadio,                 // columna 1: "Estadio" ← CAMBIO: antes era ubicacion.getIdEstadio()
                ubicacion.getNombre(),         // columna 2: "Nombre"
                "$" + ubicacion.getPrecio(),   // columna 3: "Precio"
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