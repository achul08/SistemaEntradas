package gui.espectaculo;

import entidades.Espectaculo;
import entidades.Estadio;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceEspectaculo;
import service.ServiceEstadio;
import service.ServiceException;

import java.text.SimpleDateFormat;
import java.util.List;

//REPORTE ESPECTACULOS - Hereda de ReporteBase
//Muestra una tabla con todos los espectáculos
//MEJORA: Ahora puede usarse tanto para Admin como para Vendedor
//- Admin ve TODOS los espectáculos (activos e inactivos)
//- Vendedor ve SOLO los espectáculos ACTIVOS

//RELACIÓN CON OTRAS CLASES:
//- Hereda de ReporteBase (extends)
//- ServiceEspectaculo: para obtener todos los espectáculos de la BD
//- ServiceEstadio: para obtener el nombre del estadio de cada espectáculo
//- PanelManager: para cambiar de pantalla


public class ReporteEspectaculos extends ReporteBase {
    //ATRIBUTOS ESPECÍFICOS DE ESPECTACULO -----
    private ServiceEspectaculo serviceEspectaculo = new ServiceEspectaculo(); //para consultar los espectáculos
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para obtener nombres de estadios

    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); //para mostrar fechas

    //NUEVO: atributo para controlar el comportamiento según el tipo de usuario
    private boolean soloActivos; //true = muestra solo activos (vendedor), false = muestra todos (admin)
    private int codigoMenuVolver; //código del menú al que debe volver


    //CONSTRUCTOR PARA ADMIN (muestra todos los espectáculos) -----
    public ReporteEspectaculos(PanelManager panelManager) {
        super(panelManager); //llama al constructor de ReporteBase

        //Como NO se especificó nada, asumimos que es para Admin
        this.soloActivos = false; //mostrar todos (activos e inactivos)
        this.codigoMenuVolver = 40; //volver a MenuGestionEspectaculos

        inicializar(); //carga los datos
    }


    //CONSTRUCTOR PARA VENDEDOR (muestra solo activos) -----
    //Este constructor recibe un parámetro extra que indica:
    //- soloActivos: true = mostrar solo activos
    //- codigoMenuVolver: código del menú al que volver
    public ReporteEspectaculos(PanelManager panelManager, boolean soloActivos, int codigoMenuVolver) {
        super(panelManager); //llama al constructor de ReporteBase

        //Guardar los parámetros
        this.soloActivos = soloActivos; //true = solo activos, false = todos
        this.codigoMenuVolver = codigoMenuVolver; //20 para vendedor, 40 para admin

        inicializar(); //carga los datos
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        //Si soloActivos = true, es para vendedor
        //Si soloActivos = false, es para admin
        if(soloActivos) {
            return "ESPECTÁCULOS DISPONIBLES PARA VENTA";
        } else {
            return "REPORTE DE ESPECTÁCULOS";
        }
    }


    //METODO 2 - obtenerNombresColumnas() -----
    //Define los nombres de las columnas de la tabla
    @Override
    public String[] obtenerNombresColumnas() {
        //Si es para vendedor (soloActivos = true), NO mostrar columna "Estado"
        //porque todos son activos
        if(soloActivos) {
            return new String[]{
                    "ID",
                    "Nombre del Espectáculo",
                    "Fecha",
                    "Estadio"
            };
        } else {
            //Si es para admin, mostrar columna "Estado"
            return new String[]{
                    "ID Espectáculo",
                    "Nombre",
                    "Fecha",
                    "Estadio",
                    "Estado"
            };
        }
    }


    //METODO 3 - consultarTodos() -----
    //Consulta los espectáculos de la BD según el tipo de usuario
    @Override
    public List<?> consultarTodos() throws ServiceException {
        //Si soloActivos = true, consultar solo los activos
        //Si soloActivos = false, consultar todos
        if(soloActivos) {
            return serviceEspectaculo.consultarActivos(); //devuelve List<Espectaculo> solo activos
        } else {
            return serviceEspectaculo.consultarTodos(); //devuelve List<Espectaculo> todos
        }
    }


    //METODO 4 - convertirElementoAFila() -----
    //Convierte un espectáculo en un array de Object para mostrar en la tabla
    //El ORDEN debe coincidir con el orden de las columnas
    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Espectaculo espectaculo = (Espectaculo) elemento; //cast de Object a Espectaculo

        //convertir fecha a String usando el formato dd/MM/yyyy
        String fecha = formatoFecha.format(espectaculo.getFecha());

        //Obtener el nombre del estadio en lugar de solo mostrar el ID
        String nombreEstadio = "(Desconocido)"; //valor por defecto por si hay error

        try {
            //Consultar el estadio por su ID
            Estadio estadio = serviceEstadio.consultar(espectaculo.getIdEstadio());

            //Verificar que el estadio exista
            if(estadio != null && estadio.getIdEstadio() != 0) {
                //Si existe, usar su nombre
                nombreEstadio = estadio.getNombre();
            }
        } catch (ServiceException e) {
            //Si hay error al consultar, usar el valor por defecto
        }

        //crear el array con los datos del espectáculo
        //IMPORTANTE: el orden debe ser igual al de obtenerNombresColumnas()

        //Si es para vendedor (soloActivos = true), NO incluir columna "Estado"
        if(soloActivos) {
            return new Object[]{
                    espectaculo.getIdEspectaculo(),    // columna 0: "ID"
                    espectaculo.getNombre(),           // columna 1: "Nombre del Espectáculo"
                    fecha,                             // columna 2: "Fecha"
                    nombreEstadio                      // columna 3: "Estadio"
            };
        } else {
            //Si es para admin, incluir columna "Estado"

            //convertir boolean a String con if-else
            String estado;
            if (espectaculo.isActivo()) {
                estado = "Activo";
            } else {
                estado = "Inactivo";
            }

            return new Object[]{
                    espectaculo.getIdEspectaculo(),    // columna 0: "ID Espectáculo"
                    espectaculo.getNombre(),           // columna 1: "Nombre"
                    fecha,                             // columna 2: "Fecha"
                    nombreEstadio,                     // columna 3: "Estadio"
                    estado                             // columna 4: "Estado"
            };
        }
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        //Mensaje diferente según el tipo de usuario
        if(soloActivos) {
            return "No hay espectáculos activos disponibles para venta";
        } else {
            return "No hay espectáculos registrados";
        }
    }


    //METODO 6 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        //Usar el código que se pasó en el constructor
        //20 para vendedor, 40 para admin
        return codigoMenuVolver;
    }
}