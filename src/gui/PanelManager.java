package gui;

import javax.swing.*;
import java.awt.*;

public class PanelManager {

    //panel manager es el controlador que tiene la ventana (JFrame), cambia entre pantallas y coordina todo
    //tengo que tener todas las pantallas que voy a utilizar
    //atenti a lo de panel manager en formulario, lo de this y lo de mostrar

    //definidos como atributos todas las pantallas que voy a usar
        private MenuPrincipal menuPrincipal;
        private FormularioEstadio formularioEstadio;
        private PantallaEliminar pantallaEliminar;
        private ReporteEstadios reporteEstadios;
        JFrame jFrame; //aparece aca mi unica ventana



        public PanelManager(int tipo) //le paso un tipo inicial
        {
            jFrame=new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //para que se cierren las ventanas
            if (tipo==1)
                formularioEstadio=new FormularioEstadio(this);
            mostrar(formularioEstadio);
        }



        public void mostrar(JPanel panel) //es un panel porque todas las vistas que yo hago heredan el panel, para usar polimorfismo
        {
            jFrame.getContentPane().removeAll(); //sobre la ventana saca el que tenia. SACA EL PANEL ANTERIOR
            jFrame.getContentPane().add(BorderLayout.CENTER,panel); //agrega al nuevo. PONE EL NUEVO PANEL
            jFrame.getContentPane().validate(); //se fija que este todo definido correctamente
            jFrame.getContentPane().repaint();
            jFrame.setVisible(true); //lo muestra. MUESTRA LA VENTANA
            jFrame.pack(); //se acomoda segun la resolucion de la computadora
        }





        //verificar que esten bien !!!!!!!!!
        public void mostrar(int codigoPantalla) //este mostrar hace el trabajo de mostrar en pantalla
        {
            switch (codigoPantalla)
            {
              case 1:    //aca deberia enumerar las distintas vistas que deberia tener para crearla o mostrarla
                menuPrincipal =new MenuPrincipal(this); //este this es que se pasa a si mismo
                    mostrar(menuPrincipal);
                    break;

                case 2:
                    formularioEstadio = new FormularioEstadio(this); //form est crear
                    mostrar(formularioEstadio);
                    break;

                case 3:
                    formularioEstadio = new FormularioEstadio(this); //form est modificar
                    mostrar(formularioEstadio);
                    break;

                case 4:
                    pantallaEliminar = new PantallaEliminar();
                    mostrar(pantallaEliminar);
                    break;

                case 5:
                    reporteEstadios = new ReporteEstadios(this);
                    mostrar(reporteEstadios);
                    break;
            }
        }
    }
