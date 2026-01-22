package gui.usuario;

import entidades.Usuario;
import entidades.Administrador;
import entidades.Vendedor;
import gui.PanelManager;
import gui.base.FormularioBase;
import service.ServiceUsuario;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;

//FORMULARIO USUARIO - Hereda de FormularioBase
//Pantalla para crear y modificar usuarios (Administradores y Vendedores)

//RELACIÓN CON OTRAS CLASES:
//- Hereda de FormularioBase (extends)
//- ServiceUsuario: para guardar/modificar en la base de datos
//- PanelManager: para cambiar entre pantallas


public class FormularioUsuario extends FormularioBase {
    //ATRIBUTOS ESPECÍFICOS DE USUARIO
    private ServiceUsuario serviceUsuario = new ServiceUsuario();

    //campos de texto
    private JTextField jTextFieldNombre;
    private JTextField jTextFieldApellido;
    private JTextField jTextFieldUsername;
    private JPasswordField jPasswordField;

    //ComboBox para elegir el ROL
    private JComboBox<String> comboRol;

    //labels
    private JLabel jLabelIdUsuario; //solo se muestra en modo modificar
    private JLabel jLabelNombre;
    private JLabel jLabelApellido;
    private JLabel jLabelUsername;
    private JLabel jLabelPassword;
    private JLabel jLabelRol;

    //rol del usuario (para crear el objeto correcto)
    private String rolSeleccionado;


    //CONSTRUCTOR PARA CREAR
    public FormularioUsuario(PanelManager panel) {
        super(panel); //llama al constructor de FormularioBase para modo crear
    }


