package gui;

import entidades.Usuario;
import service.ServiceUsuario;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

//PANTALLA LOGIN - Primera pantalla del sistema
//Permite ingresar con username y password
//Valida contra la base de datos y redirige según el rol

public class PantallaLogin extends JPanel {
    //ATRIBUTOS
    private PanelManager panelManager;
    private ServiceUsuario serviceUsuario = new ServiceUsuario();
    private JPanel pantallaLogin;

    //campos de texto
    private JTextField jTextFieldUsername;
    private JPasswordField jPasswordField;

    //botones
    private JButton btnLogin;
    private JButton btnSalir;


    //CONSTRUCTOR
    public PantallaLogin(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarPantalla();
    }


    //METODO ARMAR PANTALLA
    private void armarPantalla() {
        pantallaLogin = new JPanel();
        pantallaLogin.setLayout(new BorderLayout(20, 20));
        pantallaLogin.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));


        //ZONA NORTE - TÍTULO
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        JLabel titulo = new JLabel("SISTEMA DE VENTA DE ENTRADAS");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(41, 98, 255));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Gestión de Espectáculos Públicos");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(100, 100, 100));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panelNorte.add(titulo);
        panelNorte.add(subtitulo);

        pantallaLogin.add(panelNorte, BorderLayout.NORTH);


        //ZONA CENTRO - FORMULARIO DE LOGIN
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new GridLayout(3, 2, 15, 15));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        //Label de instrucción
        JLabel lblInstruccion = new JLabel("Ingrese sus credenciales:", SwingConstants.CENTER);
        lblInstruccion.setFont(new Font("Arial", Font.BOLD, 18));
        lblInstruccion.setForeground(new Color(60, 60, 60));
        panelCentro.add(new JLabel("")); //espacio vacío
        panelCentro.add(lblInstruccion);

        //Campo USERNAME
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        jTextFieldUsername = new JTextField(20);
        jTextFieldUsername.setFont(new Font("Arial", Font.PLAIN, 16));

        panelCentro.add(lblUsername);
        panelCentro.add(jTextFieldUsername);

        //Campo PASSWORD
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        jPasswordField = new JPasswordField(20);
        jPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));

        panelCentro.add(lblPassword);
        panelCentro.add(jPasswordField);

        pantallaLogin.add(panelCentro, BorderLayout.CENTER);


        //ZONA SUR - BOTONES
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSur.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setPreferredSize(new Dimension(180, 50));
        btnLogin.setBackground(new Color(76, 175, 80)); //verde
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSalir.setPreferredSize(new Dimension(120, 40));
        btnSalir.setBackground(new Color(220, 220, 220));
        btnSalir.setFocusPainted(false);

        panelSur.add(btnLogin);
        panelSur.add(btnSalir);

        pantallaLogin.add(panelSur, BorderLayout.SOUTH);


        //agregar el panel interno a this
        this.setLayout(new BorderLayout());
        this.add(pantallaLogin, BorderLayout.CENTER);


        //COMPORTAMIENTO DE LOS BOTONES

        //botón LOGIN
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });

        //permitir hacer login con ENTER
        jPasswordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });

        //botón SALIR
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(
                        PantallaLogin.this,
                        "¿Está seguro que desea salir del sistema?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }


    //METODO VALIDAR LOGIN
    private void validarLogin() {
        //obtener los datos ingresados
        String username = jTextFieldUsername.getText().trim();
        String password = new String(jPasswordField.getPassword()).trim();

        //validar que no estén vacíos
        if(username.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un username",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE
            );
            jTextFieldUsername.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una contraseña",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE
            );
            jPasswordField.requestFocus();
            return;
        }

        //buscar el usuario en la base de datos
        try {
            List<Usuario> usuarios = serviceUsuario.consultarTodos();

            Usuario usuarioEncontrado = null;

            //buscar si existe un usuario con ese username y password
            for(Usuario usuario : usuarios) {
                if(usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                    usuarioEncontrado = usuario;
                    break;
                }
            }

            //verificar si se encontró
            if(usuarioEncontrado == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Username o contraseña incorrectos",
                        "Login fallido",
                        JOptionPane.ERROR_MESSAGE
                );
                jPasswordField.setText("");
                jPasswordField.requestFocus();
                return;
            }

            //si llegamos acá, el login fue exitoso
            //guardar el usuario en el PanelManager
            panelManager.setUsuarioLogueado(usuarioEncontrado);

            //mostrar mensaje de bienvenida
            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido/a " + usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellido(),
                    "Login exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            //redirigir según el rol
            if(usuarioEncontrado.getRol().equals("ADMINISTRADOR")) {
                panelManager.mostrar(10); //código 10 = MenuAdmin
            } else if(usuarioEncontrado.getRol().equals("VENDEDOR")) {
                panelManager.mostrar(20); //código 20 = MenuVendedor
            }

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al validar el usuario: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}