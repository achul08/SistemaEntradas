package gui.base;

import gui.PanelManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public abstract class PantallaSeleccionBase extends JPanel {
    //ATRIBUTOS COMUNES (PRIVATE) -----
    private PanelManager panelManager; //para cambiar de pantalla
    private JPanel pantallaSeleccion; //panel interno que contiene todo

    //componentes visuales comunes
    private JComboBox<String> comboElementos; //ComboBox para seleccionar el elemento
    private JButton jButtonModificar; //botón para modificar
    private JButton jButtonCancelar; //botón para cancelar

    //lista con TODOS los elementos de la BD
    //la usamos para saber cuál elemento seleccionó el usuario
    private List<?> listaElementos; //List de tipo genérico (puede ser List<Estadio>, List<Ubicacion>, etc.)


    //CONSTRUCTOR -----
    public PantallaSeleccionBase(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarPantalla();
    }


    //METODO ARMAR PANTALLA -----
//Construye la estructura visual común
    private void armarPantalla() {
        //crear el panel interno
        pantallaSeleccion = new JPanel();
        pantallaSeleccion.setLayout(new BorderLayout(10, 10));
        pantallaSeleccion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        //ZONA NORTE - TÍTULO -----
        String tituloTexto = getTitulo(); //llama al método abstracto de la clase hija

        JLabel titulo = new JLabel(tituloTexto, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(163, 188, 226)); //azul clarito
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); //margen abajo

        pantallaSeleccion.add(titulo, BorderLayout.NORTH);


        //ZONA CENTRO - LABEL Y COMBOBOX -----
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new GridLayout(2, 1, 10, 10)); //2 filas, 1 columna
        panelCentro.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        //label con instrucciones
        JLabel labelInstruccion = new JLabel(getInstruccion(), SwingConstants.CENTER);
        labelInstruccion.setFont(new Font("Arial", Font.PLAIN, 16));

        //crear ComboBox vacío
        comboElementos = new JComboBox<>();
        comboElementos.setFont(new Font("Arial", Font.PLAIN, 14));

        //agregar al panel centro
        panelCentro.add(labelInstruccion);
        panelCentro.add(comboElementos);

        pantallaSeleccion.add(panelCentro, BorderLayout.CENTER);


        //ZONA SUR - BOTONES -----
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        //botón modificar
        jButtonModificar = new JButton("Modificar");
        jButtonModificar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonModificar.setBackground(new Color(173, 216, 230)); //azul clarito
        jButtonModificar.setPreferredSize(new Dimension(150, 40));

        //botón cancelar
        jButtonCancelar = new JButton("Cancelar");
        jButtonCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonCancelar.setBackground(new Color(220, 220, 220)); //gris claro
        jButtonCancelar.setPreferredSize(new Dimension(150, 40));

        panelBotones.add(jButtonModificar);
        panelBotones.add(jButtonCancelar);

        pantallaSeleccion.add(panelBotones, BorderLayout.SOUTH);


        //agregar el panel interno a this
        this.setLayout(new BorderLayout());
        this.add(pantallaSeleccion, BorderLayout.CENTER);


        //COMPORTAMIENTO DE LOS BOTONES -----

        //botón MODIFICAR
        jButtonModificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioModificar();
            }
        });

        //botón CANCELAR
        jButtonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int codigoMenu = getCodigoMenuPrincipal();
                panelManager.mostrar(codigoMenu); //vuelve al menú principal
            }
        });
    }


    //METODO CARGAR ELEMENTOS EN COMBO -----
    //Consulta la BD y llena el ComboBox
    private void cargarElementosEnCombo() {
        try {
            //consultar todos los elementos (llama al método abstracto de la clase hija)
            listaElementos = consultarTodos();

            //verificar si hay elementos
            if (listaElementos.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        getMensajeSinElementos(), //llama al método abstracto
                        "Sin elementos",
                        JOptionPane.INFORMATION_MESSAGE
                );
                panelManager.mostrar(getCodigoMenuPrincipal()); //vuelve al menú
                return;
            }

            //recorrer cada elemento y agregarlo al ComboBox
            for (Object elemento : listaElementos) {
                //formato del item (llama al método abstracto de la clase hija)
                String item = formatearElementoParaCombo(elemento);
                comboElementos.addItem(item);
            }

        } catch (Exception e) {
            //si hay error al consultar, mostrar mensaje y volver al menú
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los elementos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            panelManager.mostrar(getCodigoMenuPrincipal());
        }
    }


    //METODO ABRIR FORMULARIO MODIFICAR -----
    //Se ejecuta cuando el usuario hace click en "Modificar"
    private void abrirFormularioModificar() {
        //verificar que haya algo seleccionado
        if (comboElementos.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, seleccione un elemento",
                    "Ningún elemento seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        //obtener el índice seleccionado
        int indiceSeleccionado = comboElementos.getSelectedIndex();

        //obtener el elemento correspondiente de la lista
        Object elementoSeleccionado = listaElementos.get(indiceSeleccionado);

        //abrir el formulario en modo modificar (llama al método abstracto)
        abrirFormularioModificarConElemento(elementoSeleccionado);
    }


    //GETTERS -----

    public PanelManager getPanelManager() {
        return panelManager;
    }

    public JComboBox<String> getComboElementos() {
        return comboElementos;
    }

    public JButton getButtonModificar() {
        return jButtonModificar;
    }

    public JButton getButtonCancelar() {
        return jButtonCancelar;
    }

    public List<?> getListaElementos() {
        return listaElementos;
    }

    protected void setListaElementos(List<?> lista) {
        this.listaElementos = lista;
    }



    //METODO INICIALIZAR -----
//Este método se llama DESPUÉS de que la clase hija termine de construirse
//De esta forma, los Services ya están inicializados y funcionan correctamente
//
//IMPORTANTE: Este método debe ser llamado desde el constructor de las clases hijas
//Ejemplo: super(panelManager); inicializar();
    public void inicializar() {
        cargarElementosEnCombo();
    }


    //MÉTODOS ABSTRACTOS -----
    //Las clases hijas DEBEN implementar estos métodos
    public abstract String getTitulo();
    public abstract String getInstruccion();
    public abstract List<?> consultarTodos() throws Exception;
    public abstract String formatearElementoParaCombo(Object elemento);
    public abstract String getMensajeSinElementos();
    public abstract void abrirFormularioModificarConElemento(Object elemento);
    public abstract int getCodigoMenuPrincipal();
}