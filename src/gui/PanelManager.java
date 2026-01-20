package gui;

import entidades.Estadio;
import entidades.Usuario;
import gui.estadio.FormularioEstadio;
import gui.estadio.PantallaEliminarEstadio;
import gui.estadio.PantallaSeleccionEstadios;
import gui.estadio.ReporteEstadios;
import gui.estadio.MenuGestionEstadios;
import gui.ubicacion.MenuGestionUbicaciones;
import gui.ubicacion.FormularioUbicacion;
import gui.ubicacion.PantallaSeleccionUbicaciones;
import gui.ubicacion.PantallaEliminarUbicacion;
import gui.ubicacion.ReporteUbicaciones;
import gui.espectaculo.MenuGestionEspectaculos;
import gui.espectaculo.FormularioEspectaculo;
import gui.espectaculo.PantallaSeleccionEspectaculos;
import gui.espectaculo.PantallaEliminarEspectaculo;
import gui.espectaculo.ReporteEspectaculos;
import gui.usuario.*;
import gui.venta.FormularioVenta;
import gui.venta.ReporteVentas;
import gui.venta.ReporteVentasVendedor;

import javax.swing.*;
import java.awt.*;

    //panel manager es el controlador que tiene la ventana (JFrame), cambia entre pantallas y coordina todo
    //tengo que tener todas las pantallas que voy a utilizar
    //atenti a lo de panel manager en formulario, lo de this y lo de mostrar


    /*
     * PANEL MANAGER - El controlador de pantallas
     *tiene una única ventana (JFrame) y va cambiando el contenido.
     */


public class PanelManager {
    //ATRIBUTOS. Hacen referencia a las pantallas ------
        private FormularioEstadio formularioEstadio;
        private PantallaSeleccionEstadios pantallaSeleccionEstadios;
        private PantallaEliminarEstadio pantallaEliminarEstadio;
        private ReporteEstadios reporteEstadios;
        private MenuGestionEstadios menuGestionEstadios;
        private MenuGestionUbicaciones menuGestionUbicaciones;
        private FormularioUbicacion formularioUbicacion;
        private PantallaSeleccionUbicaciones pantallaSeleccionUbicaciones;
        private PantallaEliminarUbicacion pantallaEliminarUbicacion;
        private ReporteUbicaciones reporteUbicaciones;
        private MenuGestionEspectaculos menuGestionEspectaculos;
        private FormularioEspectaculo formularioEspectaculo;
        private PantallaSeleccionEspectaculos pantallaSeleccionEspectaculos;
        private PantallaEliminarEspectaculo pantallaEliminarEspectaculo;
        private ReporteEspectaculos reporteEspectaculos;
        private Usuario usuarioLogueado; //guarda el usuario que hizo login
        JFrame jFrame; //aparece aca mi unica ventana



    //CONSTRUCTOR
        public PanelManager(int tipo) //le paso un tipo inicial. tipo - código de la pantalla inicial (normalmente 1 = menú)
        {
            jFrame=new JFrame("Sistema de Gestión de Estadios");
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //para que se cierren las ventanas
            mostrar(tipo); //muestra la pantalla inicial segun el tipo
        }


    //NUEVOS MÉTODOS - Para manejar el usuario logueado
    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public int getIdUsuarioLogueado() {
        if(usuarioLogueado != null) {
            return usuarioLogueado.getIdUsuario();
        }
        return 0; //si no hay usuario logueado, devuelve 0
    }

        //METODO
    /*
    Este método cambia el contenido de la ventana
     * Recibe un JPanel (cualquier pantalla) y lo muestra

     * 1. Saca el panel anterior
     * 2. Pone el nuevo panel
     * 3. Refresca la ventana
     *
     * Es un panel porque todas las vistas heredan de JPanel (polimorfismo) (*)
     */
        public void mostrar(JPanel panel) //(*)
        {
            jFrame.getContentPane().removeAll(); //SACA EL PANEL ANTERIOR
            jFrame.getContentPane().add(BorderLayout.CENTER,panel); // PONE EL NUEVO PANEL
            jFrame.getContentPane().validate(); //se fija que este todo definido correctamente
            jFrame.getContentPane().repaint(); // Redibujar
            jFrame.setVisible(true); //MUESTRA LA VENTANA
            jFrame.pack(); //se acomoda segun la resolucion de la computadora
        }




