package gui.base;

import gui.PanelManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public abstract class ReporteBase extends JPanel {
    //ATRIBUTOS COMUNES (PRIVATE) -----
    private PanelManager panelManager; //para cambiar de pantalla
    private JPanel panelReporte; //panel interno que contiene todo

    //componentes para la tabla
    private JTable jTable; //el componente visual de la tabla
    private DefaultTableModel contenido; //el modelo de datos de la tabla
    private JScrollPane scrollPane; //scroll para la tabla

    //botón
    private JButton jButtonVolver; //botón para volver al menú


    //CONSTRUCTOR -----
    public ReporteBase(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarTablaReporte();
    }


    //METODO ARMAR TABLA REPORTE -----
    private void armarTablaReporte() {
        //crear el panel interno
        panelReporte = new JPanel();
        panelReporte.setLayout(new BorderLayout(10, 10));
        panelReporte.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        //ZONA NORTE - TÍTULO -----
        String tituloTexto = getTitulo(); //llama al método abstracto de la clase hija

        JLabel titulo = new JLabel(tituloTexto, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(163, 188, 226)); //azul clarito
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); //margen abajo

        panelReporte.add(titulo, BorderLayout.NORTH);


        //ZONA CENTRO - TABLA -----
        //crear el modelo de datos (vacío por ahora)
        contenido = new DefaultTableModel();

        //crear la tabla visual
        jTable = new JTable(contenido);

        //deshabilitar edición desde la tabla
        jTable.setEnabled(true); //habilitada para seleccionar
        jTable.setDefaultEditor(Object.class, null); //pero no editable

        //crear el scroll pane
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(jTable); //asociar el scroll con la tabla


        panelReporte.add(scrollPane, BorderLayout.CENTER);

        //ZONA SUR - BOTÓN VOLVER -----
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));

        jButtonVolver = new JButton("Volver al Menú");
        jButtonVolver.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonVolver.setBackground(new Color(220, 220, 220)); //gris claro
        jButtonVolver.setPreferredSize(new Dimension(180, 40));

        panelBotones.add(jButtonVolver);

        panelReporte.add(panelBotones, BorderLayout.SOUTH);


        //agregar el panel interno a this
        this.setLayout(new BorderLayout());
        this.add(panelReporte, BorderLayout.CENTER);


        //COMPORTAMIENTO DEL BOTÓN -----
        jButtonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codigoMenu = getCodigoMenuPrincipal();
                panelManager.mostrar(codigoMenu); //vuelve al menú principal
            }
        });
    }


    private void definirColumnas() {
        String[] columnas = obtenerNombresColumnas(); //llama al método abstracto

        //agregar cada columna al modelo
        for (String nombreColumna : columnas) {
            contenido.addColumn(nombreColumna);
        }
    }


    //METODO CARGAR DATOS -----
    //Consulta la BD y llena la tabla
    private void cargarDatos() {
        try {
            //consultar todos los elementos (llama al método abstracto)
            List<?> elementos = consultarTodos();

            //verificar si hay elementos
            if (elementos.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        getMensajeSinElementos(), //llama al método abstracto
                        "Sin elementos",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            //recorrer cada elemento y agregarlo a la tabla
            for (Object elemento : elementos) {
                //obtener la fila de datos (llama al método abstracto)
                Object[] fila = convertirElementoAFila(elemento);

                //agregar la fila a la tabla
                contenido.addRow(fila);
            }

        } catch (Exception e) {
            //si hay error al consultar, mostrar mensaje
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los elementos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    //GETTERS Y SETTERS -----

    public PanelManager getPanelManager() {
        return panelManager;
    }

    public JPanel getPanelReporte() {
        return panelReporte;
    }

    public JTable getTable() {
        return jTable;
    }

    public DefaultTableModel getContenido() {
        return contenido;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JButton getButtonVolver() {
        return jButtonVolver;
    }


    //METODO INICIALIZAR -----
//Este método se llama DESPUÉS de que la clase hija termine de construirse
//De esta forma, los Services ya están inicializados y funcionan correctamente
    public void inicializar() {
        definirColumnas();
        cargarDatos();
    }



    //MÉTODOS ABSTRACTOS (PUBLIC) -----
    //Las clases hijas DEBEN implementar estos métodos

    public abstract String getTitulo();

    public abstract String[] obtenerNombresColumnas();

    public abstract List<?> consultarTodos() throws Exception;

    public abstract Object[] convertirElementoAFila(Object elemento);

    public abstract String getMensajeSinElementos();

    public abstract int getCodigoMenuPrincipal();
}