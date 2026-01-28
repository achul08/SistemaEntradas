package gui.usuario;

import entidades.Usuario;
import gui.PanelManager;
import gui.base.PantallaSeleccionBase;
import service.ServiceUsuario;
import service.ServiceException;

import java.util.List;


public class PantallaSeleccionUsuarios extends PantallaSeleccionBase {
    private ServiceUsuario serviceUsuario = new ServiceUsuario(); //para consultar los usuarios en la BD


    //CONSTRUCTOR--------------
    public PantallaSeleccionUsuarios(PanelManager panel) {
        super(panel); //llama al constructor de PantallaSeleccionBase
        inicializar(); //carga los usuarios en el ComboBox
    }

    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS
    @Override
    public String getTitulo() {
        return "SELECCIONAR USUARIO A MODIFICAR";
    }

    @Override
    public String getInstruccion() {
        return "Seleccione un usuario";
    }

    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUsuario.consultarTodos(); //devuelve List<Usuario>
    }

    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Usuario usuario = (Usuario) elemento; //cast de Object a Usuario

        return usuario.getIdUsuario() + " - " + usuario.getNombre() + " " + usuario.getApellido() +
                " (Rol: " + usuario.getRol() + ")";
    }

    @Override
    public String getMensajeSinElementos() {
        return "No hay usuarios disponibles para modificar";
    }


    @Override
    public void abrirFormularioModificarConElemento(Object elemento) {
        Usuario usuario = (Usuario) elemento; //cast de Object a Usuario
        FormularioUsuario formulario = new FormularioUsuario(getPanelManager(), usuario);
        getPanelManager().mostrar(formulario);
    }

    @Override
    public int getCodigoMenuPrincipal() {
        return 11; //código 11 = MenuGestionUsuarios
    }
}