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

//REPORTE VENTAS VENDEDOR - Muestra las ventas de un vendedor específico
//Cada vendedor ve SOLO sus propias ventas

//RELACIÓN CON OTRAS CLASES:
//- ServiceVenta: para obtener las ventas del vendedor
//- ServiceEspectaculo: para mostrar el nombre del espectáculo
//- ServiceUbicacion: para mostrar el nombre de la ubicación
//- PanelManager: para cambiar de pantalla


public class ReporteVentasVendedor extends JPanel {
    //ATRIBUTOS - SERVICES -----
    private ServiceVenta serviceVenta = new ServiceVenta();
    private ServiceEspectaculo serviceEspectaculo = new ServiceEspectaculo();
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion();

    //ATRIBUTOS - GUI -----
    private PanelManager panelManager;
    private JPanel panelReporte;

    //componentes para la tabla
    private JTable jTable;
    private DefaultTableModel contenido;
    private JScrollPane scrollPane;

    //botón
    private JButton jButtonVolver;

    //ATRIBUTO - ID DEL VENDEDOR -----
    //Por ahora usamos ID fijo, después lo pasamos por parámetro
    private int idVendedor;

    //ATRIBUTO - FORMATO DE FECHA -----
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public ReporteVentasVendedor(PanelManager panelManager) {
        this.panelManager = panelManager;
        //en lugar de ID fijo, obtenemos el ID del usuario logueado
        this.idVendedor = panelManager.getIdUsuarioLogueado();
        armarReporte();
    }


