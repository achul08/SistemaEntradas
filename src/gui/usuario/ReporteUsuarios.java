package gui.usuario;

import entidades.Usuario;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceUsuario;
import service.ServiceException;

import java.util.List;

//REPORTE USUARIOS - Hereda de ReporteBase
//Muestra una tabla con todos los usuarios

//RELACIÓN CON OTRAS CLASES:
//- Hereda de ReporteBase (extends)
//- ServiceUsuario: para obtener todos los usuarios de la BD
//- PanelManager: para cambiar de pantalla


public class ReporteUsuarios extends ReporteBase {
    //ATRIBUTOS ESPECÍFICOS DE USUARIO
    private ServiceUsuario serviceUsuario = new ServiceUsuario(); //para consultar los usuarios


    //CONSTRUCTOR
    public ReporteUsuarios(PanelManager panelManager) {
        super(panelManager); //llama al constructor de ReporteBase
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS

    //METODO 1 - getTitulo()
    @Override
    public String getTitulo() {
        return "REPORTE DE USUARIOS";
    }


    //METODO 2 - obtenerNombresColumnas()
    //Define los nombres de las columnas de la tabla
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


    //METODO 3 - consultarTodos()
    //Consulta todos los usuarios de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUsuario.consultarTodos(); //devuelve List<Usuario>
    }


    //METODO 4 - convertirElementoAFila()
    //Convierte un usuario en un array de Object para mostrar en la tabla
    //El ORDEN debe coincidir con el orden de las columnas
    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Usuario usuario = (Usuario) elemento; //cast de Object a Usuario

        //crear el array con los datos del usuario
        //IMPORTANTE: el orden debe ser igual al de obtenerNombresColumnas()
        return new Object[]{
                usuario.getIdUsuario(),      // columna 0: "ID Usuario"
                usuario.getNombre(),         // columna 1: "Nombre"
                usuario.getApellido(),       // columna 2: "Apellido"
                usuario.getUsername(),       // columna 3: "Username"
                usuario.getRol()             // columna 4: "Rol"
        };
        //NOTA: No mostramos la contraseña por seguridad
    }


    //METODO 5 - getMensajeSinElementos()
    @Override
    public String getMensajeSinElementos() {
        return "No hay usuarios registrados";
    }


    //METODO 6 - getCodigoMenuPrincipal()
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 11; //código 11 = MenuGestionUsuarios
    }
}