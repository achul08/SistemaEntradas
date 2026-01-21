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
import promocion.IPromocion; //para usar la interfaz
import promocion.PromocionHappyHour; //promoción de 5am-6am
import promocion.SinPromocion; //sin descuento
import java.util.Calendar; //para obtener la hora actual



public class FormularioVenta extends JPanel {
    private ServiceVenta serviceVenta = new ServiceVenta();
    private ServiceEspectaculo serviceEspectaculo = new ServiceEspectaculo();
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion();
    private ServiceEstadio serviceEstadio = new ServiceEstadio();

    private PanelManager panelManager;
    private JPanel formulario;

    private JComboBox<String> comboEspectaculos;
    private JComboBox<String> comboUbicaciones;
    private JTextField jTextFieldNombreCliente;
    private JTextField jTextFieldDniCliente;
    private JTextField jTextFieldValorAbono; //NUEVO - para ingresar el valor del abono

    private JLabel jLabelEstadio;
    private JLabel jLabelFechaEspectaculo;
    private JLabel jLabelPrecio;
    private JLabel jLabelCapacidadDisponible;

    private JButton jButtonGuardar;
    private JButton jButtonCancelar;

    private List<Espectaculo> listaEspectaculos;
    private List<Ubicacion> listaUbicaciones;

    private int idVendedor; //ID del vendedor que hace la venta

    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");


    public FormularioVenta(PanelManager panelManager) {
        this.panelManager = panelManager;
        //en lugar de ID fijo, obtenemos el ID del usuario logueado
        this.idVendedor = panelManager.getIdUsuarioLogueado();
        armarFormulario();
    }


