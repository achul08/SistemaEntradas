package gui.espectaculo;

import entidades.Espectaculo;
import entidades.Estadio;
import gui.PanelManager;
import gui.base.FormularioBase;
import service.ServiceEspectaculo;
import service.ServiceEstadio;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class FormularioEspectaculo extends FormularioBase {
    //ATRIBUTOS ESPECÍFICOS DE ESPECTACULO -----
    private ServiceEspectaculo serviceEspectaculo = new ServiceEspectaculo(); //para comunicarse con la BD de espectáculos
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para obtener la lista de estadios

    //campos específicos de espectaculo
    private JComboBox<String> comboEstadios; //ComboBox para seleccionar el estadio
    private JTextField jTextFieldNombre;
    private JTextField jTextFieldFecha; //formato: dd/MM/yyyy
    private JCheckBox jCheckBoxActivo; //true = activo, false = inactivo

    //labels específicos de espectaculo
    private JLabel jLabelIdEspectaculo; //solo se muestra en modo modificar
    private JLabel jLabelEstadio;
    private JLabel jLabelNombre;
    private JLabel jLabelFecha;
    private JLabel jLabelActivo;

    //lista de estadios (para saber qué estadio seleccionó el usuario)
    private List<Estadio> listaEstadios;

    //formato de fecha
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    //Convierte String → Date (cuando el usuario escribe la fecha)
    //Convierte Date → String (cuando mostramos la fecha en el TextField)

    //CONSTRUCTOR PARA CREAR -----
    public FormularioEspectaculo(PanelManager panel) {
        super(panel); //llama al constructor de FormularioBase para modo crear
        formatoFecha.setLenient(false); //no permitir fechas inválidas como 32/13/2025
        cargarEstadiosEnCombo();
    }


    //CONSTRUCTOR PARA MODIFICAR -----
    public FormularioEspectaculo(PanelManager panel, Espectaculo espectaculo) {
        super(panel, espectaculo.getIdEspectaculo()); //llama al constructor de FormularioBase para modo modificar
        formatoFecha.setLenient(false);
        cargarEstadiosEnCombo();
        cargarDatos(espectaculo); //llenar los campos con los datos del espectaculo
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----
    @Override
    public String getTitulo() {
        if (isModoModificar()) {
            return "MODIFICAR ESPECTÁCULO";
        } else {
            return "CREAR NUEVO ESPECTÁCULO";
        }
    }

    @Override
    public JPanel crearPanelCampos() {
        JPanel panelCampos = new JPanel();

        if (isModoModificar()) {
            panelCampos.setLayout(new GridLayout(5, 2, 10, 15));
        } else {
            panelCampos.setLayout(new GridLayout(4, 2, 10, 15));
        }

        if (isModoModificar()) {
            jLabelIdEspectaculo = new JLabel("ID: " + getIdElementoModificar() + " (no se puede modificar el id)");
            jLabelIdEspectaculo.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabelIdEspectaculo.setForeground(new Color(100, 100, 100));

            JLabel espacioVacio = new JLabel("");

            panelCampos.add(jLabelIdEspectaculo);
            panelCampos.add(espacioVacio);
        }

        //CAMPO 1 - ComboBox de estadios
        jLabelEstadio = new JLabel("Estadio");
        jLabelEstadio.setFont(new Font("Arial", Font.PLAIN, 14));

        comboEstadios = new JComboBox<>();
        comboEstadios.setFont(new Font("Arial", Font.PLAIN, 14));
        //NO llamar a cargarEstadiosEnCombo() acá!

        panelCampos.add(jLabelEstadio);
        panelCampos.add(comboEstadios);

        //CAMPO 2 - Nombre del espectáculo
        jLabelNombre = new JLabel("Nombre del espectáculo");
        jLabelNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldNombre = new JTextField(20);

        panelCampos.add(jLabelNombre);
        panelCampos.add(jTextFieldNombre);

        //CAMPO 3 - Fecha
        jLabelFecha = new JLabel("Fecha (dd/MM/yyyy)");
        jLabelFecha.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldFecha = new JTextField(20);
        jTextFieldFecha.setToolTipText("Formato: dd/MM/yyyy (ejemplo: 25/12/2025)");

        panelCampos.add(jLabelFecha);
        panelCampos.add(jTextFieldFecha);

        //CAMPO 4 - Checkbox Activo
        jLabelActivo = new JLabel("¿Espectáculo activo?");
        jLabelActivo.setFont(new Font("Arial", Font.PLAIN, 14));
        jCheckBoxActivo = new JCheckBox();
        jCheckBoxActivo.setSelected(true);

        panelCampos.add(jLabelActivo);
        panelCampos.add(jCheckBoxActivo);

        return panelCampos;
    }


    private void cargarEstadiosEnCombo() {
        try {
            listaEstadios = serviceEstadio.consultarTodos();

            if (listaEstadios.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay estadios disponibles. Primero debe crear un estadio.",
                        "Sin estadios",
                        JOptionPane.WARNING_MESSAGE
                );
                getPanelManager().mostrar(getCodigoMenuPrincipal());
                return;
            }

            //agregar cada estadio al combo
            for (Estadio estadio : listaEstadios) {
                String item = estadio.getIdEstadio() + " - " + estadio.getNombre();
                comboEstadios.addItem(item);
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los estadios: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            getPanelManager().mostrar(getCodigoMenuPrincipal());
        }
    }


    @Override
    public void guardar() {
        //leer datos de los campos
        String nombre = jTextFieldNombre.getText();
        String fechaTexto = jTextFieldFecha.getText();


        //VALIDACIONES DE GUI -----
        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            jTextFieldNombre.requestFocus();
            return;
        }

        if (comboEstadios.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estadio", "Estadio no seleccionado", JOptionPane.WARNING_MESSAGE);
            comboEstadios.requestFocus();
            return;
        }
        if (fechaTexto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha es obligatoria", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            jTextFieldFecha.requestFocus();
            return;
        }

        Date fecha;
        try {
            fecha = formatoFecha.parse(fechaTexto);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Formato de fecha incorrecto. Use: dd/MM/yyyy\nEjemplo: 25/12/2025",
                    "Formato incorrecto",
                    JOptionPane.ERROR_MESSAGE
            );
            jTextFieldFecha.requestFocus();
            return;
        }


        //OBTENER EL ESTADIO SELECCIONADO -----
        int indiceEstadioSeleccionado = comboEstadios.getSelectedIndex();
        Estadio estadioSeleccionado = listaEstadios.get(indiceEstadioSeleccionado);
        int idEstadio = estadioSeleccionado.getIdEstadio();

        //OBTENER EL ESTADO DEL CHECKBOX -----
        boolean activo = jCheckBoxActivo.isSelected();


        //CREAR OBJETO ESPECTACULO -----
        Espectaculo espectaculo = new Espectaculo();
        espectaculo.setNombre(nombre);
        espectaculo.setFecha(fecha);
        espectaculo.setIdEstadio(idEstadio);
        espectaculo.setActivo(activo);

        if (isModoModificar()) {
            espectaculo.setIdEspectaculo(getIdElementoModificar());
        }


        //GUARDAR/MODIFICAR EN BASE DE DATOS -----
        try {
            if (isModoModificar()) {
                //modo modificar
                serviceEspectaculo.modificar(espectaculo);

                JOptionPane.showMessageDialog(this, "Espectáculo modificado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                getPanelManager().mostrar(getCodigoMenuPrincipal());

            } else {
                //modo crear
                serviceEspectaculo.insertar(espectaculo);
                JOptionPane.showMessageDialog(this, "Espectáculo creado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                //volver
                getPanelManager().mostrar(getCodigoMenuPrincipal());
            }
        }
        catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }


    //METODO 4 - getCodigoMenuPrincipal() -----
    @Override
    public int getCodigoMenuPrincipal() {
        return 40; //código 40 = MenuGestionEspectaculos
    }


    //METODO ADICIONAL - cargarDatos() -----
    //Solo se usa en modo modificar
    private void cargarDatos(Espectaculo espectaculo) {
        //cargar los campos de texto
        jTextFieldNombre.setText(espectaculo.getNombre());
        jTextFieldFecha.setText(formatoFecha.format(espectaculo.getFecha()));
        jCheckBoxActivo.setSelected(espectaculo.isActivo());

        //seleccionar el estadio correcto en el combo
        for (int i = 0; i < listaEstadios.size(); i++) {
            if (listaEstadios.get(i).getIdEstadio() == espectaculo.getIdEstadio()) {
                comboEstadios.setSelectedIndex(i);
                break;
            }
        }
    }
}