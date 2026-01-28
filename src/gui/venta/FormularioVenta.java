package gui.venta;

import entidades.*;
import gui.PanelManager;
import service.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import promocion.IPromocion;
import promocion.PromocionHappyHour;
import promocion.SinPromocion;
import java.util.Calendar;


public class FormularioVenta extends JPanel {
    private ServiceVenta serviceVenta = new ServiceVenta();
    private ServiceEspectaculo serviceEspectaculo = new ServiceEspectaculo();
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion();
    private ServiceEstadio serviceEstadio = new ServiceEstadio();

    private PanelManager panelManager;
    private JPanel formulario;

    private JComboBox<String> comboEspectaculos;
    private JComboBox<String> comboUbicaciones;
    private JSpinner spinnerCantidad;  // ← NUEVO - Selector de cantidad de entradas
    private JTextField jTextFieldNombreCliente;
    private JTextField jTextFieldDniCliente;
    private JTextField jTextFieldValorAbono;

    private JLabel jLabelEstadio;
    private JLabel jLabelFechaEspectaculo;
    private JLabel jLabelPrecio;
    private JLabel jLabelCapacidadDisponible;

    private JButton jButtonGuardar;
    private JButton jButtonCancelar;

    private List<Espectaculo> listaEspectaculos;
    private List<Ubicacion> listaUbicaciones;

    private int idVendedor;

    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");


    public FormularioVenta(PanelManager panelManager) {
        this.panelManager = panelManager;
        this.idVendedor = panelManager.getIdUsuarioLogueado();
        armarFormulario();
    }


    private void armarFormulario() {
        formulario = new JPanel();
        formulario.setLayout(new BorderLayout(10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));



        // ZONA NORTE - TÍTULO
        JLabel titulo = new JLabel("REGISTRAR NUEVA VENTA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(76, 175, 80));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formulario.add(titulo, BorderLayout.NORTH);



        // ZONA CENTRO - CAMPOS
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(10, 2, 10, 15));  // ← CAMBIO: 10 filas (agregamos cantidad)
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        //ComboBox ESPECTÁCULOS
        JLabel labelEspectaculo = new JLabel("Espectáculo:");
        labelEspectaculo.setFont(new Font("Arial", Font.PLAIN, 14));
        comboEspectaculos = new JComboBox<>();
        comboEspectaculos.setFont(new Font("Arial", Font.PLAIN, 14));
        panelCampos.add(labelEspectaculo);
        panelCampos.add(comboEspectaculos);

        //Label ESTADIO
        JLabel labelEstadioTexto = new JLabel("Estadio:");
        labelEstadioTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabelEstadio = new JLabel("(Seleccione un espectáculo)");
        jLabelEstadio.setFont(new Font("Arial", Font.ITALIC, 14));
        jLabelEstadio.setForeground(new Color(100, 100, 100));
        panelCampos.add(labelEstadioTexto);
        panelCampos.add(jLabelEstadio);

        //Label FECHA
        JLabel labelFechaTexto = new JLabel("Fecha del espectáculo:");
        labelFechaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabelFechaEspectaculo = new JLabel("(Seleccione un espectáculo)");
        jLabelFechaEspectaculo.setFont(new Font("Arial", Font.ITALIC, 14));
        jLabelFechaEspectaculo.setForeground(new Color(100, 100, 100));
        panelCampos.add(labelFechaTexto);
        panelCampos.add(jLabelFechaEspectaculo);

        //ComboBox UBICACIONES
        JLabel labelUbicacion = new JLabel("Ubicación:");
        labelUbicacion.setFont(new Font("Arial", Font.PLAIN, 14));
        comboUbicaciones = new JComboBox<>();
        comboUbicaciones.setFont(new Font("Arial", Font.PLAIN, 14));
        comboUbicaciones.setEnabled(false);
        panelCampos.add(labelUbicacion);
        panelCampos.add(comboUbicaciones);

        //Label PRECIO UNITARIO
        JLabel labelPrecioTexto = new JLabel("Precio unitario:");
        labelPrecioTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabelPrecio = new JLabel("$0.00");
        jLabelPrecio.setFont(new Font("Arial", Font.BOLD, 14));
        jLabelPrecio.setForeground(new Color(76, 175, 80));
        panelCampos.add(labelPrecioTexto);
        panelCampos.add(jLabelPrecio);


        //CANTIDAD DE ENTRADAS
        JLabel labelCantidad = new JLabel("Cantidad de entradas:");
        labelCantidad.setFont(new Font("Arial", Font.PLAIN, 14));

        SpinnerNumberModel modeloCantidad = new SpinnerNumberModel(
                1,    // Valor inicial
                1,    // Valor mínimo
                5,    // Valor máximo
                1     // Incremento
        );
        spinnerCantidad = new JSpinner(modeloCantidad);
        spinnerCantidad.setFont(new Font("Arial", Font.BOLD, 16));

        // Hacer que el spinner sea más grande y fácil de usar
        JComponent editor = spinnerCantidad.getEditor();
        JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
        spinnerEditor.getTextField().setColumns(5);
        spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);

