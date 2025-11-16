package gui;

import entidades.Estadio;
import service.ServiceEstadio;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/*
 * PANTALLA ELIMINAR ESTADIO - Para seleccionar y eliminar un estadio
 *
 * Esta pantalla muestra un ComboBox con todos los estadios disponibles.
 * El usuario selecciona uno y hace click en eliminar
 * Antes de eliminar, se pide confirmación.
 *
 * RELACIÓN CON OTRAS CLASES:
 * - ServiceEstadio: para obtener todos los estadios y eliminar el seleccionado
 * - PanelManager: para cambiar de pantalla (volver al menú)
 *
 * 1. Se carga el ComboBox con todos los estadios
 * 2. se selecciona un estadio
 * 3. se hace click en eliminar
 * 4. se pide confirmación con JOptionPane
 * 5. si se confirma → elimina y muestra mensaje de éxito
 * 6. si se cancela → no hace nada
 */


public class PantallaEliminar extends JPanel {
    // ATRIBUTOS -----
    ServiceEstadio serviceEstadio;  // Para consultar y eliminar estadios en la BD
    PanelManager panel;  // Para cambiar de pantalla
    JPanel pantallaEliminar;  // Panel interno que contiene todo

    // Componentes visuales
    JComboBox<String> comboEstadios;  // ComboBox para seleccionar el estadio
    JButton jButtonEliminar;  // Botón para eliminar
    JButton jButtonCancelar;  // Botón para cancelar

    // Lista con TODOS los estadios de la BD
    // La necesitamos para saber cuál estadio seleccionó el usuario
    List<Estadio> listaEstadios;



