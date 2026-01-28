package gui.usuario;

import entidades.Usuario;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceUsuario;
import service.ServiceException;

import java.util.List;



public class ReporteUsuarios extends ReporteBase {
    //ATRIBUTOS ESPECÍFICOS DE USUARIO
    private ServiceUsuario serviceUsuario = new ServiceUsuario(); //para consultar los usuarios
    //serviceUsuario se crea ACÁ, antes del constructor
    //Por eso cuando llamemos a inicializar() ya va a existir


    //CONSTRUCTOR ------------------------
    public ReporteUsuarios(PanelManager panelManager) {
        super(panelManager); //llama al constructor de ReporteBase
        inicializar();
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS --------------
    @Override
    public String getTitulo() {
        return "REPORTE DE USUARIOS";
    }

    @Override
    public String[] obtenerNombresColumnas() {
        return new String[]{
                "ID Usuario",
                "Nombre",
                "Apellido",
                "Username",
                "Rol"
        };
    }


    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUsuario.consultarTodos(); //devuelve List<Usuario>
    }


    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Usuario usuario = (Usuario) elemento; //cast de Object a Usuario
        return new Object[]{
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getUsername(),
                usuario.getRol()
        };
    }


    @Override
    public String getMensajeSinElementos() {
        return "No hay usuarios registrados";
    }


    @Override
    public int getCodigoMenuPrincipal() {
        return 11; //código 11 = MenuGestionUsuarios
    }
}