package gui.base;

import gui.PanelManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

//CLASE BASE ABSTRACTA - PantallaEliminarBase
//Define la estructura común de todas las pantallas de eliminación
//Las clases hijas implementan lo específico de cada entidad

//RELACIÓN CON OTRAS CLASES:
//- PantallaEliminarEstadio extends PantallaEliminarBase
//- PantallaEliminarUbicacion extends PantallaEliminarBase
//- PantallaEliminarEspectaculo extends PantallaEliminarBase


public abstract class PantallaEliminarBase extends JPanel {
    //ATRIBUTOS COMUNES (PRIVATE) -----
    private PanelManager panelManager; //para cambiar de pantalla
    private JPanel pantallaEliminar; //panel interno que contiene todo

    //componentes visuales comunes
    private JComboBox<String> comboElementos; //ComboBox para seleccionar el elemento
    private JButton jButtonEliminar; //botón para eliminar
    private JButton jButtonCancelar; //botón para cancelar

    //lista con TODOS los elementos de la BD
    private List<?> listaElementos; //List de tipo genérico


    //CONSTRUCTOR -----
    public PantallaEliminarBase(PanelManager panelManager) {
        this.panelManager = panelManager;
        armarPantalla();
    }


    //METODO ARMAR PANTALLA -----
    //Construye la estructura visual común
    private void armarPantalla() {
        //crear el panel interno
        pantallaEliminar = new JPanel();
        pantallaEliminar.setLayout(new BorderLayout(10, 10));
        pantallaEliminar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        //ZONA NORTE - TÍTULO -----
        String tituloTexto = getTitulo(); //llama al método abstracto de la clase hija

        JLabel titulo = new JLabel(tituloTexto, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(220, 20, 60)); //rojo
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); //margen abajo

        pantallaEliminar.add(titulo, BorderLayout.NORTH);


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

        pantallaEliminar.add(panelCentro, BorderLayout.CENTER);


        //CARGAR LOS ELEMENTOS EN EL COMBOBOX -----
        cargarElementosEnCombo();


        //ZONA SUR - BOTONES -----
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        //botón eliminar
        jButtonEliminar = new JButton("Eliminar");
        jButtonEliminar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonEliminar.setBackground(new Color(255, 182, 193)); //rojo clarito
        jButtonEliminar.setPreferredSize(new Dimension(150, 40));

        //botón cancelar
        jButtonCancelar = new JButton("Cancelar");
        jButtonCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        jButtonCancelar.setBackground(new Color(220, 220, 220)); //gris claro
        jButtonCancelar.setPreferredSize(new Dimension(150, 40));

        panelBotones.add(jButtonEliminar);
        panelBotones.add(jButtonCancelar);

        pantallaEliminar.add(panelBotones, BorderLayout.SOUTH);


        //agregar el panel interno a this
        this.setLayout(new BorderLayout());
        this.add(pantallaEliminar, BorderLayout.CENTER);


        //COMPORTAMIENTO DE LOS BOTONES -----

        //botón ELIMINAR
        jButtonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarElemento();
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


    //METODO ELIMINAR ELEMENTO -----
    //Se ejecuta cuando el usuario hace click en "Eliminar"
    private void eliminarElemento() {
        //PASO 1: Verificar que haya algo seleccionado
        if (comboElementos.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, seleccione un elemento",
                    "Ningún elemento seleccionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        //PASO 2: Obtener el elemento seleccionado
        int indiceSeleccionado = comboElementos.getSelectedIndex();
        Object elementoSeleccionado = listaElementos.get(indiceSeleccionado);

        //PASO 3: Obtener información del elemento para mostrar en el mensaje
        String infoElemento = obtenerInformacionElemento(elementoSeleccionado); //método abstracto

        //PASO 4: Pedir confirmación
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar el siguiente elemento?\n\n" +
                        infoElemento + "\n\n" +
                        "Esta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        //PASO 5: Si el usuario confirmó, eliminar
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                //obtener el ID del elemento
                int id = obtenerIdElemento(elementoSeleccionado); //método abstracto

                //llamar al método que elimina (método abstracto)
                eliminarEnBD(id);

                //mostrar mensaje de éxito
                JOptionPane.showMessageDialog(
                        this,
                        "El elemento ha sido eliminado correctamente",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                //volver al menú principal
                panelManager.mostrar(getCodigoMenuPrincipal());

            } catch (Exception e) {
                //si hubo error al eliminar, mostrar el mensaje
                JOptionPane.showMessageDialog(
                        this,
                        "Error al eliminar: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        //si el usuario dijo "No", no hacemos nada
    }


    //GETTERS Y SETTERS -----

    public PanelManager getPanelManager() {
        return panelManager;
    }

    public JComboBox<String> getComboElementos() {
        return comboElementos;
    }

    public JButton getButtonEliminar() {
        return jButtonEliminar;
    }

    public JButton getButtonCancelar() {
        return jButtonCancelar;
    }

    public List<?> getListaElementos() {
        return listaElementos;
    }

    public void setListaElementos(List<?> lista) {
        this.listaElementos = lista;
    }


    //MÉTODOS ABSTRACTOS (PUBLIC) -----
    //Las clases hijas DEBEN implementar estos métodos

    //getTitulo() - Devuelve el título de la pantalla
    //Ejemplo: "ELIMINAR ESTADIO"
    public abstract String getTitulo();

    //getInstruccion() - Devuelve el texto de instrucción
    //Ejemplo: "Seleccione el estadio que desea eliminar:"
    public abstract String getInstruccion();

    //consultarTodos() - Consulta todos los elementos de la BD
    //Ejemplo: serviceEstadio.consultarTodos()
    public abstract List<?> consultarTodos() throws Exception;

    //formatearElementoParaCombo() - Define cómo se muestra cada elemento en el ComboBox
    //Ejemplo: return estadio.getIdEstadio() + " - " + estadio.getNombre();
    public abstract String formatearElementoParaCombo(Object elemento);

    //getMensajeSinElementos() - Mensaje cuando no hay elementos
    //Ejemplo: "No hay estadios disponibles para eliminar"
    public abstract String getMensajeSinElementos();

    //obtenerInformacionElemento() - Info del elemento para mostrar en la confirmación
    //Ejemplo: return "ID: " + estadio.getIdEstadio() + "\nNombre: " + estadio.getNombre();
    public abstract String obtenerInformacionElemento(Object elemento);

    //obtenerIdElemento() - Obtiene el ID del elemento
    //Ejemplo: return ((Estadio) elemento).getIdEstadio();
    public abstract int obtenerIdElemento(Object elemento);

    //eliminarEnBD() - Llama al Service para eliminar
    //Ejemplo: serviceEstadio.eliminar(id);
    public abstract void eliminarEnBD(int id) throws Exception;

    //getCodigoMenuPrincipal() - Código del menú al que vuelve
    //Ejemplo: return 1;
    public abstract int getCodigoMenuPrincipal();
}