        panelCampos.add(labelCantidad);
        panelCampos.add(spinnerCantidad);

        //Label CAPACIDAD DISPONIBLE
        JLabel labelCapacidadTexto = new JLabel("Entradas disponibles:");
        labelCapacidadTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabelCapacidadDisponible = new JLabel("0");
        jLabelCapacidadDisponible.setFont(new Font("Arial", Font.BOLD, 14));
        jLabelCapacidadDisponible.setForeground(new Color(33, 150, 243));
        panelCampos.add(labelCapacidadTexto);
        panelCampos.add(jLabelCapacidadDisponible);

        //TextField NOMBRE CLIENTE
        JLabel labelNombreCliente = new JLabel("Nombre del cliente:");
        labelNombreCliente.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldNombreCliente = new JTextField(20);
        panelCampos.add(labelNombreCliente);
        panelCampos.add(jTextFieldNombreCliente);

        //TextField DNI CLIENTE
        JLabel labelDniCliente = new JLabel("DNI del cliente:");
        labelDniCliente.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldDniCliente = new JTextField(20);
        panelCampos.add(labelDniCliente);
        panelCampos.add(jTextFieldDniCliente);

        //TextField VALOR DEL ABONO
        JLabel labelValorAbono = new JLabel("Valor del abono (0 si no tiene):");
        labelValorAbono.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldValorAbono = new JTextField(20);
        jTextFieldValorAbono.setText("0");
        jTextFieldValorAbono.setToolTipText("Ingrese el valor del abono del cliente. Si no tiene abono, dejar en 0");
        panelCampos.add(labelValorAbono);
        panelCampos.add(jTextFieldValorAbono);

        formulario.add(panelCampos, BorderLayout.CENTER);



        // ZONA SUR - BOTONES
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        jButtonGuardar = new JButton("Registrar Venta");
        jButtonGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonGuardar.setBackground(new Color(76, 175, 80));
        jButtonGuardar.setForeground(Color.WHITE);
        jButtonGuardar.setPreferredSize(new Dimension(180, 40));

        jButtonCancelar = new JButton("Cancelar");
        jButtonCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonCancelar.setBackground(new Color(220, 220, 220));
        jButtonCancelar.setPreferredSize(new Dimension(150, 40));

