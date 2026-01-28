package gui.base;

import gui.PanelManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class FormularioBase extends JPanel {
    //ATRIBUTOS COMUNES (PRIVATE) -----
    private PanelManager panelManager; //para cambiar de pantalla
    private JPanel formulario; //panel interno que contiene todo

    //botones comunes a todos los formularios
    private JButton jButtonGuardar;
    private JButton jButtonCancelar;

    //variables para controlar el modo
    private boolean modoModificar; //true = modificar, false = crear
    private int idElementoModificar; //guardamos el id cuando se modifica


    //CONSTRUCTOR PARA CREAR -----
    public FormularioBase(PanelManager panelManager) {
        this.panelManager = panelManager;
        this.modoModificar = false; //modo crear
        armarFormulario();
    }


    //CONSTRUCTOR PARA MODIFICAR -----
    public FormularioBase(PanelManager panelManager, int idElemento) {
        this.panelManager = panelManager;
        this.modoModificar = true; //modo modificar
        this.idElementoModificar = idElemento;
        armarFormulario();
    }


    //METODO ARMAR FORMULARIO -----
    private void armarFormulario() {
        //crear el panel interno
        formulario = new JPanel();
        formulario.setLayout(new BorderLayout(10, 10)); //espacio entre zonas
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //margen general


        //ZONA NORTE - TÍTULO -----
        //El título cambia según el modo (crear/modificar) y la entidad
        String tituloTexto = getTitulo(); //llama al método abstracto de la clase hija

        JLabel titulo = new JLabel(tituloTexto, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(163, 188, 226)); //azul clarito
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); //margen abajo

        formulario.add(titulo, BorderLayout.NORTH);


        //ZONA CENTRO - CAMPOS ESPECÍFICOS -----
        //Cada clase hija define sus propios campos
        JPanel panelCampos = crearPanelCampos(); //llama al método abstracto de la clase hija
        formulario.add(panelCampos, BorderLayout.CENTER);


        //ZONA SUR - BOTONES -----
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        //boton guardar
        jButtonGuardar = new JButton("Guardar");
        jButtonGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonGuardar.setBackground(new Color(144, 238, 144)); //verde clarito
        jButtonGuardar.setPreferredSize(new Dimension(150, 40));

        //boton cancelar
        jButtonCancelar = new JButton("Cancelar");
        jButtonCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonCancelar.setBackground(new Color(220, 220, 220)); //gris clarito
        jButtonCancelar.setPreferredSize(new Dimension(150, 40));

        panelBotones.add(jButtonGuardar);
        panelBotones.add(jButtonCancelar);

        formulario.add(panelBotones, BorderLayout.SOUTH);


        //agregar el formulario a this
        setLayout(new BorderLayout());
        add(formulario, BorderLayout.CENTER);


        //COMPORTAMIENTO DE LOS BOTONES -----
        jButtonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar(); //llama al método abstracto de la clase hija
            }
        });

        jButtonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codigoMenu = getCodigoMenuPrincipal(); //llama al método abstracto
                panelManager.mostrar(codigoMenu); //vuelve al menú correspondiente
            }
        });
    }


    //GETTERS Y SETTERS -----
    //Para que las clases hijas puedan acceder a los atributos de forma controlada

    public PanelManager getPanelManager() {
        return panelManager;
    }

    public JPanel getFormulario() {
        return formulario;
    }

    public JButton getButtonGuardar() {
        return jButtonGuardar;
    }

    public JButton getButtonCancelar() {
        return jButtonCancelar;
    }

    public boolean isModoModificar() {
        return modoModificar;
    }

    public int getIdElementoModificar() {
        return idElementoModificar;
    }


    //MÉTODOS ABSTRACTOS (PUBLIC) -----
    //Las clases hijas DEBEN implementar estos métodos

    public abstract String getTitulo();
    public abstract JPanel crearPanelCampos();
    public abstract void guardar();
    public abstract int getCodigoMenuPrincipal();
}