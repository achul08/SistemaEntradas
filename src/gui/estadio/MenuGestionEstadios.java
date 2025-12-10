package gui.estadio;

import gui.PanelManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//MENU GESTIÓN ESTADIOS
//Menú principal para gestionar estadios (crear, modificar, eliminar, ver reporte)
//Igual que MenuPrincipal pero con nombre más descriptivo

public class MenuGestionEstadios extends JPanel {
    private PanelManager panelManager;
    private JPanel panelBotones;

    private JButton btnNuevo;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnReporte;
    private JButton btnVolver;

    public MenuGestionEstadios(PanelManager panelManager){
        this.panelManager = panelManager;
        armarMenu();
    }

    private void armarMenu(){
        setLayout(new BorderLayout());

        //TÍTULO
        JLabel titulo = new JLabel("GESTIÓN DE ESTADIOS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(163, 188, 226));
        titulo.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        add(titulo, BorderLayout.NORTH);

        //BOTONES
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5,1,10,10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));

        btnNuevo = new JButton("Crear nuevo estadio");
        btnModificar = new JButton("Modificar estadio existente");
        btnEliminar = new JButton("Eliminar estadio existente");
        btnReporte = new JButton("Ver reporte de los estadios");
        btnVolver = new JButton("Volver al menú anterior");

        Font fuenteBotones = new Font("Arial", Font.PLAIN, 16);
        btnNuevo.setFont(fuenteBotones);
        btnNuevo.setBackground(new Color(144, 238, 144));

        btnModificar.setFont(fuenteBotones);
        btnModificar.setBackground(new Color(173,216,230));

        btnEliminar.setFont(fuenteBotones);
        btnEliminar.setBackground(new Color(255,182,193));

        btnReporte.setFont(fuenteBotones);
        btnReporte.setBackground(new Color(255,255,153));

        btnVolver.setFont(fuenteBotones);
        btnVolver.setBackground(new Color(220,220,220));

        panelBotones.add(btnNuevo);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnReporte);
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.CENTER);

        //COMPORTAMIENTO
        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(2);
            }
        });

        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(3);
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(4);
            }
        });

        btnReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(5);
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(10); //vuelve al MenuAdmin
            }
        });
    }
}