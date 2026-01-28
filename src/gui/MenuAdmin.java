package gui;
import gui.PanelManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuAdmin extends JPanel {
    //ATRIBUTOS -----
    private PanelManager panelManager; //para cambiar de pantalla
    private JPanel menuPanel; //panel interno que contiene todo

    //botones del menú
    private JButton btnGestionarUsuarios;
    private JButton btnGestionarEstadios;
    private JButton btnGestionarUbicaciones;
    private JButton btnGestionarEspectaculos;
    private JButton btnVerReportes;
    private JButton btnCerrarSesion;


    //CONSTRUCTOR ------
    public MenuAdmin(PanelManager panelManager) {
        this.panelManager = panelManager; //recibo el panel manager y lo asocio
        armarMenu(); //arma la parte visual
    }


    //METODO ARMAR MENU ----------
    private void armarMenu() {
        //crear el panel interno
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout(15, 15)); //espacio entre zonas
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); //margen general


        //ZONA NORTE -----
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BorderLayout());
        panelHeader.setBackground(new Color(76, 175, 80)); //verde
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); //margen interno

        //titulo
        JLabel titulo = new JLabel("PANEL DE ADMINISTRACIÓN");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE); //texto blanco

        //subtitulo
        JLabel subtitulo = new JLabel("Acceso completo al sistema");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(240, 255, 240)); //verde clarito

        //panel para apilar título y subtítulo verticalmente
        JPanel panelTitulos = new JPanel();
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));
        panelTitulos.setBackground(new Color(76, 175, 80)); //mismo verde
        panelTitulos.add(titulo);
        panelTitulos.add(subtitulo);

        panelHeader.add(panelTitulos, BorderLayout.WEST); //poner a la izquierda

        menuPanel.add(panelHeader, BorderLayout.NORTH);


        //ZONA CENTRO - BOTONES DEL MENÚ ------
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5, 1, 0, 15)); //5 filas, 1 columna, espacio vertical 15px
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); //margen

        //fuente para todos los botones
        Font fuenteBotones = new Font("Arial", Font.BOLD, 16);

        //BOTON 1 - Gestionar Usuarios
        btnGestionarUsuarios = new JButton("Gestionar Usuarios");
        btnGestionarUsuarios.setFont(fuenteBotones);
        btnGestionarUsuarios.setBackground(new Color(255, 235, 205)); //naranja clarito
        btnGestionarUsuarios.setFocusPainted(false);
        btnGestionarUsuarios.setPreferredSize(new Dimension(400, 60));

        //BOTON 2 - Gestionar Estadios
        btnGestionarEstadios = new JButton("Gestionar Estadios");
        btnGestionarEstadios.setFont(fuenteBotones);
        btnGestionarEstadios.setBackground(new Color(144, 238, 144)); //verde clarito
        btnGestionarEstadios.setFocusPainted(false);
        btnGestionarEstadios.setPreferredSize(new Dimension(400, 60));

        //BOTON 3 - Gestionar Ubicaciones
        btnGestionarUbicaciones = new JButton("Gestionar Ubicaciones");
        btnGestionarUbicaciones.setFont(fuenteBotones);
        btnGestionarUbicaciones.setBackground(new Color(173, 216, 230)); //azul clarito
        btnGestionarUbicaciones.setFocusPainted(false);
        btnGestionarUbicaciones.setPreferredSize(new Dimension(400, 60));

        //BOTON 4 - Gestionar Espectáculos
        btnGestionarEspectaculos = new JButton("Gestionar Espectáculos");
        btnGestionarEspectaculos.setFont(fuenteBotones);
        btnGestionarEspectaculos.setBackground(new Color(221, 160, 221)); //violeta clarito
        btnGestionarEspectaculos.setFocusPainted(false);
        btnGestionarEspectaculos.setPreferredSize(new Dimension(400, 60));

        //BOTON 5 - Ver Reportes
        btnVerReportes = new JButton("Ver Reportes");
        btnVerReportes.setFont(fuenteBotones);
        btnVerReportes.setBackground(new Color(255, 255, 153)); //amarillo clarito
        btnVerReportes.setFocusPainted(false);
        btnVerReportes.setPreferredSize(new Dimension(400, 60));

        //agregar todos los botones al panel
        panelBotones.add(btnGestionarUsuarios);
        panelBotones.add(btnGestionarEstadios);
        panelBotones.add(btnGestionarUbicaciones);
        panelBotones.add(btnGestionarEspectaculos);
        panelBotones.add(btnVerReportes);

        menuPanel.add(panelBotones, BorderLayout.CENTER);


        //ZONA SUR - BOTÓN CERRAR SESIÓN ------
        JPanel panelSur = new JPanel();
        panelSur.setLayout(new FlowLayout(FlowLayout.CENTER)); //centrado
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); //margen arriba

        btnCerrarSesion = new JButton("← Cerrar Sesión");
        btnCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCerrarSesion.setPreferredSize(new Dimension(200, 40));
        btnCerrarSesion.setBackground(new Color(220, 220, 220)); //gris claro
        btnCerrarSesion.setFocusPainted(false);

        panelSur.add(btnCerrarSesion);
        menuPanel.add(panelSur, BorderLayout.SOUTH);


        //agregar el panel interno a this
        this.setLayout(new BorderLayout());
        this.add(menuPanel, BorderLayout.CENTER);


        //COMPORTAMIENTO DE LOS BOTONES ------

        //boton GESTIONAR USUARIOS
        //va a abrir un submenú para crear/modificar/eliminar usuarios
        btnGestionarUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(11); //codigo 11 = MenuGestionUsuarios (lo vamos a crear después)
            }
        });

        //boton GESTIONAR ESTADIOS
        //va a tu MenuPrincipal actual (el que ya tenés funcionando)
        btnGestionarEstadios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(1); //codigo 1 = MenuPrincipal (tu menú de estadios)
            }
        });

        //boton GESTIONAR UBICACIONES
        //va a abrir el menú para crear ubicaciones dentro de cada estadio
        btnGestionarUbicaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(30); //codigo 30 = MenuGestionUbicaciones (lo vamos a crear)
            }
        });

        //boton GESTIONAR ESPECTÁCULOS
        //va a abrir el menú para crear eventos
        btnGestionarEspectaculos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(40); //codigo 40 = MenuGestionEspectaculos (lo vamos a crear)
            }
        });

        //boton VER REPORTES
        //va a abrir la pantalla de reportes con ventas por espectáculo, fechas, etc.
        btnVerReportes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(50); //codigo 50 = PantallaReportes (lo vamos a crear)
            }
        });

        //boton CERRAR SESIÓN
        //vuelve a la pantalla de login
        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(0); //codigo 0 = PantallaLogin
            }
        });
    }
}