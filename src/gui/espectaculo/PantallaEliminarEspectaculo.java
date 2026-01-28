package gui.espectaculo;

import entidades.Espectaculo;
import gui.PanelManager;
import gui.base.PantallaEliminarBase;
import service.ServiceEspectaculo;
import service.ServiceException;

import java.text.SimpleDateFormat;
import java.util.List;

public class PantallaEliminarEspectaculo extends PantallaEliminarBase {
    //ATRIBUTOS ESPECÍFICOS DE ESPECTACULO -----
    private ServiceEspectaculo serviceEspectaculo= new ServiceEspectaculo(); //para consultar y eliminar espectáculos en la BD
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); //para mostrar fechas


    //CONSTRUCTOR -----
    public PantallaEliminarEspectaculo(PanelManager panel) {
        super(panel); //llama al constructor de PantallaEliminarBase
        inicializar(); //carga los espectáculos en el ComboBox
    }

    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----
    @Override
    public String getTitulo() {
        return "ELIMINAR ESPECTÁCULO";
    }

    @Override
    public String getInstruccion() {
        return "Seleccione el espectáculo que desea eliminar:";
    }

    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEspectaculo.consultarTodos(); //devuelve List<Espectaculo>
    }
    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Espectaculo espectaculo = (Espectaculo) elemento; //cast de Object a Espectaculo

        String fecha = formatoFecha.format(espectaculo.getFecha());
        return espectaculo.getIdEspectaculo() + " - " + espectaculo.getNombre() +
                " (Fecha: " + fecha + ")";
    }

    @Override
    public String getMensajeSinElementos() {
        return "No hay espectáculos disponibles para eliminar";
    }

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

    @Override
    public int obtenerIdElemento(Object elemento) {
        Espectaculo espectaculo = (Espectaculo) elemento; //cast de Object a Espectaculo
        return espectaculo.getIdEspectaculo();
    }


    @Override
    public void eliminarEnBD(int id) throws ServiceException {
        serviceEspectaculo.eliminar(id);
    }


    @Override
    public int getCodigoMenuPrincipal() {
        return 40;
    }
}