    //CONSTRUCTOR PARA MODIFICAR
    public FormularioUsuario(PanelManager panel, Usuario usuario) {
        super(panel, usuario.getIdUsuario()); //llama al constructor de FormularioBase para modo modificar
        cargarDatos(usuario); //llenar los campos con los datos del usuario
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS

    //METODO 1 - getTitulo()
    @Override
    public String getTitulo() {
        if (isModoModificar()) {
            return "MODIFICAR USUARIO";
        } else {
            return "CREAR NUEVO USUARIO";
        }
    }


    //METODO 2 - crearPanelCampos()
    @Override
    public JPanel crearPanelCampos() {
        JPanel panelCampos = new JPanel();

        //en modo MODIFICAR son 6 filas (id, nombre, apellido, username, password, rol)
        //en modo CREAR son 5 filas (nombre, apellido, username, password, rol)
        if (isModoModificar()) {
            panelCampos.setLayout(new GridLayout(6, 2, 10, 15));
        } else {
            panelCampos.setLayout(new GridLayout(5, 2, 10, 15));
        }

        //si el modo es MODIFICAR se muestra el id
        if (isModoModificar()) {
            jLabelIdUsuario = new JLabel("ID: " + getIdElementoModificar() + " (no se puede modificar el id)");
            jLabelIdUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
            jLabelIdUsuario.setForeground(new Color(100, 100, 100)); //gris

            JLabel espacioVacio = new JLabel("");

            panelCampos.add(jLabelIdUsuario);
            panelCampos.add(espacioVacio);
        }

        //CAMPO 1 - Nombre
        jLabelNombre = new JLabel("Nombre:");
        jLabelNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldNombre = new JTextField(20);

        panelCampos.add(jLabelNombre);
        panelCampos.add(jTextFieldNombre);

        //CAMPO 2 - Apellido
        jLabelApellido = new JLabel("Apellido:");
        jLabelApellido.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldApellido = new JTextField(20);

        panelCampos.add(jLabelApellido);
        panelCampos.add(jTextFieldApellido);

        //CAMPO 3 - Username
        jLabelUsername = new JLabel("Username:");
        jLabelUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        jTextFieldUsername = new JTextField(20);

        panelCampos.add(jLabelUsername);
        panelCampos.add(jTextFieldUsername);

        //CAMPO 4 - Password
        jLabelPassword = new JLabel("Contraseña:");
        jLabelPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        jPasswordField = new JPasswordField(20);

        panelCampos.add(jLabelPassword);
        panelCampos.add(jPasswordField);

        //CAMPO 5 - Rol (ComboBox)
        jLabelRol = new JLabel("Rol:");
        jLabelRol.setFont(new Font("Arial", Font.PLAIN, 14));

        comboRol = new JComboBox<>();
        comboRol.setFont(new Font("Arial", Font.PLAIN, 14));
        comboRol.addItem("ADMINISTRADOR");
        comboRol.addItem("VENDEDOR");

        panelCampos.add(jLabelRol);
        panelCampos.add(comboRol);

        return panelCampos;
    }


    //METODO 3 - guardar()
    @Override
    public void guardar() {
        //leer datos de los campos
        String nombre = jTextFieldNombre.getText();
        String apellido = jTextFieldApellido.getText();
        String username = jTextFieldUsername.getText();
        String password = new String(jPasswordField.getPassword());


        //VALIDACIONES DE GUI

        //verificar que el nombre no esté vacío
        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            jTextFieldNombre.requestFocus();
            return;
        }

        //verificar que el apellido no esté vacío
        if (apellido.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El apellido es obligatorio", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            jTextFieldApellido.requestFocus();
            return;
        }

        //verificar que el username no esté vacío
        if (username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El username es obligatorio", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            jTextFieldUsername.requestFocus();
            return;
        }

        //verificar que la contraseña no esté vacía
        if (password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña es obligatoria", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            jPasswordField.requestFocus();
            return;
        }

        //verificar que el rol esté seleccionado
        if (comboRol.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un rol", "Rol no seleccionado", JOptionPane.WARNING_MESSAGE);
            comboRol.requestFocus();
            return;
        }


        //OBTENER EL ROL SELECCIONADO
        rolSeleccionado = (String) comboRol.getSelectedItem();


        //CREAR OBJETO USUARIO (Administrador o Vendedor según el rol)
        //Esta es la parte CLAVE: usamos herencia para crear el objeto correcto
        Usuario usuario;

        if (rolSeleccionado.equals("ADMINISTRADOR")) {
            //crear un Administrador
            usuario = new Administrador(nombre, apellido, username, password);
        } else {
            //crear un Vendedor
            usuario = new Vendedor(nombre, apellido, username, password);
        }

        //si estamos modificando, setear el ID
        if (isModoModificar()) {
            usuario.setIdUsuario(getIdElementoModificar());
        }


        //GUARDAR/MODIFICAR EN BASE DE DATOS
        try {
            if (isModoModificar()) {
                //modo modificar
                serviceUsuario.modificar(usuario);

                JOptionPane.showMessageDialog(this, "Usuario modificado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                getPanelManager().mostrar(getCodigoMenuPrincipal());

            } else {
                //modo crear
                serviceUsuario.insertar(usuario);
                JOptionPane.showMessageDialog(this, "Usuario creado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                //volver
                getPanelManager().mostrar(getCodigoMenuPrincipal());
            }
        }
        catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }


    //METODO 4 - getCodigoMenuPrincipal()
    @Override
    public int getCodigoMenuPrincipal() {
        return 11; //código 11 = MenuGestionUsuarios
    }


    //METODO ADICIONAL - cargarDatos()
    //Solo se usa en modo modificar
    private void cargarDatos(Usuario usuario) {
        //cargar los campos de texto
        jTextFieldNombre.setText(usuario.getNombre());
        jTextFieldApellido.setText(usuario.getApellido());
        jTextFieldUsername.setText(usuario.getUsername());
        jPasswordField.setText(usuario.getPassword());

        //seleccionar el rol correcto en el combo
        if (usuario.getRol().equals("ADMINISTRADOR")) {
            comboRol.setSelectedIndex(0);
        } else {
            comboRol.setSelectedIndex(1);
        }
    }
}