    private void armarFormulario() {
        formulario = new JPanel();
        formulario.setLayout(new BorderLayout(10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        //ZONA NORTE - TÍTULO
        JLabel titulo = new JLabel("REGISTRAR NUEVA VENTA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(76, 175, 80));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        formulario.add(titulo, BorderLayout.NORTH);


        //ZONA CENTRO - CAMPOS
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(9, 2, 10, 15)); //CAMBIO: de 8 a 9 filas
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

        //Label PRECIO
        JLabel labelPrecioTexto = new JLabel("Precio:");
        labelPrecioTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        jLabelPrecio = new JLabel("$0.00");
        jLabelPrecio.setFont(new Font("Arial", Font.BOLD, 14));
        jLabelPrecio.setForeground(new Color(76, 175, 80));
        panelCampos.add(labelPrecioTexto);
        panelCampos.add(jLabelPrecio);

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

        //TextField VALOR DEL ABONO - NUEVO
        JLabel labelValorAbono = new JLabel("Valor del abono (0 si no tiene):");
        labelValorAbono.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldValorAbono = new JTextField(20);
        jTextFieldValorAbono.setText("0"); //valor por defecto = 0
        jTextFieldValorAbono.setToolTipText("Ingrese el valor del abono del cliente. Si no tiene abono, dejar en 0");
        panelCampos.add(labelValorAbono);
        panelCampos.add(jTextFieldValorAbono);

        formulario.add(panelCampos, BorderLayout.CENTER);


        //ZONA SUR - BOTONES
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


        //CARGAR DATOS INICIALES
        cargarEspectaculosEnCombo();


        //LISTENERS
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

            int capacidadDisponible = serviceVenta.verificarCapacidadDisponible(espectaculoSeleccionado.getIdEspectaculo(), ubicacionSeleccionada.getIdUbicacion());
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

        //═════════════════════════════════════════════════════════════════════
        // PASO 1: LEER Y VALIDAR EL VALOR DEL ABONO
        //═════════════════════════════════════════════════════════════════════

        String valorAbonoTexto = jTextFieldValorAbono.getText().trim();
        double valorAbono = 0.0;

        //Intentar convertir el texto a número
        try {
            valorAbono = Double.parseDouble(valorAbonoTexto);

            //Validar que el valor del abono no sea negativo
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
            //Si no es un número válido, mostrar error
            JOptionPane.showMessageDialog(
                    this,
                    "El valor del abono debe ser un número válido\nEjemplos: 0, 500, 1000.50",
                    "Formato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );
            jTextFieldValorAbono.requestFocus();
            return;
        }


        //═════════════════════════════════════════════════════════════════════
        // PASO 2: CREAR OBJETO ABONO Y CALCULAR PRECIO DESPUÉS DEL ABONO
        //═════════════════════════════════════════════════════════════════════

        //Obtener el precio base de la ubicación (sin ningún descuento)
        double precioBase = ubicacionSeleccionada.getPrecio();

        //Crear el objeto Abono con el valor ingresado
        Abono abono = new Abono(valorAbono);

        //Calcular el precio después de aplicar el abono
        //Si precioBase = 1000 y valorAbono = 300 → precioConAbono = 700
        double precioConAbono = abono.calcularPrecioFinal(precioBase);


        //═════════════════════════════════════════════════════════════════════
        // PASO 3: DETERMINAR QUÉ PROMOCIÓN APLICAR
        //═════════════════════════════════════════════════════════════════════

        //Obtener la hora actual del sistema
        Calendar ahora = Calendar.getInstance();
        int horaActual = ahora.get(Calendar.HOUR_OF_DAY); //0-23

        //Variable para guardar la promoción a usar (interfaz IPromocion)
        IPromocion promocion;

        //Verificar si estamos en Happy Hour (5am-6am)
        if (horaActual >= 5 && horaActual < 6) {
            //ESTAMOS EN HAPPY HOUR → usar PromocionHappyHour
            promocion = new PromocionHappyHour();

            //Mostrar mensaje al vendedor informando del descuento
            JOptionPane.showMessageDialog(
                    this,
                    "¡HAPPY HOUR ACTIVO!\n20% de descuento aplicado automáticamente",
                    "Promoción aplicada",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            //NO ES HAPPY HOUR → usar SinPromocion (precio normal)
            promocion = new SinPromocion();
        }


        //═════════════════════════════════════════════════════════════════════
        // PASO 4: APLICAR LA PROMOCIÓN AL PRECIO CON ABONO
        //═════════════════════════════════════════════════════════════════════

        //IMPORTANTE: La promoción se aplica DESPUÉS del abono
        //Ejemplo:
        // - Precio base: $1000
        // - Cliente tiene abono de $300 → queda $700
        // - Happy Hour 20% descuento → $700 - 20% = $560
        // - Precio final: $560

        double precioFinal = promocion.aplicarDescuento(precioConAbono);


        //═════════════════════════════════════════════════════════════════════
        // PASO 5: CREAR EL OBJETO VENTA CON TODOS LOS DATOS
        //═════════════════════════════════════════════════════════════════════

        Venta venta = new Venta();
        venta.setIdEspectaculo(espectaculoSeleccionado.getIdEspectaculo());
        venta.setIdUbicacion(ubicacionSeleccionada.getIdUbicacion());
        venta.setIdVendedor(idVendedor);
        venta.setFechaVenta(new Timestamp(System.currentTimeMillis()));
        venta.setPrecioFinal(precioFinal);
        venta.setNombreCliente(nombreCliente);
        venta.setDniCliente(dniCliente);
        venta.setTipoPromocion(promocion.getNombre());
        venta.setValorAbono(valorAbono); //NUEVO - guardar el valor del abono

        try {
            serviceVenta.insertar(venta);

            //Construir el mensaje de confirmación
            String mensaje = "Venta registrada correctamente\n\n" +
                    "Cliente: " + nombreCliente + "\n" +
                    "Espectáculo: " + espectaculoSeleccionado.getNombre() + "\n" +
                    "Ubicación: " + ubicacionSeleccionada.getNombre() + "\n" +
                    "Precio original: $" + String.format("%.2f", precioBase) + "\n";

            //Si tiene abono, mostrar el detalle
            if (valorAbono > 0) {
                mensaje += "Abono: -$" + String.format("%.2f", valorAbono) + "\n";
                mensaje += "Precio después del abono: $" + String.format("%.2f", precioConAbono) + "\n";
            }

            mensaje += "Promoción: " + promocion.getNombre() + "\n" +
                    "Precio final: $" + String.format("%.2f", precioFinal);

            //Calcular ahorro total
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

            jTextFieldNombreCliente.setText("");
            jTextFieldDniCliente.setText("");
            jTextFieldValorAbono.setText("0"); //NUEVO - limpiar el campo de abono
            jTextFieldNombreCliente.requestFocus();
            actualizarInfoUbicacion();

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}