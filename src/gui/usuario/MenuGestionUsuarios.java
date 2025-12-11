package gui.usuario;

import gui.PanelManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//MENU GESTION USUARIOS
//Menú principal para gestionar usuarios (crear, modificar, eliminar, ver reporte)
//Similar a MenuGestionEstadios pero para usuarios

//RELACIÓN CON OTRAS CLASES:
//- PanelManager: para navegar entre pantallas


public class MenuGestionUsuarios extends JPanel {
    //ATRIBUTOS
    private PanelManager panelManager;
    private JPanel panelBotones;

    //botones del menú
    private JButton btnNuevo;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnReporte;
    private JButton btnVolver;


    //CONSTRUCTOR
    public MenuGestionUsuarios(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarMenu();
    }


    //METODO ARMAR MENU
    private void armarMenu() {
        //layout principal
        setLayout(new BorderLayout());

        //ZONA NORTE - TÍTULO
        JLabel titulo = new JLabel("GESTIÓN DE USUARIOS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(255, 152, 0)); //naranja
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);


        //ZONA CENTRO - BOTONES
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5, 1, 10, 10)); //5 filas, 1 columna
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        //fuente para todos los botones
        Font fuenteBotones = new Font("Arial", Font.PLAIN, 16);

        //BOTON 1 - Crear nuevo usuario
        btnNuevo = new JButton("Crear nuevo usuario");
        btnNuevo.setFont(fuenteBotones);
        btnNuevo.setBackground(new Color(144, 238, 144)); //verde clarito

        //BOTON 2 - Modificar usuario existente
        btnModificar = new JButton("Modificar usuario existente");
        btnModificar.setFont(fuenteBotones);
        btnModificar.setBackground(new Color(173, 216, 230)); //azul clarito

        //BOTON 3 - Eliminar usuario existente
        btnEliminar = new JButton("Eliminar usuario existente");
        btnEliminar.setFont(fuenteBotones);
        btnEliminar.setBackground(new Color(255, 182, 193)); //rosa clarito

        //BOTON 4 - Ver reporte de usuarios
        btnReporte = new JButton("Ver reporte de usuarios");
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


        //COMPORTAMIENTO DE LOS BOTONES

        //botón CREAR NUEVO USUARIO
        //abre FormularioUsuario en modo crear
        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(12); //código 12 = FormularioUsuario (crear)
            }
        });

        //botón MODIFICAR USUARIO
        //abre PantallaSeleccionUsuarios para elegir cuál modificar
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(13); //código 13 = PantallaSeleccionUsuarios
            }
        });

        //botón ELIMINAR USUARIO
        //abre PantallaEliminarUsuario para elegir cuál eliminar
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(14); //código 14 = PantallaEliminarUsuario
            }
        });

        //botón VER REPORTE
        //abre ReporteUsuarios con la tabla
        btnReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(15); //código 15 = ReporteUsuarios
            }
        });

        //botón VOLVER
        //vuelve al MenuAdmin
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(10); //código 10 = MenuAdmin
            }
        });
    }
}