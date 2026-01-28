package gui.espectaculo;

import gui.PanelManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuGestionEspectaculos extends JPanel {
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
    public MenuGestionEspectaculos(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarMenu();
    }


    //METODO ARMAR MENU -----
    private void armarMenu() {
        //layout principal
        setLayout(new BorderLayout());

        //ZONA NORTE - TÍTULO -----
        JLabel titulo = new JLabel("GESTIÓN DE ESPECTÁCULOS", SwingConstants.CENTER);
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

        //BOTON 1 - Crear nuevo espectáculo
        btnNuevo = new JButton("Crear nuevo espectáculo");
        btnNuevo.setFont(fuenteBotones);
        btnNuevo.setBackground(new Color(144, 238, 144)); //verde clarito

        //BOTON 2 - Modificar espectáculo existente
        btnModificar = new JButton("Modificar espectáculo existente");
        btnModificar.setFont(fuenteBotones);
        btnModificar.setBackground(new Color(173, 216, 230)); //azul clarito

        //BOTON 3 - Eliminar espectáculo existente
        btnEliminar = new JButton("Eliminar espectáculo existente");
        btnEliminar.setFont(fuenteBotones);
        btnEliminar.setBackground(new Color(255, 182, 193)); //rosa clarito

        //BOTON 4 - Ver reporte de espectáculos
        btnReporte = new JButton("Ver reporte de espectáculos");
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

        //botón CREAR NUEVO ESPECTÁCULO
        //abre FormularioEspectaculo en modo crear
        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(41);
            }
        });

        //botón MODIFICAR ESPECTÁCULO
        //abre PantallaSeleccionEspectaculos para elegir cuál modificar
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(42);
            }
        });

        //botón ELIMINAR ESPECTÁCULO
        //abre PantallaEliminarEspectaculo para elegir cuál eliminar
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(43);
            }
        });

        //botón VER REPORTE
        //abre ReporteEspectaculos con la tabla
        btnReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(44);
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