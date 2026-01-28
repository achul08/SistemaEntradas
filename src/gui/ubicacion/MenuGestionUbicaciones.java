package gui.ubicacion;

import gui.PanelManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuGestionUbicaciones extends JPanel {
    //ATRIBUTOS -----
    private PanelManager panelManager; //para cambiar de pantalla
    private JPanel panelBotones; //panel que contiene los botones

    //botones del menú
    private JButton btnNuevo;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnReporte;
    private JButton btnVolver;


    //CONSTRUCTOR -----
    public MenuGestionUbicaciones(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarMenu();
    }


    //METODO ARMAR MENU -----
    private void armarMenu() {
        //layout principal
        setLayout(new BorderLayout());

        //ZONA NORTE - TÍTULO -----
        JLabel titulo = new JLabel("GESTIÓN DE UBICACIONES", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(163, 188, 226)); //azul clarito
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); //margen arriba/abajo
        add(titulo, BorderLayout.NORTH);


        //ZONA CENTRO - BOTONES -----
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5, 1, 10, 10)); //5 filas, 1 columna
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); //margen

        //fuente para todos los botones
        Font fuenteBotones = new Font("Arial", Font.PLAIN, 16);

        //BOTON 1 - Crear nueva ubicación
        btnNuevo = new JButton("Crear nueva ubicación");
        btnNuevo.setFont(fuenteBotones);
        btnNuevo.setBackground(new Color(144, 238, 144)); //verde clarito

        //BOTON 2 - Modificar ubicación existente
        btnModificar = new JButton("Modificar ubicación existente");
        btnModificar.setFont(fuenteBotones);
        btnModificar.setBackground(new Color(173, 216, 230)); //azul clarito

        //BOTON 3 - Eliminar ubicación existente
        btnEliminar = new JButton("Eliminar ubicación existente");
        btnEliminar.setFont(fuenteBotones);
        btnEliminar.setBackground(new Color(255, 182, 193)); //rosa clarito

        //BOTON 4 - Ver reporte de ubicaciones
        btnReporte = new JButton("Ver reporte de ubicaciones");
        btnReporte.setFont(fuenteBotones);
        btnReporte.setBackground(new Color(255, 255, 153)); //amarillo clarito

        //BOTON 5 - Volver al menú anterior
        btnVolver = new JButton("Volver al menú anterior");
        btnVolver.setFont(fuenteBotones);
        btnVolver.setBackground(new Color(220, 220, 220)); //gris claro

        //agregar los botones al panel
        panelBotones.add(btnNuevo);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnReporte);
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.CENTER);


        //COMPORTAMIENTO DE LOS BOTONES -----

        //botón CREAR NUEVA UBICACIÓN
        //abre FormularioUbicacion en modo crear
        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(31);
            }
        });

        //botón MODIFICAR UBICACIÓN
        //abre PantallaSeleccionUbicaciones para elegir cuál modificar
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(32);
            }
        });

        //botón ELIMINAR UBICACIÓN
        //abre PantallaEliminarUbicacion para elegir cuál eliminar
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(33);
            }
        });

        //botón VER REPORTE
        //abre ReporteUbicaciones con la tabla
        btnReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(34);
            }
        });

        //botón VOLVER
        //vuelve al MenuAdmin
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(10);
            }
        });
    }
}