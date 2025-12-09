package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//PANTALLA LOGIN - Primera pantalla del sistema
//Permite seleccionar el ROL del usuario: Administrador o Vendedor

//RELACIÓN CON OTRAS CLASES:
//- PanelManager: para cambiar de pantalla según el rol elegido


public class PantallaLogin extends JPanel {
    //ATRIBUTOS -----
    private PanelManager panelManager; //para cambiar de pantalla
    private JPanel pantallaLogin; //panel interno que contiene todo

    //botones
    private JButton btnAdmin;
    private JButton btnVendedor;
    private JButton btnSalir;


    //CONSTRUCTOR ------
    public PantallaLogin(PanelManager panelManager) {
        this.panelManager = panelManager; //recibo el panel manager y lo asocio
        armarPantalla(); //arma la parte visual
    }


    //METODO ARMAR PANTALLA ----------
    private void armarPantalla() {
        //crear el panel interno
        pantallaLogin = new JPanel();
        pantallaLogin.setLayout(new BorderLayout(20, 20)); //espacio entre zonas
        pantallaLogin.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); //margen general


        //ZONA NORTE - TÍTULO ------
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS)); //componentes uno debajo del otro
        panelNorte.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0)); //margen

        //titulo principal
        JLabel titulo = new JLabel("SISTEMA DE VENTA DE ENTRADAS");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(41, 98, 255)); //azul
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT); //centrado

        //subtitulo
        JLabel subtitulo = new JLabel("Gestión de Espectáculos Públicos");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(100, 100, 100)); //gris
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); //margen arriba

        panelNorte.add(titulo);
        panelNorte.add(subtitulo);

        pantallaLogin.add(panelNorte, BorderLayout.NORTH);


        //ZONA CENTRO - SELECCIÓN DE ROL ------
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new GridLayout(3, 1, 20, 20)); //3 filas, 1 columna, espacio entre ellas
        panelCentro.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); //margen

        //label de instrucción
        JLabel lblInstruccion = new JLabel("Seleccione su rol:", SwingConstants.CENTER);
        lblInstruccion.setFont(new Font("Arial", Font.BOLD, 18));
        lblInstruccion.setForeground(new Color(60, 60, 60));

        //boton ADMINISTRADOR
        btnAdmin = new JButton("ADMINISTRADOR");
        btnAdmin.setFont(new Font("Arial", Font.BOLD, 18));
        btnAdmin.setPreferredSize(new Dimension(300, 70));
        btnAdmin.setBackground(new Color(144, 238, 144)); //verde clarito
        btnAdmin.setFocusPainted(false); //saca el borde cuando haces click

        //boton VENDEDOR
        btnVendedor = new JButton("VENDEDOR");
        btnVendedor.setFont(new Font("Arial", Font.BOLD, 18));
        btnVendedor.setPreferredSize(new Dimension(300, 70));
        btnVendedor.setBackground(new Color(173, 216, 230)); //azul clarito
        btnVendedor.setFocusPainted(false);

        panelCentro.add(lblInstruccion);
        panelCentro.add(btnAdmin);
        panelCentro.add(btnVendedor);

        pantallaLogin.add(panelCentro, BorderLayout.CENTER);


        //ZONA SUR - BOTÓN SALIR ------
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new FlowLayout(FlowLayout.CENTER)); //centrado
        panelSur.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); //margen arriba

        btnSalir = new JButton("Salir del Sistema");
        btnSalir.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSalir.setPreferredSize(new Dimension(200, 40));
        btnSalir.setBackground(new Color(220, 220, 220)); //gris claro
        btnSalir.setFocusPainted(false);

        panelSur.add(btnSalir);
        pantallaLogin.add(panelSur, BorderLayout.SOUTH);


        //agregar el panel interno a this
        //PantallaLogin ES un JPanel, entonces le ponemos BorderLayout
        //y agregamos el panel interno (pantallaLogin) en el centro
        this.setLayout(new BorderLayout());
        this.add(pantallaLogin, BorderLayout.CENTER);


        //COMPORTAMIENTO DE LOS BOTONES ------

        //boton ADMINISTRADOR
        //cuando haces click, va al MenuAdmin (código 10)
        btnAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(10); //codigo 10 = MenuAdmin
            }
        });

        //boton VENDEDOR
        //cuando haces click, va al MenuVendedor (código 20)
        btnVendedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(20); //codigo 20 = MenuVendedor
            }
        });

        //boton SALIR
        //pide confirmación antes de cerrar el programa
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showConfirmDialog muestra un mensaje con botones SI/NO
                int confirmacion = JOptionPane.showConfirmDialog(
                        PantallaLogin.this, //el this es para que el mensaje salga centrado sobre esta pantalla
                        "¿Está seguro que desea salir del sistema?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_OPTION, //botones Si/No
                        JOptionPane.QUESTION_MESSAGE //icono de pregunta
                );

                //si el usuario dijo SI, cerrar el programa
                if (confirmacion == JOptionPane.YES_OPTION) {
                    System.exit(0); //cierra todo
                }
                //si dijo NO, no hace nada y se queda en la pantalla
            }
        });
    }
}
