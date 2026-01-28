package gui.venta;

import entidades.*;
import gui.PanelManager;
import service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;


public class ReporteVentas extends JPanel {
    //ATRIBUTOS - SERVICES -----
    private ServiceVenta serviceVenta = new ServiceVenta();
    private ServiceEspectaculo serviceEspectaculo = new ServiceEspectaculo();
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion();
    private ServiceUsuario serviceUsuario = new ServiceUsuario();

    private PanelManager panelManager;
    private JPanel panelReporte;

    private JTable jTable;
    private DefaultTableModel contenido;
    private JScrollPane scrollPane;

    private JButton jButtonVolver;
    private JButton jButtonFiltrar;
    private JComboBox<String> comboEspectaculos;

    private List<Espectaculo> listaEspectaculos;

    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");


    //CONSTRUCTOR -----
    public ReporteVentas(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarReporte();
    }


    private void armarReporte() {
        panelReporte = new JPanel();
        panelReporte.setLayout(new BorderLayout(10, 10));
        panelReporte.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        //ZONA NORTE - TÍTULO Y FILTROS
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("REPORTE DE VENTAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(163, 188, 226));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelNorte.add(titulo, BorderLayout.NORTH);

        //Panel de filtros
        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        JLabel lblFiltro = new JLabel("Filtrar por espectáculo:");
        lblFiltro.setFont(new Font("Arial", Font.PLAIN, 14));

        comboEspectaculos = new JComboBox<>();
        comboEspectaculos.setFont(new Font("Arial", Font.PLAIN, 14));
        comboEspectaculos.setPreferredSize(new Dimension(300, 30));

        jButtonFiltrar = new JButton("Aplicar Filtro");
        jButtonFiltrar.setFont(new Font("Arial", Font.BOLD, 12));
        jButtonFiltrar.setBackground(new Color(33, 150, 243));
        jButtonFiltrar.setForeground(Color.WHITE);

        JButton btnVerTodas = new JButton("Ver Todas");
        btnVerTodas.setFont(new Font("Arial", Font.PLAIN, 12));
        btnVerTodas.setBackground(new Color(220, 220, 220));

        panelFiltros.add(lblFiltro);
        panelFiltros.add(comboEspectaculos);
        panelFiltros.add(jButtonFiltrar);
        panelFiltros.add(btnVerTodas);

        panelNorte.add(panelFiltros, BorderLayout.SOUTH);

        panelReporte.add(panelNorte, BorderLayout.NORTH);


        //ZONA CENTRO - TABLA
        contenido = new DefaultTableModel();
        jTable = new JTable(contenido);
        jTable.setEnabled(true);
        jTable.setDefaultEditor(Object.class, null);

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(jTable);

        definirColumnas();
        panelReporte.add(scrollPane, BorderLayout.CENTER);

        cargarEspectaculosEnCombo();
        cargarDatos(null);


        //ZONA SUR - BOTÓN VOLVER Y TOTALES
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new BorderLayout());

        JPanel panelTotal = new JPanel();
        panelTotal.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        try {
            List<Venta> todasLasVentas = serviceVenta.consultarTodos();
            double total = serviceVenta.calcularTotalRecaudado(todasLasVentas);
            int cantidad = serviceVenta.calcularCantidadVendida(todasLasVentas);

            JLabel labelTotal = new JLabel("Total de ventas: " + cantidad + "  |  Total recaudado: $" + String.format("%.2f", total));
            labelTotal.setFont(new Font("Arial", Font.BOLD, 16));
            labelTotal.setForeground(new Color(76, 175, 80));

            panelTotal.add(labelTotal);

        } catch (ServiceException e) {
            JLabel labelError = new JLabel("Error al calcular totales");
            labelError.setForeground(Color.RED);
            panelTotal.add(labelError);
        }

        panelSur.add(panelTotal, BorderLayout.NORTH);

        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new FlowLayout(FlowLayout.CENTER));

        jButtonVolver = new JButton("Volver al Menú");
        jButtonVolver.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonVolver.setBackground(new Color(220, 220, 220));
        jButtonVolver.setPreferredSize(new Dimension(180, 40));

        panelBoton.add(jButtonVolver);
        panelSur.add(panelBoton, BorderLayout.SOUTH);

        panelReporte.add(panelSur, BorderLayout.SOUTH);


        this.setLayout(new BorderLayout());
        this.add(panelReporte, BorderLayout.CENTER);


