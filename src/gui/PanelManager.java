package gui;

import entidades.Estadio;
import javax.swing.*;
import java.awt.*;

    //panel manager es el controlador que tiene la ventana (JFrame), cambia entre pantallas y coordina todo
    //tengo que tener todas las pantallas que voy a utilizar
    //atenti a lo de panel manager en formulario, lo de this y lo de mostrar


    /*
     * PANEL MANAGER - El controlador de pantallas
     *tiene una única ventana (JFrame) y va cambiando el contenido.
     *
     * PANTALLAS QUE MANEJA:
     * 1 = MenuPrincipal
     * 2 = FormularioEstadio (crear nuevo)
     * 3 = PantallaSeleccionEstadios (para elegir cuál modificar)
     * 4 = PantallaEliminar
     * 5 = ReporteEstadios
     */


public class PanelManager {
    //ATRIBUTOS. Hacen referencia a las pantallas ------
        private MenuPrincipal menuPrincipal;
        private FormularioEstadio formularioEstadio;
        private PantallaSeleccionEstadios pantallaSeleccionEstadios;
        private PantallaEliminar pantallaEliminar;
        private ReporteEstadios reporteEstadios;
        JFrame jFrame; //aparece aca mi unica ventana



    //CONSTRUCTOR
        public PanelManager(int tipo) //le paso un tipo inicial. tipo - código de la pantalla inicial (normalmente 1 = menú)
        {
            jFrame=new JFrame("Sistema de Gestión de Estadios");
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //para que se cierren las ventanas
            mostrar(tipo); //muestra la pantalla inicial segun el tipo
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
              case 1:
                  // MENÚ PRINCIPAL
                  // Crear el menú principal
                  // this = PanelManager se pasa a sí mismo para que el menú
                  // pueda llamar a panel.mostrar() y cambiar de pantalla
                menuPrincipal =new MenuPrincipal(this);
                    mostrar(menuPrincipal);
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
                    pantallaEliminar = new PantallaEliminar(this);
                    mostrar(pantallaEliminar);
                    break;

                case 5:
                    // REPORTE DE ESTADIOS
                    // Muestra una tabla (JTable) con todos los estadios y sus datos
                    // Incluye: ID, Nombre, Dirección, Capacidad Total
                    reporteEstadios = new ReporteEstadios(this);
                    mostrar(reporteEstadios);
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
