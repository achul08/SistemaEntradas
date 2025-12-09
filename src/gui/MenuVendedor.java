package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//MENU VENDEDOR - Panel principal para usuarios con rol VENDEDOR
//Tiene acceso limitado: solo puede registrar ventas y consultar espectáculos

//RELACIÓN CON OTRAS CLASES:
//- PanelManager: para navegar entre pantallas


public class MenuVendedor extends JPanel {
    //ATRIBUTOS -----
    private PanelManager panelManager; //para cambiar de pantalla
    private JPanel menuPanel; //panel interno que contiene todo

    //botones del menú
    private JButton btnRegistrarVenta;
    private JButton btnVerEspectaculos;
    private JButton btnVerMisVentas;
    private JButton btnCerrarSesion;


    //CONSTRUCTOR ------
    public MenuVendedor(PanelManager panelManager) {
        this.panelManager = panelManager; //recibo el panel manager y lo asocio
        armarMenu(); //arma la parte visual
    }


    //METODO ARMAR MENU ----------
    private void armarMenu() {
        //crear el panel interno
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout(15, 15)); //espacio entre zonas
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); //margen general


        //ZONA NORTE - HEADER (cabecera azul con título) ------
        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(new BorderLayout());
        panelHeader.setBackground(new Color(33, 150, 243)); //azul
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); //margen interno

        //titulo
        JLabel titulo = new JLabel("PANEL DE VENDEDOR");
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE); //texto blanco

        //subtitulo
        JLabel subtitulo = new JLabel("Registro de ventas");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(224, 247, 250)); //azul clarito

        //panel para apilar título y subtítulo verticalmente
        JPanel panelTitulos = new JPanel();
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));
        panelTitulos.setBackground(new Color(33, 150, 243)); //mismo azul
        panelTitulos.add(titulo);
        panelTitulos.add(subtitulo);

        panelHeader.add(panelTitulos, BorderLayout.WEST); //poner a la izquierda

        menuPanel.add(panelHeader, BorderLayout.NORTH);


        //ZONA CENTRO - BOTONES DEL MENÚ ------
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 1, 0, 20)); //3 filas, 1 columna, espacio vertical 20px
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60)); //margen

        //fuente para todos los botones
        Font fuenteBotones = new Font("Arial", Font.BOLD, 16);

        //BOTON 1 - Registrar Venta
        btnRegistrarVenta = new JButton("Registrar Venta");
        btnRegistrarVenta.setFont(fuenteBotones);
        btnRegistrarVenta.setBackground(new Color(144, 238, 144)); //verde clarito
        btnRegistrarVenta.setFocusPainted(false);
        btnRegistrarVenta.setPreferredSize(new Dimension(400, 70));

        //BOTON 2 - Ver Espectáculos Disponibles
        btnVerEspectaculos = new JButton("Ver Espectáculos Disponibles");
        btnVerEspectaculos.setFont(fuenteBotones);
        btnVerEspectaculos.setBackground(new Color(221, 160, 221)); //violeta clarito
        btnVerEspectaculos.setFocusPainted(false);
        btnVerEspectaculos.setPreferredSize(new Dimension(400, 70));

        //BOTON 3 - Ver Mis Ventas
        btnVerMisVentas = new JButton("Ver Mis Ventas");
        btnVerMisVentas.setFont(fuenteBotones);
        btnVerMisVentas.setBackground(new Color(255, 255, 153)); //amarillo clarito
        btnVerMisVentas.setFocusPainted(false);
        btnVerMisVentas.setPreferredSize(new Dimension(400, 70));

        //agregar todos los botones al panel
        panelBotones.add(btnRegistrarVenta);
        panelBotones.add(btnVerEspectaculos);
        panelBotones.add(btnVerMisVentas);

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

        //boton REGISTRAR VENTA
        //va a abrir el formulario para registrar una venta nueva
        btnRegistrarVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(21); //codigo 21 = FormularioVenta (lo vamos a crear)
            }
        });

        //boton VER ESPECTÁCULOS DISPONIBLES
        //va a mostrar una tabla con todos los espectáculos activos
        btnVerEspectaculos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(22); //codigo 22 = ReporteEspectaculosDisponibles (lo vamos a crear)
            }
        });

        //boton VER MIS VENTAS
        //va a mostrar las ventas que hizo este vendedor
        btnVerMisVentas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(23); //codigo 23 = ReporteVentasVendedor (lo vamos a crear)
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