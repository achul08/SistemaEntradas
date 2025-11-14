package gui;

import entidades.Estadio;
import service.ServiceEstadio;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/*PANTALLA SELECCIÓN ESTADIO - Para elegir cuál modificar

Esta pantalla muestra un ComboBox con todos los estadios disponibles.
El usuario selecciona uno y hace click en "Modificar".

RELACIÓN CON OTRAS CLASES:
- ServiceEstadio: para obtener todos los estadios de la BD
- PanelManager: para cambiar a FormularioEstadio
- FormularioEstadio: se abre en modo modificar con el estadio seleccionado
 */


public class PantallaSeleccionEstadios extends JPanel {
    //ATRIBUTOS -----
    ServiceEstadio serviceEstadio;  //para consultar los estadios en la bd
    PanelManager panel; //para cambiar de pantalla
    JPanel pantallaSeleccion; //panel interno que contiene todo


    //combo box
    JComboBox<String> comboEstadios;

    JButton jButtonModificar;
    JButton jButtonCancelar;



    // Lista con TODOS los estadios de la BD
    // La necesitamos para saber cuál estadio seleccionó el usuario
    // Ejemplo: Usuario selecciona índice 0 del combo → tomamos listaEstadios.get(0)
    List<Estadio> listaEstadios;


    //CONSTRUCTOR ------
    public PantallaSeleccionEstadios(PanelManager panel){
        this.panel = panel;
        // this.panel = el atributo de ESTA clase (PantallaSeleccionEstadio)
        // panel = el parámetro que recibí (viene del PanelManager)
        // Estoy guardando el panel que me pasaron en mi atributo panel
        serviceEstadio = new ServiceEstadio();
        armarPantalla();
    }

    //METODO ARMAR PANTALLA ----------
    /*
     Este método construye toda la interfaz visual
     * ESTRUCTURA:
     * - NORTH: Título
     * - CENTER: Label + ComboBox
     * - SOUTH: Botones
     */

public void armarPantalla() {
    //crear el panel interno
    pantallaSeleccion = new JPanel();
    pantallaSeleccion.setLayout(new BorderLayout(10, 10));
    pantallaSeleccion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


    //ZONA NORTE. TITULO ------
    JLabel titulo = new JLabel("SELECCIONAR ESTADIO A MODIFICAR", SwingConstants.CENTER);
    titulo.setFont(new Font("Arial", Font.BOLD, 20));
    titulo.setForeground(new Color(163, 188, 226));//azul clarito
    titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); //margen abajo

    pantallaSeleccion.add(titulo, BorderLayout.NORTH);


    //ZONA CENTRO. LABEL Y COMBO BOX -------
    JPanel panelCentro = new JPanel();
    panelCentro.setLayout(new GridLayout(2, 1, 10, 10));
    panelCentro.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

    //label que diga seleccione un estadio
    JLabel labelInstruccion = new JLabel("Seleccione un estadio", SwingConstants.CENTER);
    labelInstruccion.setFont(new Font("Arial", Font.PLAIN, 16));


    //crear combobox vacio
    comboEstadios = new JComboBox<>();
    comboEstadios.setFont(new Font("Arial", Font.PLAIN, 14));

    //agregar al panel centro
    panelCentro.add(labelInstruccion);
    panelCentro.add(comboEstadios);

    pantallaSeleccion.add(panelCentro, BorderLayout.CENTER);


    //CARGAR LOS ESTADIOS EN EL COMBOBOX -------
    cargarEstadiosEnCombo();

    //ZONA SUR. BOTONES ----
    JPanel panelBotones = new JPanel();
    panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20,10));

    //boton modificar
    jButtonModificar = new JButton("Modificar");
    jButtonModificar.setFont(new Font("Arial", Font.BOLD, 14));
    jButtonModificar.setBackground(new Color(173, 216, 230));  // Azul clarito
    jButtonModificar.setPreferredSize(new Dimension(150, 40));


    //boton cancelar
    jButtonCancelar = new JButton("Cancelar");
    jButtonCancelar.setFont(new Font("Arial", Font.BOLD, 14));
    jButtonCancelar.setBackground(new Color(220, 220, 220));  // Gris claro
    jButtonCancelar.setPreferredSize(new Dimension(150, 40));

    panelBotones.add(jButtonModificar);
    panelBotones.add(jButtonCancelar);


    pantallaSeleccion.add(panelBotones, BorderLayout.SOUTH);
    this.setLayout(new BorderLayout());
    this.add(pantallaSeleccion, BorderLayout.CENTER);
    // PantallaSeleccionEstadio ES un JPanel
    //Le ponemos BorderLayout y agregamos el panel interno


    //COMPOTAMIENTO DE LOS BOTONES ----
    //boton modificar
    jButtonModificar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            abrirFormularioModificar();
        }
    });

    //boton cancelar
    jButtonCancelar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            panel.mostrar(1);  // Volver al menú principal
        }
    });
}



        //METODO --- CARGAR ESTADIOS EN COMBOBOX
    private void cargarEstadiosEnCombo(){
    try{
        listaEstadios = serviceEstadio.consultarTodos();
        if(listaEstadios.isEmpty()){
            JOptionPane.showMessageDialog(this, "No hay estadios disponibles para modificar", "Sin estadios", JOptionPane.INFORMATION_MESSAGE);
            panel.mostrar(1);
            return;
        }
        //recorrer cada estadio y agregarlo al combobox
        for (Estadio estadio : listaEstadios){
            String item = estadio.getIdEstadio() + " - " + estadio.getNombre();
            comboEstadios.addItem(item);
        }
    } catch (ServiceException e){
        JOptionPane.showMessageDialog(this, "Error ala cargar los estadios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        panel.mostrar(1);
    }
    }



        //METODO --- ABRIR FORM
    // Este método se ejecuta cuando el usuario hace click en "Modificar"
    //FLUJO:
    //1. Obtener el índice seleccionado del ComboBox
    //2. Buscar el estadio correspondiente en listaEstadios
    //3. Abrir FormularioEstadio en modo modificar con ese estadio
    private void abrirFormularioModificar(){
    //verificar que haya algo seleccionado
    if (comboEstadios.getSelectedIndex() == -1){
        JOptionPane.showMessageDialog(this, "Por favor, seleccione un estadio", "Ningún estadio seleccionado", JOptionPane.WARNING_MESSAGE);
        return;
    }
        // Obtener el índice seleccionado
        // Ejemplo: Usuario seleccionó "2 - Bombonera" que está en posición 1
        int indiceSeleccionado = comboEstadios.getSelectedIndex();

        // Obtener el estadio correspondiente de la lista
        // Como el ComboBox y la lista están en el mismo orden,
        // el índice del combo coincide con el índice de la lista
        Estadio estadioSeleccionado = listaEstadios.get(indiceSeleccionado);

        // Abrir FormularioEstadio en modo MODIFICAR con ese estadio
        // Esto llamará al constructor: FormularioEstadio(panel, estadio)
        // que automáticamente:
        // - Pone modoModificar = true
        // - Guarda el ID
        // - Llena los campos con cargarDatos(estadio)
        panel.mostrarFormularioModificar(estadioSeleccionado);
    }




}




