package gui.espectaculo;

import entidades.Espectaculo;
import gui.PanelManager;
import gui.base.PantallaSeleccionBase;
import service.ServiceEspectaculo;
import service.ServiceException;

import java.text.SimpleDateFormat;
import java.util.List;

//PANTALLA SELECCIÓN ESPECTACULOS - Hereda de PantallaSeleccionBase
//Para elegir cuál espectáculo modificar

//RELACIÓN CON OTRAS CLASES:
//- Hereda de PantallaSeleccionBase (extends)
//- ServiceEspectaculo: para obtener todos los espectáculos de la BD
//- PanelManager: para cambiar a FormularioEspectaculo


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

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "SELECCIONAR ESPECTÁCULO A MODIFICAR";
    }


    //METODO 2 - getInstruccion() -----
    @Override
    public String getInstruccion() {
        return "Seleccione un espectáculo";
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
        return "No hay espectáculos disponibles para modificar";
    }


    //METODO 6 - abrirFormularioModificarConElemento() -----
    //Abre FormularioEspectaculo en modo modificar con el espectáculo seleccionado
    @Override
    public void abrirFormularioModificarConElemento(Object elemento) {
        Espectaculo espectaculo = (Espectaculo) elemento; //cast de Object a Espectaculo

        //Crear FormularioEspectaculo en modo modificar
        FormularioEspectaculo formulario = new FormularioEspectaculo(getPanelManager(), espectaculo);
        getPanelManager().mostrar(formulario);
    }


    //METODO 7 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 40; //código 40 = MenuGestionEspectaculos
    }
}
