package gui;
import entidades.Estadio;
import service.ServiceEstadio;
import service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//pantalla 2 //crear y modificar

//gridbaglayout buscar eso si quiero acomodar el layout


public class FormularioEstadio extends JPanel {
        ServiceEstadio serviceEstadio;
        PanelManager panel; //me voy a tener q comunicar con el panel manager, no lo estoy creando, recibo lo que viene por parametro del this panelmanager
        JPanel formularioEstadio; //otro panel
        JLabel jLabelidEstadio;
        JLabel jLabelnombre;
        JLabel jLabelDireccion;
        JTextField jTextFieldNombre;
        JTextField jTextFieldDireccion;
        JTextField jTextFieldidEstadio;
        JButton jButtonGrabar;
        JButton jButtonReporte;



        public FormularioEstadio(PanelManager panel)
        {
            this.panel=panel; //recibe el panel manager y se lo asigna al pm interno
            armarFormulario();
        }

        public void armarFormulario()
        {

            serviceEstadio=new ServiceEstadio();
            formularioEstadio=new JPanel();
            formularioEstadio.setLayout(new GridLayout(2,3)); //ACA MUESTRA COMO SE DISTRIBUYE LA INFORMACION DEL LAYOUT

            jLabelidEstadio=new JLabel("id Estadio");
            jTextFieldidEstadio=new JTextField(20);


            jLabelnombre=new JLabel("Nombre");
            jTextFieldNombre=new JTextField(20);

            jLabelDireccion=new JLabel("Direccion");
            jTextFieldDireccion=new JTextField(20);

            jButtonGrabar=new JButton("Grabar");
            jButtonReporte=new JButton("Ver reporte");
            jButtonGrabar.setBackground(Color.yellow); //para cambiar colores y eso
            jButtonReporte.setBackground(Color.pink);



            //aca agrego cosas al layout
            //00, 01 el orden
            //aca vinculo los new que cree arriba
            formularioEstadio.add(jLabelidEstadio);
            formularioEstadio.add(jTextFieldidEstadio);

            formularioEstadio.add(jLabelDireccion);
            formularioEstadio.add(jTextFieldDireccion);

            formularioEstadio.add(jLabelnombre);
            formularioEstadio.add(jTextFieldNombre);

            formularioEstadio.add(jButtonGrabar);
            formularioEstadio.add(jButtonReporte);



            //para que los botones hagan algo, les doy comportamiento
            jButtonGrabar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Estadio estadio=new Estadio();
                    estadio.setNombre(jTextFieldNombre.getText());
                    //aca tengo que levantar todos los datos de pantalla, ej
                    //int edad=Integer.parseInt(jTextFieldApellido.getText());  //hago la conversion de int a integer  o double
                    //agregar los atributos de las clases
                    try {
                        serviceEstadio.insertar(estadio); //insertar = guardar
                    }
                    catch (ServiceException s)
                    {
                        JOptionPane.showMessageDialog(null,"No se pudo guardar");
                    }

                }
            });



            jButtonReporte.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                  //  panel.mostrar(2);
                }
            });
            setLayout(new BorderLayout());
            add(formularioEstadio,BorderLayout.CENTER);
        }
    }



