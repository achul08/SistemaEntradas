package gui.espectaculo;

import entidades.Espectaculo;
import gui.PanelManager;
import gui.base.PantallaSeleccionBase;
import service.ServiceEspectaculo;
import service.ServiceException;

import java.text.SimpleDateFormat;
import java.util.List;


public class PantallaSeleccionEspectaculos extends PantallaSeleccionBase {
    //ATRIBUTOS ESPECÍFICOS DE ESPECTACULO -----
    private ServiceEspectaculo serviceEspectaculo = new ServiceEspectaculo(); //para consultar los espectáculos en la BD
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy"); //para mostrar fechas
    //Porque Espectaculo tiene un atributo fecha que queremos mostrar en el ComboBox.


    //CONSTRUCTOR -----
    public PantallaSeleccionEspectaculos(PanelManager panel) {
        super(panel); //llama al constructor de PantallaSeleccionBase
        inicializar(); //carga los espectáculos en el ComboBox
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----
    @Override
    public String getTitulo() {
        return "SELECCIONAR ESPECTÁCULO A MODIFICAR";
    }

    @Override
    public String getInstruccion() {
        return "Seleccione un espectáculo";
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
        return "No hay espectáculos disponibles para modificar";
    }

    @Override
    public void abrirFormularioModificarConElemento(Object elemento) {
        Espectaculo espectaculo = (Espectaculo) elemento; //cast de Object a Espectaculo

        //Crear FormularioEspectaculo en modo modificar
        FormularioEspectaculo formulario = new FormularioEspectaculo(getPanelManager(), espectaculo);
        getPanelManager().mostrar(formulario);
    }

    @Override
    public int getCodigoMenuPrincipal() {
        return 40; //código 40 = MenuGestionEspectaculos
    }
}
