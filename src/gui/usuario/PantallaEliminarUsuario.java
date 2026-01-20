package gui.usuario;

import entidades.Usuario;
import gui.PanelManager;
import gui.base.PantallaEliminarBase;
import service.ServiceUsuario;
import service.ServiceException;

import java.util.List;

//PANTALLA ELIMINAR USUARIO - Hereda de PantallaEliminarBase
//Para elegir cuál usuario eliminar

//RELACIÓN CON OTRAS CLASES:
//- Hereda de PantallaEliminarBase (extends)
//- ServiceUsuario: para obtener y eliminar usuarios de la BD
//- PanelManager: para cambiar de pantalla


public class PantallaEliminarUsuario extends PantallaEliminarBase {
    //ATRIBUTOS ESPECÍFICOS DE USUARIO
    private ServiceUsuario serviceUsuario = new ServiceUsuario(); //para consultar y eliminar usuarios en la BD

    //CONSTRUCTOR
    public PantallaEliminarUsuario(PanelManager panel) {
        super(panel); //llama al constructor de PantallaEliminarBase
        inicializar(); //carga los usuarios en el ComboBox
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS

    //METODO 1 - getTitulo()
    @Override
    public String getTitulo() {
        return "ELIMINAR USUARIO";
    }


    //METODO 2 - getInstruccion()
    @Override
    public String getInstruccion() {
        return "Seleccione el usuario que desea eliminar:";
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
        return "No hay usuarios disponibles para eliminar";
    }


    //METODO 6 - obtenerInformacionElemento()
    //Devuelve la información del usuario para mostrar en el mensaje de confirmación
    @Override
    public String obtenerInformacionElemento(Object elemento) {
        Usuario usuario = (Usuario) elemento; //cast de Object a Usuario

        //formato de la información que se muestra
        return "ID: " + usuario.getIdUsuario() + "\n" +
                "Nombre: " + usuario.getNombre() + "\n" +
                "Apellido: " + usuario.getApellido() + "\n" +
                "Username: " + usuario.getUsername() + "\n" +
                "Rol: " + usuario.getRol();
    }


    //METODO 7 - obtenerIdElemento()
    //Obtiene el ID del usuario
    @Override
    public int obtenerIdElemento(Object elemento) {
        Usuario usuario = (Usuario) elemento; //cast de Object a Usuario
        return usuario.getIdUsuario();
    }


    //METODO 8 - eliminarEnBD()
    //Llama al Service para eliminar el usuario de la BD
    @Override
    public void eliminarEnBD(int id) throws ServiceException {
        serviceUsuario.eliminar(id);
    }


    //METODO 9 - getCodigoMenuPrincipal()
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 11; //código 11 = MenuGestionUsuarios
    }
}