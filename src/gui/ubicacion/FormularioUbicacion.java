package gui.ubicacion;

import entidades.Ubicacion;
import entidades.Estadio;
import gui.PanelManager;
import gui.base.FormularioBase;
import service.ServiceUbicacion;
import service.ServiceEstadio;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

//FORMULARIO UBICACION - Hereda de FormularioBase
//Pantalla para crear y modificar ubicaciones

//RELACIÓN CON OTRAS CLASES:
//- Hereda de FormularioBase (extends)
//- ServiceUbicacion: para guardar/modificar en la base de datos
//- ServiceEstadio: para obtener la lista de estadios
//- PanelManager: para cambiar entre pantallas


public class FormularioUbicacion extends FormularioBase {
    //ATRIBUTOS ESPECÍFICOS DE UBICACION -----
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion(); //para comunicarse con la BD de ubicaciones
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para obtener la lista de estadios
    //campos específicos de ubicacion
    private JComboBox<String> comboEstadios; //ComboBox para seleccionar el estadio
    private JTextField jTextFieldNombre;
    private JTextField jTextFieldPrecio;
    private JTextField jTextFieldCapacidad;

    //labels específicos de ubicacion
    private JLabel jLabelIdUbicacion; //solo se muestra en modo modificar
    private JLabel jLabelEstadio;
    private JLabel jLabelNombre;
    private JLabel jLabelPrecio;
    private JLabel jLabelCapacidad;


    //lista de estadios (para saber qué estadio seleccionó el usuario)
    private List<Estadio> listaEstadios;


    //CONSTRUCTOR PARA CREAR -----
    public FormularioUbicacion(PanelManager panel) {
        super(panel); //llama al constructor de FormularioBase para modo crear
        cargarEstadiosEnCombo();
    }


    //CONSTRUCTOR PARA MODIFICAR -----
    public FormularioUbicacion(PanelManager panel, Ubicacion ubicacion) {
        super(panel, ubicacion.getIdUbicacion());
        //DESPUÉS del super, cargar los datos
        cargarEstadiosEnCombo();
        cargarDatos(ubicacion);
    }



    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        if (isModoModificar()) {
            return "MODIFICAR UBICACIÓN";
        } else {
            return "CREAR NUEVA UBICACIÓN";
        }
    }


    //METODO 2 - crearPanelCampos()
    @Override
    public JPanel crearPanelCampos() {
        JPanel panelCampos = new JPanel();

        if (isModoModificar()) {
            panelCampos.setLayout(new GridLayout(5, 2, 10, 15));
        } else {
            panelCampos.setLayout(new GridLayout(4, 2, 10, 15));
        }

        if (isModoModificar()) {
            jLabelIdUbicacion = new JLabel("ID: " + getIdElementoModificar() + " (no se puede modificar el id)");
            jLabelIdUbicacion.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabelIdUbicacion.setForeground(new Color(100, 100, 100));

            JLabel espacioVacio = new JLabel("");

            panelCampos.add(jLabelIdUbicacion);
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

        //CAMPO 2 - Nombre de la ubicacion
        jLabelNombre = new JLabel("Nombre");
        jLabelNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldNombre = new JTextField(20);

        panelCampos.add(jLabelNombre);
        panelCampos.add(jTextFieldNombre);

        //CAMPO 3 - Precio
        jLabelPrecio = new JLabel("Precio");
        jLabelPrecio.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldPrecio = new JTextField(20);

        panelCampos.add(jLabelPrecio);
        panelCampos.add(jTextFieldPrecio);

        //CAMPO 4 - Capacidad
        jLabelCapacidad = new JLabel("Capacidad");
        jLabelCapacidad.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldCapacidad = new JTextField(20);


        panelCampos.add(jLabelCapacidad);
        panelCampos.add(jTextFieldCapacidad);

        return panelCampos;
    }


    //METODO AUXILIAR - cargarEstadiosEnCombo() -----
    //Consulta todos los estadios de la BD y los carga en el ComboBox
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
                getPanelManager().mostrar(getCodigoMenuPrincipal()); //vuelve al menu
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


    //METODO 3 - guardar() -----
    @Override
    public void guardar() {
        //leer datos de los campos
        String nombre = jTextFieldNombre.getText();


        //VALIDACIONES DE GUI -----

        //verificar que el nombre no esté vacío
        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            jTextFieldNombre.requestFocus();
            return;
        }

        //verificar que haya un estadio seleccionado
        if (comboEstadios.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estadio", "Estadio no seleccionado", JOptionPane.WARNING_MESSAGE);
            comboEstadios.requestFocus();
            return;
        }

        //verificar que el precio sea un número válido
        double precio;
        try {
            precio = Double.parseDouble(jTextFieldPrecio.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
            jTextFieldPrecio.requestFocus();
            return;
        }

        //verificar que la capacidad sea un número válido
        int capacidad;
        try {
            capacidad = Integer.parseInt(jTextFieldCapacidad.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número válido", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
            jTextFieldCapacidad.requestFocus();
            return;
        }


        //OBTENER EL ESTADIO SELECCIONADO -----
        int indiceEstadioSeleccionado = comboEstadios.getSelectedIndex();
        Estadio estadioSeleccionado = listaEstadios.get(indiceEstadioSeleccionado);
        int idEstadio = estadioSeleccionado.getIdEstadio();


        //CREAR OBJETO UBICACION -----
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setIdEstadio(idEstadio);
        ubicacion.setNombre(nombre);
        ubicacion.setPrecio(precio);
        ubicacion.setCapacidad(capacidad);

        if (isModoModificar()) {
            ubicacion.setIdUbicacion(getIdElementoModificar());
        }


        //GUARDAR/MODIFICAR EN BASE DE DATOS -----
        try {
            if (isModoModificar()) {
                //modo modificar
                serviceUbicacion.modificar(ubicacion);

                JOptionPane.showMessageDialog(this, "Ubicación modificada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                getPanelManager().mostrar(getCodigoMenuPrincipal());

            } else {
                //modo crear
                serviceUbicacion.insertar(ubicacion);
                JOptionPane.showMessageDialog(this, "Ubicación creada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

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
        return 30; //código 30 = MenuGestionUbicaciones
    }


    //METODO ADICIONAL - cargarDatos() -----
    //Solo se usa en modo modificar
    private void cargarDatos(Ubicacion ubicacion) {
        //cargar los campos de texto
        jTextFieldNombre.setText(ubicacion.getNombre());
        jTextFieldPrecio.setText(String.valueOf(ubicacion.getPrecio()));
        jTextFieldCapacidad.setText(String.valueOf(ubicacion.getCapacidad()));

        //seleccionar el estadio correcto en el combo
        //buscar el índice del estadio en la lista
        for (int i = 0; i < listaEstadios.size(); i++) {
            if (listaEstadios.get(i).getIdEstadio() == ubicacion.getIdEstadio()) {
                comboEstadios.setSelectedIndex(i);
                break;
            }
        }
    }
}