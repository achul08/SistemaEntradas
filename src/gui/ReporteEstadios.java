package gui;

import entidades.Estadio;
import service.ServiceEstadio;
import service.ServiceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


/*
 * REPORTE ESTADIOS - Tabla con todos los estadios
 *
 * Esta pantalla muestra una tabla con todos los estadios guardados en la BD.
 *
 * RELACIÓN CON OTRAS CLASES:
 * - ServiceEstadio: para obtener todos los estadios
 * - PanelManager: para volver al menú
 */


//combo box, list box


public class ReporteEstadios extends JPanel { //entiende de panel para poder usarla en el panel manager
        private PanelManager panelManager; // Para cambiar de pantalla
        ServiceEstadio serviceEstadio;  // Para consultar los estadios
        JPanel panelReporte;  // Panel interno que contiene todo

        private JTable jTable; //es el dibujo
        private DefaultTableModel contenido; //son los datos que le da al table
        private JScrollPane scrollPane; //tiene un scroll para poder hacerlo sobre la tabla
    //*

        JButton jButtonVolver;  // Botón para volver al menú


        //CONSTRUCTOR -------
        public ReporteEstadios(PanelManager panelManager)  { //parametro: panelManager - gestor de pantalla
            this.panelManager = panelManager; //recibo el panel manager y los asocio
            serviceEstadio = new ServiceEstadio();
            armarTablaReporte(); //arma la parte visual
        }



        //METODO -----
        public void armarTablaReporte() {
            // Crear el panel interno
            panelReporte = new JPanel();
            // Ordeno mi pantalla con el BorderLayout que me permite poner norte, sur, este, oeste, centro
            panelReporte.setLayout(new BorderLayout(10, 10));
            panelReporte.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


            //ZONA NORTE. TITULO ----
            JLabel titulo = new JLabel("REPORTE DE ESTADIOS", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 20));
            titulo.setForeground(new Color(163, 188, 226));  // Azul clarito
            titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));  // Margen abajo

            panelReporte.add(titulo, BorderLayout.NORTH);



            //ZONA CENTRO. TABLA ------
            // Crear el modelo de datos (datos que se van a mostrar)
            contenido = new DefaultTableModel();

            // Crear la tabla visual
            jTable = new JTable(contenido);

            // Para deshabilitar que se pueda modificar la tabla desde el frontend
            jTable.setEnabled(true);  // Habilitada para seleccionar
            jTable.setDefaultEditor(Object.class, null);  // Pero no editable

            // Crear el scroll pane
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(jTable);  // Asocio el scroll con la tabla

            // Definir las columnas (títulos que se van a ver arriba de las tablas)
            contenido.addColumn("ID Estadio");
            contenido.addColumn("Nombre");
            contenido.addColumn("Dirección");
            contenido.addColumn("Capacidad Total");

            panelReporte.add(scrollPane, BorderLayout.CENTER);
            // ScrollPane asociado con la tabla y la tabla con el contenido



            //jtable.setEnabled por ejemplo, para deshabilitar que se pueda modificar la tabla desde el frontend
            //dentro de java usamos localdate, las bases de datos usan date. jcalender es como un calendario, hay que descargarlo y agregarlo para usarlo y poner las fechas
            //hora y minuto a mano es mas facil


            //cargar los datos de la base de datos
            cargarEstadios();


            //ZONA SUR. BOTON VOLVER -----
            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

            jButtonVolver = new JButton("Volver al Menú");
            jButtonVolver.setFont(new Font("Arial", Font.BOLD, 14));
            jButtonVolver.setBackground(new Color(220, 220, 220));  // Gris claro
            jButtonVolver.setPreferredSize(new Dimension(180, 40));

            panelBotones.add(jButtonVolver);

            panelReporte.add(panelBotones, BorderLayout.SOUTH);

            //agregar el panel interno a this
            this.setLayout(new BorderLayout());
            this.add(panelReporte, BorderLayout.CENTER);


            //comportamiento del boton
            jButtonVolver.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panelManager.mostrar(1);  // Volver al menú principal
                }
            });
        }


        //METODO. CARGAR ESTADIOS ---------
        private void cargarEstadios() {
            try {
                // Este reporte se genera con el ver reporte (botón que está en la pantallita primera)

                // Consultar todos los estadios (consultarTodos)
                List<Estadio> estadios = serviceEstadio.consultarTodos();

                // Verificar si hay estadios
                if (estadios.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No hay estadios registrados",
                            "Sin estadios",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
            }
                // Armo la consulta de forma que recupere la información
                for (Estadio estadio : estadios) {
                //   tipo   variable   coleccion(array que recorro)

                    // Lo defino de 4 lugares porque tengo 4 columnas
                    Object[] fila = new Object[4];

                    // Cargo los datos
                    fila[0] = estadio.getIdEstadio();
                    fila[1] = estadio.getNombre();
                    fila[2] = estadio.getDireccion();
                    fila[3] = estadio.getCapacidadTotal();

                    // Agregar la fila a la tabla
                    contenido.addRow(fila);
                }

            } catch (ServiceException e) {
                // Si hubo un error al consultar, mostrar mensaje específico
                JOptionPane.showMessageDialog(
                        this,
                        "Error al cargar los estadios: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
}











/*
           clalse con martha
            ServiceEstadio service = new ServiceEstadio(); //esta aca el service porque levanto los datos y listo, si tiene mas utilidades se pone arriba *
            /*try {

                //este reporte se genera con el ver reporte (boton que esta en la pantallita primera que hicimos)
                ArrayList<Estadio> estadios = service.buscarTodos(); //consultarTodos
                for(Estadio estadio:estadios) //armo la consulta de forma que recupere la informacion
                {
                    Object [] fila= new Object[4]; //lo defino de 3 lugares porque tengo 3 columnas
                    fila[0]= estadio.getIdEstadio(); //cargo los datos
                    fila[1]=estadio.getNombre();
                    fila[2]=estadio.getDireccion();
                    fila[3]=estadio.getCapacidadTotal();
                    contenido.addRow(fila);
                }

            }
            catch ( DaoException e)
            {
                JOptionPane.showMessageDialog(null, "Error");
            }
            add(scrollPane, BorderLayout.CENTER); //scrollpane asociado con la tabla y la tabla con el contenido
        }

 */