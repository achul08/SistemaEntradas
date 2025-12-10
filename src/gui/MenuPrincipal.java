package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JPanel {
//primer pantalla que se va a ver, tiene los 5 botones


    private PanelManager panelManager; //esto para decirle al PanelManager cambia a otra pantalla cuando el usuario hace click

    //atributos, componentes visuales(botones, paneles)
    private JPanel panelBotones; //panel que contiene los botones
    private JButton btnNuevo; //boton crear
    private JButton btnModificar; //boton modificar
    private JButton btnEliminar; //boton eliminar
    private JButton btnReporte; //boton ver reporte
    private JButton btnSalir;


    //constructor que se ejecuta cuando se crea el MenuPrincipal
    public MenuPrincipal(PanelManager panelManager){
        this.panelManager = panelManager;
        armarMenu();
    }

    //armarMenu() crea toda la interfaz visual
    private void armarMenu(){
        //layout principal, border layout, divide la pantalla en norte sur este oeste centro
        setLayout(new BorderLayout());

        //poner un titulo arriba
        JLabel titulo = new JLabel("SISTEMA DE GESTION DE ESTADIOS", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20)); //fuente grande y en negrita
        titulo.setBorder(BorderFactory.createEmptyBorder(20,0,20,0)); //margen arriba/abajo
        add(titulo, BorderLayout.NORTH);

        //panel para los botones
        //GridLayout(filas, columnas) organiza los componentes en una cuadricula
        //GridLayout(5, 1) = 5 filas 1 columna
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5,1,10,10)); //filas, columnas, esp hor, esp vert

        panelBotones.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));
        //margen arriba 30, izq 50, abajo 30, der 50

        //crear los 5 botones
        btnNuevo = new JButton("Crear nuevo estadio");
        btnModificar = new JButton("Modificar estadio existente");
        btnEliminar = new JButton("Eliminar estadio existente");
        btnReporte = new JButton("Ver reporte de los estadios");
        btnSalir = new JButton("Volver al menu anterior");


        Font fuenteBotones = new Font("Arial", Font.PLAIN, 16);
        btnNuevo.setFont(fuenteBotones);
        btnNuevo.setBackground(new Color(144, 238, 144));//verde clarito

        btnModificar.setFont(fuenteBotones);
        btnModificar.setBackground(new Color(173,216,230)); //azul clarito

        btnEliminar.setFont(fuenteBotones);
        btnEliminar.setBackground(new Color(255,182,193)); //rosa clarito

        btnReporte.setFont(fuenteBotones);
        btnReporte.setBackground(new Color(255,255,153));

        btnSalir.setFont(fuenteBotones);
        btnSalir.setBackground(new Color(220,220,220)); //gris claro



        //agregar los botones al panel
        panelBotones.add(btnNuevo);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnReporte);
        panelBotones.add(btnSalir);


        //agregar los botones al centro
        add(panelBotones, BorderLayout.CENTER);


        //Agregar funcionalidad a los botones
        //este abre el formulario vacio para crear
      btnNuevo.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              panelManager.mostrar(2); //codigo 2 = formulario estadio en modo crear

          }
      });

        //este abre la pantalla pra elegir y modificar
        btnModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(3);
            }
        });

        //abre pantalla para eliminar
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(4);
            }
        });


        //abre la tabla con todos los estadios, la de reporte
        btnReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(5);
            }
        });

        //volver al menu admin
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelManager.mostrar(10); //codigo 10 = MenuAdmin
            }
        });




    }
        //actionlistener es una interfaz de java que dice "Todo escuchador debe tener un método llamado actionPerformed"
        //new ActionListener() { crea una clase anónima que implementa esa interfaz.
        //Una clase anonima es una clase que se crea y se usa en el mismo lugar
        //define que hace cada boton



}
