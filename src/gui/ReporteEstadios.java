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

//pantalla 4 //tabla tipo reporte

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
            setLayout(new BorderLayout()); //ordeno mi pantalla con el border que me permite poner norte, sur, este, oeste, centro
            contenido= new DefaultTableModel(); //datos que se van a mostrar
            jTable = new JTable(contenido);
            scrollPane=new JScrollPane();
            scrollPane.setViewportView(jTable); //asocio el scroll con la tabla
            contenido.addColumn("Id Estadiio");
            contenido.addColumn("Nombre"); //titulos que se van a ver arriba de las tablas
            contenido.addColumn("Direccion");
            contenido.addColumn("Capacidad Total");
            //jtable.setEnabled por ejemplo, para deshabilitar que se pueda modificar la tabla desde el frontend
            //dentro de java usamos localdate, las bases de datos usan date. jcalender es como un calendario, hay que descargarlo y agregarlo para usarlo y poner las fechas
            //hora y minuto a mano es mas facil
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
            }*/
            add(scrollPane, BorderLayout.CENTER); //scrollpane asociado con la tabla y la tabla con el contenido
        }
        /*



         */

    }
