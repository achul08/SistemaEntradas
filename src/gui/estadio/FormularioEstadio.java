package gui.estadio;

import entidades.Estadio;
import gui.PanelManager;
import gui.base.FormularioBase;
import service.ServiceEstadio;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;

//FORMULARIO ESTADIO - Hereda de FormularioBase
//Pantalla para crear y modificar estadios

//RELACIÓN CON OTRAS CLASES:
//- Hereda de FormularioBase (extends)
//- ServiceEstadio: para guardar/modificar en la base de datos
//- PanelManager: para cambiar entre pantallas


public class FormularioEstadio extends FormularioBase {
    //ATRIBUTOS ESPECÍFICOS DE ESTADIO -----
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para comunicarse con la base de datos
    //campos de texto específicos de estadio
    private JTextField jTextFieldNombre;
    private JTextField jTextFieldDireccion;
    private JTextField jTextFieldCapacidad;
    private JTextField jTextFieldFotoUrl;

    //labels específicos de estadio
    private JLabel jLabelNombre;
    private JLabel jLabelDireccion;
    private JLabel jLabelCapacidad;
    private JLabel jLabelFotoUrl;
    private JLabel jLabelIdEstadio; //solo se muestra en modo modificar


    //CONSTRUCTOR PARA CREAR -----
    public FormularioEstadio(PanelManager panel) {
        super(panel); //llama al constructor de FormularioBase para modo crear
    }


    //CONSTRUCTOR PARA MODIFICAR -----
    public FormularioEstadio(PanelManager panel, Estadio estadio) {
        super(panel, estadio.getIdEstadio()); //llama al constructor de FormularioBase para modo modificar
        cargarDatos(estadio); //llenar los campos con los datos del estadio
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    //Devuelve el título según el modo (crear/modificar)
    @Override
    public String getTitulo() {
        if (isModoModificar()) {
            return "MODIFICAR ESTADIO";
        } else {
            return "CREAR NUEVO ESTADIO";
        }
    }


    //METODO 2 - crearPanelCampos() -----
    //Crea el panel con los campos específicos de estadio
    @Override
    public JPanel crearPanelCampos() {
        JPanel panelCampos = new JPanel();

        //en modo MODIFICAR son 5 filas (id, nombre, direc, cap, foto)
        //en modo CREAR son 4 filas (nombre, direc, cap, foto)
        if (isModoModificar()) {
            panelCampos.setLayout(new GridLayout(5, 2, 10, 15));
        } else {
            panelCampos.setLayout(new GridLayout(4, 2, 10, 15));
        }

        //si el modo es MODIFICAR se muestra el id
        if (isModoModificar()) {
            jLabelIdEstadio = new JLabel("ID: " + getIdElementoModificar() + " (no se puede modificar el id)");
            jLabelIdEstadio.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabelIdEstadio.setForeground(new Color(100, 100, 100)); //gris porque es no editable

            JLabel espacioVacio = new JLabel(""); //espacio vacío en la segunda columna

            panelCampos.add(jLabelIdEstadio);
            panelCampos.add(espacioVacio);
        }

        //labels y textfields
        jLabelNombre = new JLabel("Nombre");
        jLabelNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldNombre = new JTextField(20);

        jLabelDireccion = new JLabel("Direccion");
        jLabelDireccion.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldDireccion = new JTextField(20);

        jLabelCapacidad = new JLabel("Capacidad Total");
        jLabelCapacidad.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldCapacidad = new JTextField(20);
        jLabelFotoUrl = new JLabel("URL de la foto (opcional)");
        jLabelFotoUrl.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldFotoUrl = new JTextField(20);
        jTextFieldFotoUrl.setToolTipText("Ejemplo: https://ejemplo.com/foto-estadio.jpg");


        //agregar al panel en orden izq-der, arriba-abajo
        panelCampos.add(jLabelNombre);
        panelCampos.add(jTextFieldNombre);
        panelCampos.add(jLabelDireccion);
        panelCampos.add(jTextFieldDireccion);
        panelCampos.add(jLabelCapacidad);
        panelCampos.add(jTextFieldCapacidad);
        panelCampos.add(jLabelFotoUrl);
        panelCampos.add(jTextFieldFotoUrl);

        return panelCampos;
    }


    //METODO 3 - guardar() -----
    //Lógica para guardar o modificar el estadio
    @Override
    public void guardar() {
        //leer datos de los campos
        String nombre = jTextFieldNombre.getText();
        String direccion = jTextFieldDireccion.getText();
        String fotoUrl = jTextFieldFotoUrl.getText().trim();


        //VALIDACIONES DE GUI -----
        //verificar que el nombre no esté vacío
        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Campo Vacio", JOptionPane.WARNING_MESSAGE);
            jTextFieldNombre.requestFocus();
            return; //salir sin guardar
        }

        //verificar que la direccion no esté vacía
        if (direccion.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La direccion es obligatoria", "Campo vacio", JOptionPane.WARNING_MESSAGE);
            jTextFieldDireccion.requestFocus();
            return;
        }

        //verificar que la capacidad sea un número válido
        int capacidad;
        try {
            capacidad = Integer.parseInt(jTextFieldCapacidad.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número valido", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
            jTextFieldCapacidad.requestFocus();
            return;
        }



        //CREAR OBJETO ESTADIO -----
        Estadio estadio = new Estadio();
        estadio.setNombre(nombre);
        estadio.setDireccion(direccion);
        estadio.setCapacidadTotal(capacidad);
        estadio.setFotoUrl(fotoUrl);

        if (isModoModificar()) {
            estadio.setIdEstadio(getIdElementoModificar()); //si estamos modificando asignar id
        }


        //GUARDAR/MODIFICAR EN BASE DE DATOS -----
        try {
            if (isModoModificar()) {
                //modo modificar
                serviceEstadio.modificar(estadio);

                JOptionPane.showMessageDialog(this, "Estadio modificado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                getPanelManager().mostrar(getCodigoMenuPrincipal()); //vuelve al menu

            } else {
                //modo crear
                serviceEstadio.insertar(estadio);
                JOptionPane.showMessageDialog(this, "Estadio creado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);

                //volver al menú
                getPanelManager().mostrar(getCodigoMenuPrincipal());
            }
        }
        catch (ServiceException e) {
            //si el Service lanza una excepción, mostrar el mensaje de error
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }


    //METODO 4 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 1; //codigo 1 = MenuPrincipal de estadios
    }


    //METODO ADICIONAL - cargarDatos() -----
    //Solo se usa en modo modificar
    //Llena los campos del formulario con los datos del estadio
    private void cargarDatos(Estadio estadio) {
        jTextFieldNombre.setText(estadio.getNombre());
        jTextFieldDireccion.setText(estadio.getDireccion());
        jTextFieldCapacidad.setText(String.valueOf(estadio.getCapacidadTotal()));
        jTextFieldFotoUrl.setText(estadio.getFotoUrl() != null ? estadio.getFotoUrl() : "");
    }
}