        //METODO
    /*
     * Este método crea y muestra una pantalla según su código numérico
     */
        public void mostrar(int codigoPantalla) //este mostrar hace el trabajo de mostrar en pantalla. El parametro es codigoPantalla - el número que identifica la pantalla
        {
            switch (codigoPantalla)
            { //aca deberia enumerar las distintas vistas que deberia tener para crearla o mostrarla
                case 0:  // NUEVO - Pantalla de Login
                    PantallaLogin pantallaLogin = new PantallaLogin(this);
                    mostrar(pantallaLogin);
                    break;

                case 1:
                  // MENÚ PRINCIPAL
                  // Crear el menú principal
                  // this = PanelManager se pasa a sí mismo para que el menú
                  // pueda llamar a panel.mostrar() y cambiar de pantalla
                    menuGestionEstadios = new MenuGestionEstadios(this);
                    mostrar(menuGestionEstadios);
                    break;

                case 2:
                    // FORMULARIO CREAR ESTADIO (modo crear, formulario vacío)
                    // Llama al constructor: FormularioEstadio(panel)
                    // que pone modoModificar = false
                    formularioEstadio = new FormularioEstadio(this);
                    mostrar(formularioEstadio);
                    break;

                case 3:
                    // PANTALLA SELECCIÓN ESTADIO (para elegir cuál modificar)
                    // Esta pantalla muestra un ComboBox con todos los estadios
                    // Cuando el usuario selecciona uno y hace click en "Modificar",
                    // llama a: panel.mostrarFormularioModificar(estadio)
                    pantallaSeleccionEstadios = new PantallaSeleccionEstadios(this);
                    mostrar(pantallaSeleccionEstadios);
                    break;

                case 4:
                    // PANTALLA ELIMINAR
                    // Muestra un ComboBox con todos los estadios
                    // El usuario selecciona uno y hace click en "Eliminar"
                    // Antes de eliminar, se pide confirmación
                    pantallaEliminarEstadio = new PantallaEliminarEstadio(this);
                    mostrar(pantallaEliminarEstadio);
                    break;

                case 5:
                    // REPORTE DE ESTADIOS
                    // Muestra una tabla (JTable) con todos los estadios y sus datos
                    // Incluye: ID, Nombre, Dirección, Capacidad Total
                    reporteEstadios = new ReporteEstadios(this);
                    mostrar(reporteEstadios);
                    break;

                case 10:  // NUEVO - Menú Administrador
                    MenuAdmin menuAdmin = new MenuAdmin(this);
                    mostrar(menuAdmin);
                    break;

                case 11:  // Gestión de Usuarios
                    MenuGestionUsuarios menuGestionUsuarios = new MenuGestionUsuarios(this);
                    mostrar(menuGestionUsuarios);
                    break;

                case 12:  // FormularioUsuario (crear)
                    FormularioUsuario formularioUsuario = new FormularioUsuario(this);
                    mostrar(formularioUsuario);
                    break;

                case 13:  // PantallaSeleccionUsuarios
                    PantallaSeleccionUsuarios pantallaSeleccionUsuarios = new PantallaSeleccionUsuarios(this);
                    mostrar(pantallaSeleccionUsuarios);
                    break;

                case 14:  // PantallaEliminarUsuario
                    PantallaEliminarUsuario pantallaEliminarUsuario = new PantallaEliminarUsuario(this);
                    mostrar(pantallaEliminarUsuario);
                    break;

                case 15:  // ReporteUsuarios
                    ReporteUsuarios reporteUsuarios = new ReporteUsuarios(this);
                    mostrar(reporteUsuarios);
                    break;

                case 20:  // NUEVO - Menú Vendedor
                    MenuVendedor menuVendedor = new MenuVendedor(this);
                    mostrar(menuVendedor);
                    break;

                case 21:  // Registrar Venta
                    FormularioVenta formularioVenta = new FormularioVenta(this);
                    mostrar(formularioVenta);
                    break;

                case 22:  // Ver Espectáculos Disponibles (para VENDEDOR)
                    //Crear el reporte con el constructor especial para vendedor:
                    //- true = solo mostrar espectáculos activos
                    //- 20 = volver al MenuVendedor
                    ReporteEspectaculos reporteEspectaculosVendedor = new ReporteEspectaculos(this, true, 20);
                    mostrar(reporteEspectaculosVendedor);
                    break;

                case 23:  // Ver Mis Ventas
                    ReporteVentasVendedor reporteVentasVendedor = new ReporteVentasVendedor(this);
                    mostrar(reporteVentasVendedor);
                    break;

                case 30:  // MenuGestionUbicaciones
                    menuGestionUbicaciones = new MenuGestionUbicaciones(this);
                    mostrar(menuGestionUbicaciones);
                    break;

                case 31:  // FormularioUbicacion (crear)
                    formularioUbicacion = new FormularioUbicacion(this);
                    mostrar(formularioUbicacion);
                    break;

                case 32:  // PantallaSeleccionUbicaciones
                    pantallaSeleccionUbicaciones = new PantallaSeleccionUbicaciones(this);
                    mostrar(pantallaSeleccionUbicaciones);
                    break;

                case 33:  // PantallaEliminarUbicacion
                    pantallaEliminarUbicacion = new PantallaEliminarUbicacion(this);
                    mostrar(pantallaEliminarUbicacion);
                    break;

                case 34:  // ReporteUbicaciones
                    reporteUbicaciones = new ReporteUbicaciones(this);
                    mostrar(reporteUbicaciones);
                    break;

                case 40:  // MenuGestionEspectaculos
                    // Crear el menú de gestión de espectáculos
                    // Es similar al MenuGestionUbicaciones pero para espectáculos
                    menuGestionEspectaculos = new MenuGestionEspectaculos(this);
                    mostrar(menuGestionEspectaculos);
                    break;

                case 41:  // FormularioEspectaculo (crear)
                    // Crear el formulario vacío para crear un espectáculo nuevo
                    // Llama al constructor: FormularioEspectaculo(panel)
                    // que pone modoModificar = false
                    formularioEspectaculo = new FormularioEspectaculo(this);
                    mostrar(formularioEspectaculo);
                    break;

                case 42:  // PantallaSeleccionEspectaculos
                    // Pantalla para ELEGIR qué espectáculo modificar
                    // Muestra un ComboBox con todos los espectáculos
                    // Cuando el usuario selecciona uno y hace click en "Modificar",
                    // se abre FormularioEspectaculo en modo modificar
                    pantallaSeleccionEspectaculos = new PantallaSeleccionEspectaculos(this);
                    mostrar(pantallaSeleccionEspectaculos);
                    break;

                case 43:  // PantallaEliminarEspectaculo
                    // Pantalla para ELEGIR qué espectáculo eliminar
                    // Muestra un ComboBox con todos los espectáculos
                    // El usuario selecciona uno y confirma la eliminación
                    pantallaEliminarEspectaculo = new PantallaEliminarEspectaculo(this);
                    mostrar(pantallaEliminarEspectaculo);
                    break;

                case 44:  // ReporteEspectaculos
                    // Muestra una tabla (JTable) con todos los espectáculos
                    // Incluye: ID, Nombre, Fecha, ID Estadio, Estado (Activo/Inactivo)
                    reporteEspectaculos = new ReporteEspectaculos(this);
                    mostrar(reporteEspectaculos);
                    break;

                case 50:  // Reportes de Ventas
                    ReporteVentas reporteVentas = new ReporteVentas(this);
                    mostrar(reporteVentas);
                    break;
            }
        }


        /*
        METODO MOSTRAR FORMULARIO MODIFICAR
     * Este método abre el FormularioEstadio en MODO MODIFICAR
     * Se llama desde PantallaSeleccionEstadios cuando el usuario
     * selecciona un estadio y hace click en "Modificar"
     *
     * 1. PantallaSeleccionEstadios llama: panel.mostrarFormularioModificar(estadio)
     * 2. Este método crea: new FormularioEstadio(this, estadio)
     * 3. FormularioEstadio entra en modo modificar y llena los campos

     *  estadio - el estadio que se va a modificar
     */

        public void mostrarFormularioModificar(Estadio estadio) {
            // Crear FormularioEstadio con el constructor de MODIFICAR
            // Este constructor recibe: (PanelManager, Estadio)
            // - Pone modoModificar = true
            // - Guarda el ID del estadio
            // - Llena los campos con los datos del estadio
            formularioEstadio = new FormularioEstadio(this, estadio);
            mostrar(formularioEstadio);
        }



    }
