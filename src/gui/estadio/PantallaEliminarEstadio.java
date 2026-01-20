package gui.estadio;

import entidades.Estadio;
import gui.PanelManager;
import gui.base.PantallaEliminarBase;
import service.ServiceEstadio;
import service.ServiceException;

import java.util.List;

//PANTALLA ELIMINAR ESTADIO - Hereda de PantallaEliminarBase
//Para elegir cuál estadio eliminar

//RELACIÓN CON OTRAS CLASES:
//- Hereda de PantallaEliminarBase (extends)
//- ServiceEstadio: para obtener y eliminar estadios de la BD
//- PanelManager: para cambiar de pantalla


public class PantallaEliminarEstadio extends PantallaEliminarBase {
    //ATRIBUTOS ESPECÍFICOS DE ESTADIO -----
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para consultar y eliminar estadios en la BD

    //CONSTRUCTOR -----
    public PantallaEliminarEstadio(PanelManager panel) {
        //serviceEstadio ya se creó en la línea 23
        //Entonces ya existe y está listo para usar

        super(panel); //llama al constructor de PantallaEliminarBase
        //El padre arma toda la pantalla pero NO llena el ComboBox

        //AHORA llamamos a inicializar() para llenar el ComboBox
        inicializar(); //este método está en PantallaEliminarBase
        //inicializar() → cargarElementosEnCombo() → consultarTodos() → serviceEstadio.consultarTodos()
        //Como serviceEstadio ya existe, funciona perfecto
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "ELIMINAR ESTADIO";
    }


    //METODO 2 - getInstruccion() -----
    @Override
    public String getInstruccion() {
        return "Seleccione el estadio que desea eliminar:";
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todos los estadios de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEstadio.consultarTodos(); //devuelve List<Estadio>
    }


    //METODO 4 - formatearElementoParaCombo() -----
    //Define cómo se muestra cada estadio en el ComboBox
    //Formato: "ID - Nombre"
    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio
        return estadio.getIdEstadio() + " - " + estadio.getNombre();
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay estadios disponibles para eliminar";
    }


    //METODO 6 - obtenerInformacionElemento() -----
    //Devuelve la información del estadio para mostrar en el mensaje de confirmación
    @Override
    public String obtenerInformacionElemento(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio

        //formato de la información que se muestra
        return "ID: " + estadio.getIdEstadio() + "\n" +
                "Nombre: " + estadio.getNombre() + "\n" +
                "Dirección: " + estadio.getDireccion() + "\n" +
                "Capacidad Total: " + estadio.getCapacidadTotal();
    }


    //METODO 7 - obtenerIdElemento() -----
    //Obtiene el ID del estadio
    @Override
    public int obtenerIdElemento(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio
        return estadio.getIdEstadio();
    }


    //METODO 8 - eliminarEnBD() -----
    //Llama al Service para eliminar el estadio de la BD
    @Override
    public void eliminarEnBD(int id) throws ServiceException {
        serviceEstadio.eliminar(id);
    }


    //METODO 9 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 1; //código 1 = MenuPrincipal de estadios
    }
}