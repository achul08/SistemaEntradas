package gui.usuario;

import entidades.Usuario;
import gui.PanelManager;
import gui.base.PantallaEliminarBase;
import service.ServiceUsuario;
import service.ServiceException;

import java.util.List;


public class PantallaEliminarUsuario extends PantallaEliminarBase {
    //ATRIBUTOS ESPECÍFICOS DE USUARIO
    private ServiceUsuario serviceUsuario = new ServiceUsuario(); //para consultar y eliminar usuarios en la BD

    //CONSTRUCTOR ------------------------
    public PantallaEliminarUsuario(PanelManager panel) {
        super(panel);
        inicializar();
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS-----------------
    @Override
    public String getTitulo() {
        return "ELIMINAR USUARIO";
    }

    @Override
    public String getInstruccion() {
        return "Seleccione el usuario que desea eliminar:";
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
        return "No hay usuarios disponibles para eliminar";
    }


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


    @Override
    public int obtenerIdElemento(Object elemento) {
        Usuario usuario = (Usuario) elemento; //cast de Object a Usuario
        return usuario.getIdUsuario();
    }


    @Override
    public void eliminarEnBD(int id) throws ServiceException {
        serviceUsuario.eliminar(id);
    }


    @Override
    public int getCodigoMenuPrincipal() {
        return 11;
    }
}