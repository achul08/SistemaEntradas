package gui.ubicacion;

import entidades.Ubicacion;
import gui.PanelManager;
import gui.base.PantallaEliminarBase;
import service.ServiceUbicacion;
import service.ServiceException;

import java.util.List;

//PANTALLA ELIMINAR UBICACION - Hereda de PantallaEliminarBase
//Para elegir cuál ubicación eliminar

//RELACIÓN CON OTRAS CLASES:
//- Hereda de PantallaEliminarBase (extends)
//- ServiceUbicacion: para obtener y eliminar ubicaciones de la BD
//- PanelManager: para cambiar de pantalla


public class PantallaEliminarUbicacion extends PantallaEliminarBase {
    //ATRIBUTOS ESPECÍFICOS DE UBICACION -----
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion(); //para consultar y eliminar ubicaciones en la BD

    //CONSTRUCTOR -----
    public PantallaEliminarUbicacion(PanelManager panel) {
        super(panel); //llama al constructor de PantallaEliminarBase
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "ELIMINAR UBICACIÓN";
    }


    //METODO 2 - getInstruccion() -----
    @Override
    public String getInstruccion() {
        return "Seleccione la ubicación que desea eliminar:";
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todas las ubicaciones de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUbicacion.consultarTodos(); //devuelve List<Ubicacion>
    }


    //METODO 4 - formatearElementoParaCombo() -----
    //Define cómo se muestra cada ubicación en el ComboBox
    //Formato: "ID - Nombre"
    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion
        return ubicacion.getIdUbicacion() + " - " + ubicacion.getNombre();
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay ubicaciones disponibles para eliminar";
    }


    //METODO 6 - obtenerInformacionElemento() -----
    //Devuelve la información de la ubicación para mostrar en el mensaje de confirmación
    @Override
    public String obtenerInformacionElemento(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion

        //formato de la información que se muestra
        return "ID: " + ubicacion.getIdUbicacion() + "\n" +
                "Nombre: " + ubicacion.getNombre() + "\n" +
                "ID Estadio: " + ubicacion.getIdEstadio() + "\n" +
                "Precio: $" + ubicacion.getPrecio() + "\n" +
                "Capacidad: " + ubicacion.getCapacidad();
    }


    //METODO 7 - obtenerIdElemento() -----
    //Obtiene el ID de la ubicación
    @Override
    public int obtenerIdElemento(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion
        return ubicacion.getIdUbicacion();
    }


    //METODO 8 - eliminarEnBD() -----
    //Llama al Service para eliminar la ubicación de la BD
    @Override
    public void eliminarEnBD(int id) throws ServiceException {
        serviceUbicacion.eliminar(id);
    }


    //METODO 9 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 30; //código 30 = MenuGestionUbicaciones
    }
}