package gui.espectaculo;

import entidades.Espectaculo;
import gui.PanelManager;
import gui.base.PantallaEliminarBase;
import service.ServiceEspectaculo;
import service.ServiceException;

import java.text.SimpleDateFormat;
import java.util.List;

//PANTALLA ELIMINAR ESPECTACULO - Hereda de PantallaEliminarBase
//Para elegir cuál espectáculo eliminar

//RELACIÓN CON OTRAS CLASES:
//- Hereda de PantallaEliminarBase (extends)
//- ServiceEspectaculo: para obtener y eliminar espectáculos de la BD
//- PanelManager: para cambiar de pantalla


public class PantallaEliminarEspectaculo extends PantallaEliminarBase {
    //ATRIBUTOS ESPECÍFICOS DE ESPECTACULO -----
    private ServiceEspectaculo serviceEspectaculo= new ServiceEspectaculo(); //para consultar y eliminar espectáculos en la BD
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); //para mostrar fechas


    //CONSTRUCTOR -----
    public PantallaEliminarEspectaculo(PanelManager panel) {
        super(panel); //llama al constructor de PantallaEliminarBase
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "ELIMINAR ESPECTÁCULO";
    }


    //METODO 2 - getInstruccion() -----
    @Override
    public String getInstruccion() {
        return "Seleccione el espectáculo que desea eliminar:";
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todos los espectáculos de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEspectaculo.consultarTodos(); //devuelve List<Espectaculo>
    }


    //METODO 4 - formatearElementoParaCombo() -----
    //Define cómo se muestra cada espectáculo en el ComboBox
    //Formato: "ID - Nombre (Fecha: dd/MM/yyyy)"
    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Espectaculo espectaculo = (Espectaculo) elemento; //cast de Object a Espectaculo

        String fecha = formatoFecha.format(espectaculo.getFecha());
        return espectaculo.getIdEspectaculo() + " - " + espectaculo.getNombre() +
                " (Fecha: " + fecha + ")";
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay espectáculos disponibles para eliminar";
    }


    //METODO 6 - obtenerInformacionElemento() -----
    //Devuelve la información del espectáculo para mostrar en el mensaje de confirmación
    @Override
    public String obtenerInformacionElemento(Object elemento) {
        Espectaculo espectaculo = (Espectaculo) elemento; //cast de Object a Espectaculo

        String fecha = formatoFecha.format(espectaculo.getFecha());
        String estado;
        if (espectaculo.isActivo()) {
            estado = "Activo";
        } else {
            estado = "Inactivo";
        }
        //formato de la información que se muestra
        return "ID: " + espectaculo.getIdEspectaculo() + "\n" +
                "Nombre: " + espectaculo.getNombre() + "\n" +
                "Fecha: " + fecha + "\n" +
                "ID Estadio: " + espectaculo.getIdEstadio() + "\n" +
                "Estado: " + estado;
    }


    //METODO 7 - obtenerIdElemento() -----
    //Obtiene el ID del espectáculo
    @Override
    public int obtenerIdElemento(Object elemento) {
        Espectaculo espectaculo = (Espectaculo) elemento; //cast de Object a Espectaculo
        return espectaculo.getIdEspectaculo();
    }


    //METODO 8 - eliminarEnBD() -----
    //Llama al Service para eliminar el espectáculo de la BD
    @Override
    public void eliminarEnBD(int id) throws ServiceException {
        serviceEspectaculo.eliminar(id);
    }


    //METODO 9 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 40; //código 40 = MenuGestionEspectaculos
    }
}