    // CONSTRUCTOR ------
    public PantallaEliminar(PanelManager panel) {
        this.panel = panel;
        // this.panel = el atributo de ESTA clase (PantallaEliminar)
        // panel = el parámetro que recibí (viene del PanelManager)
        // Estoy guardando el panel que me pasaron en mi atributo panel

        serviceEstadio = new ServiceEstadio();  // Crear el service para usar sus métodos
        armarPantalla();  // Construir la interfaz visual
    }



//METODO ------
    public void armarPantalla() {
        // Crear el panel interno
        pantallaEliminar = new JPanel();
        pantallaEliminar.setLayout(new BorderLayout(10, 10));  // Espacio entre secciones
        pantallaEliminar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Margen general


        // ZONA NORTE - TÍTULO ------
        JLabel titulo = new JLabel("ELIMINAR ESTADIO", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(220, 20, 60));  // Rojo
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));  // Margen abajo

        pantallaEliminar.add(titulo, BorderLayout.NORTH);


        // ZONA CENTRO - LABEL Y COMBOBOX -------
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new GridLayout(2, 1, 10, 10));  // 2 filas, 1 columna
        panelCentro.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));  // Margen

        // Label con instrucciones
        JLabel labelInstruccion = new JLabel("Seleccione el estadio que desea eliminar:", SwingConstants.CENTER);
        labelInstruccion.setFont(new Font("Arial", Font.PLAIN, 16));

        // Crear ComboBox vacio
        comboEstadios = new JComboBox<>();
        comboEstadios.setFont(new Font("Arial", Font.PLAIN, 14));

        // Agregar al panel centro
        panelCentro.add(labelInstruccion);
        panelCentro.add(comboEstadios);

        pantallaEliminar.add(panelCentro, BorderLayout.CENTER);


        // CARGAR LOS ESTADIOS EN EL COMBOBOX -------
        // Este meodo consulta la BD y llena el ComboBox
        cargarEstadiosEnCombo();


        // ZONA SUR - BOTONES ----
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));  // Botones centrados con espacio

        // Botón Eliminar
        jButtonEliminar = new JButton("Eliminar");
        jButtonEliminar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonEliminar.setBackground(new Color(255, 182, 193));  // rojo clarito
        jButtonEliminar.setPreferredSize(new Dimension(150, 40));

        // Botón Cancelar
        jButtonCancelar = new JButton("Cancelar");
        jButtonCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonCancelar.setBackground(new Color(220, 220, 220));  // Gris
        jButtonCancelar.setPreferredSize(new Dimension(150, 40));

        panelBotones.add(jButtonEliminar);
        panelBotones.add(jButtonCancelar);

        pantallaEliminar.add(panelBotones, BorderLayout.SOUTH);


        // Agregar el panel interno a THIS
        // PantallaEliminar ES un JPanel
        // Le ponemos BorderLayout y agregamos el panel interno
        this.setLayout(new BorderLayout());
        this.add(pantallaEliminar, BorderLayout.CENTER);


        // COMPORTAMIENTO DE LOS BOTONES ----
        // Botón Eliminar
        jButtonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarEstadio();  // Llamar al metodo que hace el trabajo
            }
        });

        // Botón Cancelar
        jButtonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.mostrar(1);  // Volver al menú principal
            }
        });
    }


    // METODO --- CARGAR ESTADIOS EN COMBOBOX
    /*
     * consulta todos los estadios de la BD
     * y los carga en el ComboBox para que el usuario pueda elegir.
     *
     * 1. Consultar todos los estadios con serviceEstadio.consultarTodos()
     * 2. Si no hay estadios → mostrar mensaje y volver al menú
     * 3. Si hay estadios → recorrerlos y agregar cada uno al ComboBox
     *
     * FORMATO EN EL COMBOBOX: id-nombre
     */

    private void cargarEstadiosEnCombo() {
        try {
            // Consultar todos los estadios
            listaEstadios = serviceEstadio.consultarTodos();

            // Verificar si hay estadios
            if (listaEstadios.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay estadios disponibles para eliminar",
                        "Sin estadios",
                        JOptionPane.INFORMATION_MESSAGE
                );
                panel.mostrar(1);  // Volver al menú
                return;  // Salir del método
            }

            // Recorrer cada estadio y agregarlo al ComboBox
            for (Estadio estadio : listaEstadios) {
                // Formato: "ID - Nombre"
                String item = estadio.getIdEstadio() + " - " + estadio.getNombre();
                comboEstadios.addItem(item);
            }

        } catch (ServiceException e) {
            // Si hay error al consultar, mostrar mensaje y volver al menú
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los estadios: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            panel.mostrar(1);  // Volver al menú
        }
    }


    // METODO --- ELIMINAR ESTADIO
    /*
     * 1. Verificar que haya algo seleccionado en el ComboBox
     * 2. Obtener el estadio seleccionado
     * 3. Pedir confirmación al usuario (JOptionPane.showConfirmDialog)
     * 4. Si confirma:
     *    - Llamar a serviceEstadio.eliminar(id)
     *    - Mostrar mensaje de éxito
     *    - Volver al menú
     * 5. Si cancela:
     *    - No hacer nada, quedarse en la pantalla
     */
    private void eliminarEstadio() {
        // PASO 1: Verificar que haya algo seleccionado
        if (comboEstadios.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, seleccione un estadio",
                    "Ningún estadio seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;  // Salir sin hacer nada
        }

        // PASO 2: Obtener el índice seleccionado
        int indiceSeleccionado = comboEstadios.getSelectedIndex();

        // Obtener el estadio correspondiente de la lista
        // Como el ComboBox y la lista están en el mismo orden,
        // el índice del combo coincide con el índice de la lista
        Estadio estadioSeleccionado = listaEstadios.get(indiceSeleccionado);

        // PASO 3: Pedir confirmación
        // showConfirmDialog devuelve:
        // - JOptionPane.YES_OPTION si el usuario hace click en "Sí"
        // - JOptionPane.NO_OPTION si hace click en "No"
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar el estadio?\n\n" +
                        "ID: " + estadioSeleccionado.getIdEstadio() + "\n" +
                        "Nombre: " + estadioSeleccionado.getNombre() + "\n" +
                        "Dirección: " + estadioSeleccionado.getDireccion() + "\n\n" +
                        "Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,  // Mostrar botones Sí/No
                JOptionPane.WARNING_MESSAGE  // Icono de advertencia
        );

        // PASO 4: Si el usuario confirmó, eliminar
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // Llamar al Service para eliminar
                // El Service se encarga de:
                // - Verificar que el estadio exista
                // - Llamar al DAO para eliminarlo de la BD
                serviceEstadio.eliminar(estadioSeleccionado.getIdEstadio());

                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(
                        this,
                        "El estadio ha sido eliminado correctamente",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Volver al menú principal
                panel.mostrar(1);

            } catch (ServiceException e) {
                // Si hubo error al eliminar, mostrar el mensaje específico
                // El Service puede lanzar excepciones como:
                // - "El estadio no existe"
                // - "Error en base de datos"
                JOptionPane.showMessageDialog(
                        this,
                        "Error al eliminar el estadio: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        // Si el usuario dijo "No", no hacemos nada
    }
}