    //METODO ARMAR REPORTE -----
    private void armarReporte() {
        //crear el panel interno
        panelReporte = new JPanel();
        panelReporte.setLayout(new BorderLayout(10, 10));
        panelReporte.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        //═════════════════════════════════════════════════════════════════════
        // ZONA NORTE - TÍTULO
        //═════════════════════════════════════════════════════════════════════
        JLabel titulo = new JLabel("MIS VENTAS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(33, 150, 243)); //azul
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        panelReporte.add(titulo, BorderLayout.NORTH);


        //═════════════════════════════════════════════════════════════════════
        // ZONA CENTRO - TABLA
        //═════════════════════════════════════════════════════════════════════

        //crear el modelo de datos (vacío por ahora)
        contenido = new DefaultTableModel();

        //crear la tabla visual
        jTable = new JTable(contenido);

        //deshabilitar edición desde la tabla
        jTable.setEnabled(true); //habilitada para seleccionar
        jTable.setDefaultEditor(Object.class, null); //pero no editable

        //crear el scroll pane
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(jTable);

        //definir las columnas
        definirColumnas();

        panelReporte.add(scrollPane, BorderLayout.CENTER);


        //cargar los datos en la tabla
        cargarDatos();


        //═════════════════════════════════════════════════════════════════════
        // ZONA SUR - BOTÓN VOLVER Y TOTAL
        //═════════════════════════════════════════════════════════════════════
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new BorderLayout());

        //Panel para el total recaudado
        JPanel panelTotal = new JPanel();
        panelTotal.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));

        try {
            //calcular el total recaudado
            List<Venta> misVentas = serviceVenta.consultarPorVendedor(idVendedor);
            double total = serviceVenta.calcularTotalRecaudado(misVentas);
            int cantidad = serviceVenta.calcularCantidadVendida(misVentas);

            JLabel labelTotal = new JLabel("Total de ventas: " + cantidad + "  |  Total recaudado: $" + String.format("%.2f", total));
            labelTotal.setFont(new Font("Arial", Font.BOLD, 16));
            labelTotal.setForeground(new Color(76, 175, 80)); //verde

            panelTotal.add(labelTotal);

        } catch (ServiceException e) {
            JLabel labelError = new JLabel("Error al calcular totales");
            labelError.setForeground(Color.RED);
            panelTotal.add(labelError);
        }

        panelSur.add(panelTotal, BorderLayout.NORTH);

        //Panel para el botón
        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new FlowLayout(FlowLayout.CENTER));

        jButtonVolver = new JButton("Volver al Menú");
        jButtonVolver.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonVolver.setBackground(new Color(220, 220, 220)); //gris claro
        jButtonVolver.setPreferredSize(new Dimension(180, 40));

        panelBoton.add(jButtonVolver);

        panelSur.add(panelBoton, BorderLayout.SOUTH);

        panelReporte.add(panelSur, BorderLayout.SOUTH);


        //agregar el panel interno a this
        this.setLayout(new BorderLayout());
        this.add(panelReporte, BorderLayout.CENTER);


        //═════════════════════════════════════════════════════════════════════
        // COMPORTAMIENTO DEL BOTÓN
        //═════════════════════════════════════════════════════════════════════
        jButtonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(20); //código 20 = MenuVendedor
            }
        });
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO: DEFINIR COLUMNAS
    //═════════════════════════════════════════════════════════════════════
    //Define los nombres de las columnas de la tabla
    private void definirColumnas() {
        contenido.addColumn("ID Venta");
        contenido.addColumn("Espectáculo");
        contenido.addColumn("Ubicación");
        contenido.addColumn("Cliente");
        contenido.addColumn("DNI");
        contenido.addColumn("Precio");
        contenido.addColumn("Promoción"); //NUEVO - columna para mostrar la promoción aplicada
        contenido.addColumn("Fecha");
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO: CARGAR DATOS
    //═════════════════════════════════════════════════════════════════════
    //Consulta las ventas del vendedor y las muestra en la tabla
    private void cargarDatos() {
        try {
            //consultar las ventas del vendedor
            //Este método usa DaoVenta.consultarPorVendedor() que ya tenés implementado
            List<Venta> ventas = serviceVenta.consultarPorVendedor(idVendedor);

            //verificar si tiene ventas
            if(ventas.isEmpty()) {
                //si no tiene ventas, agregar una fila informativa
                contenido.addRow(new Object[]{
                        "-", "No hay ventas registradas", "-", "-", "-", "-", "-"
                });
                return;
            }

            //recorrer cada venta y agregarla a la tabla
            for(Venta venta : ventas) {
                //obtener el nombre del espectáculo
                String nombreEspectaculo = "(Desconocido)";
                try {
                    Espectaculo espectaculo = serviceEspectaculo.consultar(venta.getIdEspectaculo());
                    if(espectaculo != null && espectaculo.getIdEspectaculo() != 0) {
                        nombreEspectaculo = espectaculo.getNombre();
                    }
                } catch (ServiceException e) {
                    //si no se puede obtener, dejar el valor por defecto
                }

                //obtener el nombre de la ubicación
                String nombreUbicacion = "(Desconocida)";
                try {
                    Ubicacion ubicacion = serviceUbicacion.consultar(venta.getIdUbicacion());
                    if(ubicacion != null && ubicacion.getIdUbicacion() != 0) {
                        nombreUbicacion = ubicacion.getNombre();
                    }
                } catch (ServiceException e) {
                    //si no se puede obtener, dejar el valor por defecto
                }

                //formatear la fecha
                String fecha = formatoFecha.format(venta.getFechaVenta());
                //Obtener la promoción aplicada
                //Si es null (ventas viejas), mostrar "Sin promoción"
                String promocion = venta.getTipoPromocion();
                if (promocion == null || promocion.trim().isEmpty()) {
                    promocion = "Sin promoción";
                }

                Object[] fila = new Object[]{
                        venta.getIdVenta(),
                        nombreEspectaculo,
                        nombreUbicacion,
                        venta.getNombreCliente(),
                        venta.getDniCliente(),
                        "$" + venta.getPrecioFinal(),
                        promocion, //NUEVO - mostrar la promoción
                        fecha
                };

                //agregar la fila a la tabla
                contenido.addRow(fila);
            }

        } catch (ServiceException e) {
            //si hay error al consultar, mostrar mensaje en la tabla
            contenido.addRow(new Object[]{
                    "-", "Error al cargar las ventas: " + e.getMessage(), "-", "-", "-", "-", "-"
            });
        }
    }
}