        //LISTENERS
        jButtonFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltro();
            }
        });

        btnVerTodas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comboEspectaculos.setSelectedIndex(-1);
                contenido.setRowCount(0);
                cargarDatos(null);
            }
        });

        jButtonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(10); //MenuAdmin
            }
        });
    }




    private void definirColumnas() {
        contenido.addColumn("ID Venta");
        contenido.addColumn("Espectáculo");
        contenido.addColumn("Ubicación");
        contenido.addColumn("Vendedor");
        contenido.addColumn("Cliente");
        contenido.addColumn("DNI");
        contenido.addColumn("Precio");
        contenido.addColumn("Abono");
        contenido.addColumn("Promoción");
        contenido.addColumn("Fecha");
    }


    private void cargarEspectaculosEnCombo() {
        try {
            listaEspectaculos = serviceEspectaculo.consultarTodos();
            comboEspectaculos.addItem("(Seleccione un espectáculo)");

            for(Espectaculo espectaculo : listaEspectaculos) {
                String item = espectaculo.getIdEspectaculo() + " - " + espectaculo.getNombre();
                comboEspectaculos.addItem(item);
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar espectáculos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void aplicarFiltro() {
        if(comboEspectaculos.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un espectáculo", "Filtro no seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int indice = comboEspectaculos.getSelectedIndex() - 1; //restar 1 porque el primer item es el placeholder
        Espectaculo espectaculoSeleccionado = listaEspectaculos.get(indice);

        contenido.setRowCount(0); //limpiar tabla
        cargarDatos(espectaculoSeleccionado.getIdEspectaculo());
    }




    private void cargarDatos(Integer idEspectaculoFiltro) {
        try {
            List<Venta> ventas;

            if(idEspectaculoFiltro == null) {
                //cargar todas las ventas
                ventas = serviceVenta.consultarTodos();
            } else {
                //cargar solo las ventas del espectáculo filtrado
                ventas = serviceVenta.consultarPorEspectaculo(idEspectaculoFiltro);
            }

            if(ventas.isEmpty()) {
                contenido.addRow(new Object[]{
                        "-", "No hay ventas registradas", "-", "-", "-", "-", "-", "-", "-", "-"
                });
                return;
            }

            for(Venta venta : ventas) {
                String nombreEspectaculo = "(Desconocido)";
                try {
                    Espectaculo espectaculo = serviceEspectaculo.consultar(venta.getIdEspectaculo());
                    if(espectaculo != null && espectaculo.getIdEspectaculo() != 0) {
                        nombreEspectaculo = espectaculo.getNombre();
                    }
                } catch (ServiceException e) {}

                String nombreUbicacion = "(Desconocida)";
                try {
                    Ubicacion ubicacion = serviceUbicacion.consultar(venta.getIdUbicacion());
                    if(ubicacion != null && ubicacion.getIdUbicacion() != 0) {
                        nombreUbicacion = ubicacion.getNombre();
                    }
                } catch (ServiceException e) {}

                String nombreVendedor = "(Desconocido)";
                try {
                    Usuario vendedor = serviceUsuario.consultar(venta.getIdVendedor());
                    if(vendedor != null && vendedor.getIdUsuario() != 0) {
                        nombreVendedor = vendedor.getNombre() + " " + vendedor.getApellido();
                    }
                } catch (ServiceException e) {}

                String fecha = formatoFecha.format(venta.getFechaVenta());

                String promocion = venta.getTipoPromocion();
                if (promocion == null || promocion.trim().isEmpty()) {
                    promocion = "Sin promoción";
                }

                String valorAbono;
                if (venta.getValorAbono() == 0) {
                    valorAbono = "-";
                } else {
                    valorAbono = "$" + String.format("%.2f", venta.getValorAbono());
                }

                 Object[] fila = new Object[]{
                        venta.getIdVenta(),              // columna 0: ID Venta
                        nombreEspectaculo,               // columna 1: Espectáculo
                        nombreUbicacion,                 // columna 2: Ubicación
                        nombreVendedor,                  // columna 3: Vendedor
                        venta.getNombreCliente(),        // columna 4: Cliente
                        venta.getDniCliente(),           // columna 5: DNI
                        "$" + venta.getPrecioFinal(),    // columna 6: Precio
                        valorAbono,                      // columna 7: Abono ← NUEVO
                        promocion,                       // columna 8: Promoción
                        fecha                            // columna 9: Fecha
                };

                contenido.addRow(fila);
            }

        } catch (ServiceException e) {
            contenido.addRow(new Object[]{
                    "-", "Error al cargar las ventas: " + e.getMessage(), "-", "-", "-", "-", "-", "-", "-", "-"
            });
        }
    }
}