package gui.estadio;

import entidades.Estadio;
import gui.PanelManager;
import gui.base.PantallaSeleccionBase;
import service.ServiceEstadio;
import service.ServiceException;

import java.util.List;

//PANTALLA SELECCIÓN ESTADIOS - Hereda de PantallaSeleccionBase
//Para elegir cuál estadio modificar

//RELACIÓN CON OTRAS CLASES:
//- Hereda de PantallaSeleccionBase (extends)
//- ServiceEstadio: para obtener todos los estadios de la BD
//- PanelManager: para cambiar a FormularioEstadio


public class PantallaSeleccionEstadios extends PantallaSeleccionBase {
    //ATRIBUTOS ESPECÍFICOS DE ESTADIO -----
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para consultar los estadios en la BD
    //IMPORTANTE: Esta línea se ejecuta ANTES del constructor
    //Es decir, cuando se crea un objeto PantallaSeleccionEstadios,
    //primero se inicializa serviceEstadio, y DESPUÉS se ejecuta el constructor


    //CONSTRUCTOR -----
    public PantallaSeleccionEstadios(PanelManager panel) {
        super(panel); //llama al constructor de PantallaSeleccionBase
        //En este momento:
        //1. serviceEstadio ya existe (se creó en la línea 27)
        //2. PantallaSeleccionBase ya armó toda la estructura visual (ComboBox, botones, etc.)
        //3. PERO el ComboBox todavía está vacío porque no se cargaron los datos

        //AHORA llamamos a inicializar() para cargar los estadios en el ComboBox
        //Como serviceEstadio ya existe, no va a dar error de null
        inicializar();
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "SELECCIONAR ESTADIO A MODIFICAR";
    }


    //METODO 2 - getInstruccion() -----
    @Override
    public String getInstruccion() {
        return "Seleccione un estadio";
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todos los estadios de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEstadio.consultarTodos(); //devuelve List<Estadio>
        //Este método es llamado por cargarElementosEnCombo() (que está en PantallaSeleccionBase)
        //cargarElementosEnCombo() recorre la lista y llama a formatearElementoParaCombo() para cada estadio
    }


    //METODO 4 - formatearElementoParaCombo() -----
    //Define cómo se muestra cada estadio en el ComboBox
    //Formato: "ID - Nombre"
    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio

        //Devolvemos un String con el formato que queremos mostrar en el ComboBox
        //Ejemplo: "1 - Estadio Luna Park"
        return estadio.getIdEstadio() + " - " + estadio.getNombre();
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay estadios disponibles para modificar";
    }


    //METODO 6 - abrirFormularioModificarConElemento() -----
    //Abre FormularioEstadio en modo modificar con el estadio seleccionado
    @Override
    public void abrirFormularioModificarConElemento(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio

        //OPCIÓN 1: Usar el método especial de PanelManager para estadios
        //Este método ya existía en tu código original
        getPanelManager().mostrarFormularioModificar(estadio);

        //OPCIÓN 2 (alternativa): Crear el formulario directamente
        //Si prefieres hacerlo así, descomentá estas líneas y comentá la línea de arriba:
        //FormularioEstadio formulario = new FormularioEstadio(getPanelManager(), estadio);
        //getPanelManager().mostrar(formulario);
    }


    //METODO 7 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 1; //código 1 = MenuPrincipal de estadios (MenuGestionEstadios)
    }
}