        panelBotones.add(jButtonGuardar);
        panelBotones.add(jButtonCancelar);
        formulario.add(panelBotones, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        this.add(formulario, BorderLayout.CENTER);



        // CARGAR DATOS INICIALES
        cargarEspectaculosEnCombo();



        // LISTENERS
        comboEspectaculos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarInfoEspectaculo();
            }
        });

        comboUbicaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarInfoUbicacion();
            }
        });

        jButtonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarVenta();
            }
        });

        jButtonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(20);
            }
        });
    }


    private void cargarEspectaculosEnCombo() {
        try {
            listaEspectaculos = serviceEspectaculo.consultarActivos();

            if(listaEspectaculos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay espectáculos activos disponibles para venta", "Sin espectáculos", JOptionPane.INFORMATION_MESSAGE);
                panelManager.mostrar(20);
                return;
            }

            for(Espectaculo espectaculo : listaEspectaculos) {
                String fecha = formatoFecha.format(espectaculo.getFecha());
                String item = espectaculo.getIdEspectaculo() + " - " + espectaculo.getNombre() + " (Fecha: " + fecha + ")";
                comboEspectaculos.addItem(item);
            }

            if(listaEspectaculos.size() > 0) {
                comboEspectaculos.setSelectedIndex(0);
                actualizarInfoEspectaculo();
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar espectáculos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            panelManager.mostrar(20);
        }
    }


    private void actualizarInfoEspectaculo() {
        if(comboEspectaculos.getSelectedIndex() == -1) {
            return;
        }

        try {
            int indiceSeleccionado = comboEspectaculos.getSelectedIndex();
            Espectaculo espectaculoSeleccionado = listaEspectaculos.get(indiceSeleccionado);

            Estadio estadio = serviceEstadio.consultar(espectaculoSeleccionado.getIdEstadio());
            jLabelEstadio.setText(estadio.getNombre());
            jLabelEstadio.setForeground(Color.BLACK);
            jLabelEstadio.setFont(new Font("Arial", Font.PLAIN, 14));

            String fecha = formatoFecha.format(espectaculoSeleccionado.getFecha());
            jLabelFechaEspectaculo.setText(fecha);
            jLabelFechaEspectaculo.setForeground(Color.BLACK);
            jLabelFechaEspectaculo.setFont(new Font("Arial", Font.PLAIN, 14));

            cargarUbicacionesEnCombo(espectaculoSeleccionado.getIdEstadio());

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar información del espectáculo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void cargarUbicacionesEnCombo(int idEstadio) {
        try {
            comboUbicaciones.removeAllItems();
            listaUbicaciones = serviceUbicacion.consultarPorEstadio(idEstadio);

            if(listaUbicaciones.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El estadio no tiene ubicaciones configuradas", "Sin ubicaciones", JOptionPane.WARNING_MESSAGE);
                comboUbicaciones.setEnabled(false);
                return;
            }

            for(Ubicacion ubicacion : listaUbicaciones) {
                String item = ubicacion.getIdUbicacion() + " - " + ubicacion.getNombre();
                comboUbicaciones.addItem(item);
            }

            comboUbicaciones.setEnabled(true);

            if(listaUbicaciones.size() > 0) {
                comboUbicaciones.setSelectedIndex(0);
                actualizarInfoUbicacion();
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar ubicaciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void actualizarInfoUbicacion() {
        if(comboUbicaciones.getSelectedIndex() == -1) {
            return;
        }

        try {
            int indiceEspectaculo = comboEspectaculos.getSelectedIndex();
            Espectaculo espectaculoSeleccionado = listaEspectaculos.get(indiceEspectaculo);

            int indiceUbicacion = comboUbicaciones.getSelectedIndex();
            Ubicacion ubicacionSeleccionada = listaUbicaciones.get(indiceUbicacion);

            jLabelPrecio.setText("$" + ubicacionSeleccionada.getPrecio());

            int capacidadDisponible = serviceVenta.verificarCapacidadDisponible(
                    espectaculoSeleccionado.getIdEspectaculo(),
                    ubicacionSeleccionada.getIdUbicacion()
            );
            jLabelCapacidadDisponible.setText(String.valueOf(capacidadDisponible));

            if(capacidadDisponible == 0) {
                jLabelCapacidadDisponible.setForeground(Color.RED);
            } else if(capacidadDisponible < 10) {
                jLabelCapacidadDisponible.setForeground(new Color(255, 152, 0));
            } else {
                jLabelCapacidadDisponible.setForeground(new Color(76, 175, 80));
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Error al calcular capacidad disponible: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void registrarVenta() {

        if(comboEspectaculos.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un espectáculo", "Espectáculo no seleccionado", JOptionPane.WARNING_MESSAGE);
            comboEspectaculos.requestFocus();
            return;
        }

        if(comboUbicaciones.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una ubicación", "Ubicación no seleccionada", JOptionPane.WARNING_MESSAGE);
            comboUbicaciones.requestFocus();
            return;
        }

        String nombreCliente = jTextFieldNombreCliente.getText();
        if(nombreCliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente es obligatorio", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            jTextFieldNombreCliente.requestFocus();
            return;
        }

        String dniCliente = jTextFieldDniCliente.getText();
        if(dniCliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El DNI del cliente es obligatorio", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            jTextFieldDniCliente.requestFocus();
            return;
        }

        int indiceEspectaculo = comboEspectaculos.getSelectedIndex();
        Espectaculo espectaculoSeleccionado = listaEspectaculos.get(indiceEspectaculo);

        int indiceUbicacion = comboUbicaciones.getSelectedIndex();
        Ubicacion ubicacionSeleccionada = listaUbicaciones.get(indiceUbicacion);


        int cantidad = (Integer) spinnerCantidad.getValue();

        try {
            int capacidadDisponible = serviceVenta.verificarCapacidadDisponible(
                    espectaculoSeleccionado.getIdEspectaculo(),
                    ubicacionSeleccionada.getIdUbicacion()
            );

            if (cantidad > capacidadDisponible) {
                JOptionPane.showMessageDialog(this,
                        "No hay capacidad suficiente en esta ubicación.\n\n" +
                                "Cantidad solicitada: " + cantidad + " entrada(s)\n" +
                                "Capacidad disponible: " + capacidadDisponible + " entrada(s)\n\n" +
                                "Por favor, seleccione una cantidad menor o elija otra ubicación.",
                        "Error - Capacidad insuficiente",
                        JOptionPane.ERROR_MESSAGE);
                spinnerCantidad.requestFocus();
                return;
            }
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al verificar capacidad: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        String valorAbonoTexto = jTextFieldValorAbono.getText().trim();
        double valorAbono = 0.0;

        try {
            valorAbono = Double.parseDouble(valorAbonoTexto);

            if (valorAbono < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "El valor del abono no puede ser negativo",
                        "Valor inválido",
                        JOptionPane.WARNING_MESSAGE
                );
                jTextFieldValorAbono.requestFocus();
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "El valor del abono debe ser un número válido\nEjemplos: 0, 500, 1000.50",
                    "Formato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );
            jTextFieldValorAbono.requestFocus();
            return;
        }


        // CALCULAR PRECIO BASE (unitario × cantidad)
        double precioUnitario = ubicacionSeleccionada.getPrecio();
        double precioBase = precioUnitario * cantidad;  // ← NUEVO: multiplica por cantidad


        // APLICAR ABONO AL PRECIO TOTAL
        Abono abono = new Abono(valorAbono);
        double precioConAbono = abono.calcularPrecioFinal(precioBase);


        // DETERMINAR QUÉ PROMOCIÓN APLICAR
        Calendar ahora = Calendar.getInstance();
        int horaActual = ahora.get(Calendar.HOUR_OF_DAY);

        IPromocion promocion;

        if (horaActual >= 5 && horaActual < 6) {
            promocion = new PromocionHappyHour();
            JOptionPane.showMessageDialog(
                    this,
                    "¡HAPPY HOUR ACTIVO!\n20% de descuento aplicado automáticamente",
                    "Promoción aplicada",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            promocion = new SinPromocion();
        }


        // APLICAR LA PROMOCIÓN AL PRECIO CON ABONO
        double precioFinal = promocion.aplicarDescuento(precioConAbono);


        // CREAR EL OBJETO VENTA CON TODOS LOS DATOS
        Venta venta = new Venta();
        venta.setIdEspectaculo(espectaculoSeleccionado.getIdEspectaculo());
        venta.setIdUbicacion(ubicacionSeleccionada.getIdUbicacion());
        venta.setIdVendedor(idVendedor);
        venta.setFechaVenta(new Timestamp(System.currentTimeMillis()));
        venta.setPrecioFinal(precioFinal);
        venta.setNombreCliente(nombreCliente);
        venta.setDniCliente(dniCliente);
        venta.setTipoPromocion(promocion.getNombre());
        venta.setValorAbono(valorAbono);
        venta.setCantidad(cantidad);


        // INSERTAR LA VENTA EN LA BASE DE DATOS
        try {
            serviceVenta.insertar(venta);
            String mensaje = "Venta registrada correctamente\n\n" +
                    "Cliente: " + nombreCliente + "\n" +
                    "Espectáculo: " + espectaculoSeleccionado.getNombre() + "\n" +
                    "Ubicación: " + ubicacionSeleccionada.getNombre() + "\n" +
                    "Cantidad: " + cantidad + " entrada(s)\n" +  // ← NUEVO
                    "Precio unitario: $" + String.format("%.2f", precioUnitario) + "\n" +
                    "Precio total: $" + String.format("%.2f", precioBase) + "\n";

            if (valorAbono > 0) {
                mensaje += "Abono: -$" + String.format("%.2f", valorAbono) + "\n";
                mensaje += "Precio después del abono: $" + String.format("%.2f", precioConAbono) + "\n";
            }

            mensaje += "Promoción: " + promocion.getNombre() + "\n" +
                    "Precio final: $" + String.format("%.2f", precioFinal);

            double ahorroTotal = precioBase - precioFinal;
            if (ahorroTotal > 0) {
                mensaje += "\n\n¡Ahorro total: $" + String.format("%.2f", ahorroTotal) + "!";
            }

            JOptionPane.showMessageDialog(
                    this,
                    mensaje,
                    "Venta exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            //Limpiar campos
            jTextFieldNombreCliente.setText("");
            jTextFieldDniCliente.setText("");
            jTextFieldValorAbono.setText("0");
            spinnerCantidad.setValue(1);  // ← NUEVO: resetear a 1
            jTextFieldNombreCliente.requestFocus();
            actualizarInfoUbicacion();

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar la venta:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}