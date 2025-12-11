package gui.usuario;

import entidades.Usuario;
import gui.PanelManager;
import gui.base.PantallaSeleccionBase;
import service.ServiceUsuario;
import service.ServiceException;

import java.util.List;

//PANTALLA SELECCIÓN USUARIOS - Hereda de PantallaSeleccionBase
//Para elegir cuál usuario modificar

//RELACIÓN CON OTRAS CLASES:
//- Hereda de PantallaSeleccionBase (extends)
//- ServiceUsuario: para obtener todos los usuarios de la BD
//- PanelManager: para cambiar a FormularioUsuario


public class PantallaSeleccionUsuarios extends PantallaSeleccionBase {
    //ATRIBUTOS ESPECÍFICOS DE USUARIO
    private ServiceUsuario serviceUsuario = new ServiceUsuario(); //para consultar los usuarios en la BD


    //CONSTRUCTOR
    public PantallaSeleccionUsuarios(PanelManager panel) {
        super(panel); //llama al constructor de PantallaSeleccionBase
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS

    //METODO 1 - getTitulo()
    @Override
    public String getTitulo() {
        return "SELECCIONAR USUARIO A MODIFICAR";
    }


    //METODO 2 - getInstruccion()
    @Override
    public String getInstruccion() {
        return "Seleccione un usuario";
    }


    //METODO 3 - consultarTodos()
    //Consulta todos los usuarios de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUsuario.consultarTodos(); //devuelve List<Usuario>
    }


    //METODO 4 - formatearElementoParaCombo()
    //Define cómo se muestra cada usuario en el ComboBox
    //Formato: "ID - Nombre Apellido (Rol: ADMINISTRADOR/VENDEDOR)"
    @Override
    public String formatearElementoParaCombo(Object elemento) {
        Usuario usuario = (Usuario) elemento; //cast de Object a Usuario

        return usuario.getIdUsuario() + " - " + usuario.getNombre() + " " + usuario.getApellido() +
                " (Rol: " + usuario.getRol() + ")";
    }


    //METODO 5 - getMensajeSinElementos()
    @Override
    public String getMensajeSinElementos() {
        return "No hay usuarios disponibles para modificar";
    }


    //METODO 6 - abrirFormularioModificarConElemento()
    //Abre FormularioUsuario en modo modificar con el usuario seleccionado
    @Override
    public void abrirFormularioModificarConElemento(Object elemento) {
        Usuario usuario = (Usuario) elemento; //cast de Object a Usuario

        //Crear FormularioUsuario en modo modificar
        //Esto llama al constructor: FormularioUsuario(panel, usuario)
        //que automáticamente pone modoModificar = true y llena los campos
        FormularioUsuario formulario = new FormularioUsuario(getPanelManager(), usuario);
        getPanelManager().mostrar(formulario);
    }


    //METODO 7 - getCodigoMenuPrincipal()
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 11; //código 11 = MenuGestionUsuarios
    }
}