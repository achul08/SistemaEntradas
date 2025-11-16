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


//Esta clase es un JPanel (una pantalla) que muestra un formulario
// donde el usuario puede ingresar los datos de un estadio.

//RELACIÓN CON OTRAS CLASES:
//- ServiceEstadio: para guardar/modificar en la base de datos
//- PanelManager: para cambiar entre pantallas


public class FormularioEstadio extends JPanel {
        ServiceEstadio serviceEstadio; //para comunicarse con la base de datos
         PanelManager panel; //Referencia al PanelManager (para cambiar de pantalla). Me voy a tener q comunicar con el panel manager, no lo estoy creando, recibo lo que viene por parametro del this panelmanager
         JPanel formularioEstadio; //Panel interno que contiene todo el formulario

        //labels
         JLabel jLabelidEstadio;
         JLabel jLabelNombre;
         JLabel jLabelDireccion;
         JLabel jLabelCapacidad;
        //campos de texto
         JTextField jTextFieldNombre;
         JTextField jTextFieldDireccion;
         JTextField jTextFieldidEstadio;
         JTextField jTextFieldCapacidad;
         //botones
         JButton jButtonGuardar;
         JButton jButtonCancelar;



         //variables para controlar el modo
        private boolean modoModificar; //true = modificar  flase=crear
        private int idEstadioModificar; //guardamos el id cuando se modifica
        //guardo el id porque cuando modificamos, necesitamos saber CUÁL estadio estamos modificando.
        // Este ID se usa en: estadio.setIdEstadio(idEstadioModificar) antes de llamar a modificar()


        //CONSTRUCTOR PARA CREAR
        public FormularioEstadio(PanelManager panel) //parametro panel - el gestor de pantallas
        {
            this.panel=panel; //recibe el panel manager y se lo asigna al pm interno
            this.modoModificar= false;
            armarFormulario();
        }



        //CONSTRUCTOR PARA MODIFICAR
        public FormularioEstadio(PanelManager panel, Estadio estadio){ //el gestor de pantallas y el estadio que modifico
            this.panel = panel;
            this.modoModificar = true;
            this.idEstadioModificar = estadio.getIdEstadio();
            armarFormulario();
            cargarDatos(estadio); //para llenar los campos con los datos actuales
        }


        //METODO--------------------------
        //para crear/modificar. este metodo construye la pantalla visual
        public void armarFormulario()
        {
            serviceEstadio=new ServiceEstadio(); //inicializar el service
            formularioEstadio=new JPanel();
            formularioEstadio.setLayout(new BorderLayout(10,10)); //ACA MUESTRA COMO SE DISTRIBUYE LA INFORMACION DEL LAYOUT
            formularioEstadio.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); //margen 20px

            //zona norte. Titulo que cambia segun el modo
            String tituloTexto;
            if(modoModificar){
                tituloTexto = "MODIFICAR ESTADIO";
            }else{
                tituloTexto="CREAR NUEVO ESTADIO";
            }
            JLabel titulo = new JLabel(tituloTexto, SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 20));
            titulo.setForeground(new Color(163,188,226)); //azul clarito
            titulo.setBorder(BorderFactory.createEmptyBorder(0,0,20,0)); //margen abajo

            formularioEstadio.add(titulo, BorderLayout.NORTH);



            //ZONA CENTRO----------------------
            JPanel panelCampos = new JPanel();

            //En modo MODIFICAR son 4 filas(id, nombre, direc, cap)
            //En modo CREAR son 3 filas(nombre, direc, cap)
            if (modoModificar){
                panelCampos.setLayout((new GridLayout(4,2,10,15)));
            } else{
                panelCampos.setLayout(new GridLayout(3,2,10,15));
            } //filas, col, esp hor, esp vert



            //si el modo es MODIFICAR se muestra el id
            if(modoModificar){
                jLabelidEstadio = new JLabel("ID: "+ idEstadioModificar + "(no se puede modificar el id)");
                jLabelidEstadio.setFont(new Font("Arial", Font.PLAIN, 14));
                jLabelidEstadio.setForeground(new Color(100,100,100)); //gris porque es no editable

                JLabel espacioVacio = new JLabel(""); //creo un espacio vacio en la segunda col porque id no tiene campo de texto

                panelCampos.add(jLabelidEstadio);
                panelCampos.add(espacioVacio);
            }


            //labels y  textfields
            jLabelNombre=new JLabel("Nombre");
            jLabelNombre.setFont(new Font("Arial", Font.PLAIN, 14));
            jTextFieldNombre=new JTextField(20);

            jLabelDireccion=new JLabel("Direccion");
            jLabelDireccion.setFont(new Font("Arial", Font.PLAIN, 14));
            jTextFieldDireccion=new JTextField(20);

            jLabelCapacidad = new JLabel("Capacidad Total");
            jLabelCapacidad.setFont(new Font("Arial", Font.PLAIN, 14));
            jTextFieldCapacidad = new JTextField(20);


            //aca agrego cosas al layout
            //en orden izq-der arriba-abajo
            //aca vinculo los new que cree arriba
            panelCampos.add(jLabelNombre);
            panelCampos.add(jTextFieldNombre);
            panelCampos.add(jLabelDireccion);
            panelCampos.add(jTextFieldDireccion);
            panelCampos.add(jLabelCapacidad);
            panelCampos.add(jTextFieldCapacidad);


            formularioEstadio.add(panelCampos, BorderLayout.CENTER); //Agregar el panel de campos al centro del formularioEstadio


            //ZONA SUR. BOTONES -------------------------------
            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
            //flowlayout = los componentes se ponen uno al lado del otro, como si fueran palabras en un texto
            //CENTER: centrados, 20: espacio entre botones, 10: espacio arriba/abajo


            //boton guardar
            jButtonGuardar = new JButton("Guardar");
            jButtonGuardar.setFont(new Font("Arial", Font.BOLD, 14));
            jButtonGuardar.setBackground(new Color(144,238,144)); //verde clarito
            jButtonGuardar.setPreferredSize(new Dimension(150,40));


            //boton cacncelar
            jButtonCancelar = new JButton("Cancelar");
            jButtonCancelar.setFont(new Font("Arial", Font.BOLD, 14));
            jButtonCancelar.setBackground(new Color(220,220,220)); //gris clarito
            jButtonCancelar.setPreferredSize(new Dimension(150,40));

            panelBotones.add(jButtonGuardar);
            panelBotones.add(jButtonCancelar);

            formularioEstadio.add(panelBotones, BorderLayout.SOUTH);

           //hay un panel botones y otro campos porque tienen layouts diferentes

            //agregar el formulario a this
            //FormularioEstadio ES un JPanel, entonces le ponemos BorderLayout
            // y agregamos el panel interno (formularioEstadio) en el centro
            setLayout(new BorderLayout());
            add(formularioEstadio, BorderLayout.CENTER);




            //COMPORTAMIENTO DE LOS BOTONES -----------------------------
            //para que los botones hagan algo, les doy comportamiento
            jButtonGuardar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guardarEstadio();
                }
            });

            jButtonCancelar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panel.mostrar(1); //vuelve al menu principal
                }
            });


        }



            //METODO GUARDAR ESTADIO ----------------------------
            // Este método se ejecuta cuando el
            // usuario hace click en "Guardar"
            //1. Lee los datos de los campos
            //2. Validaciones de GUI (campos vacíos, formato de número)
            //3. Crea objeto Estadio
            //4. Llama al Service
            //5. Muestra mensaje de éxito o error

           //necesito dos validaciones, las del service y las de la gui
            private void guardarEstadio(){
            //leer datos
                String nombre = jTextFieldNombre.getText();
                String direccion = jTextFieldDireccion.getText();

                //String capacidadTexto = jTextFieldCapacidad.getText();



            //VALIDACIONES ----------
                //verificar que el nombre no este vacio
                if(nombre.trim().isEmpty()){
                    JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Campo Vacio", JOptionPane.WARNING_MESSAGE);
                    jTextFieldNombre.requestFocus();
                    return; //salir sin guardar
                }

                //verificar que la direccion no este vacia
                if(direccion.trim().isEmpty()){
                    JOptionPane.showMessageDialog(this, "La direccion es obligatoria", "Campo vacio", JOptionPane.WARNING_MESSAGE);
                    jTextFieldDireccion.requestFocus();
                    return;
                }


                //verificar que la cap sea un num valido, o sea que se pueda hacer int
                int capacidad;
                try{
                    //convertir el texto a numero
                    capacidad = Integer.parseInt(jTextFieldCapacidad.getText());
                } catch(NumberFormatException e){
                    //si no es un numero valido muestra error
                    JOptionPane.showMessageDialog(this, "La capacidad debe ser un número valido", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
                    //el this es "este objeto", si pongo null aparece en cualquier lado de la pantalla, pero con this aparece centrado sobre el form
                    jTextFieldCapacidad.requestFocus();
                    return;
                }





             //CREAR OBJETO ESTADIO --------
                Estadio estadio = new Estadio();
                estadio.setNombre(nombre);
                estadio.setDireccion(direccion);
                estadio.setCapacidadTotal(capacidad);

                if(modoModificar){
                    estadio.setIdEstadio((idEstadioModificar)); //si estamos modificando asignar id
                }


             //GUARDAR/MODIFICAR EN BASE DE DATOS -----------
                try{
                    if(modoModificar){
                        serviceEstadio.modificar(estadio);

                        JOptionPane.showMessageDialog(this, "Estadio modificado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                        panel.mostrar(1); //vuelvo al menu

                    } else{
                        //modo crear
                        serviceEstadio.insertar(estadio);
                        JOptionPane.showMessageDialog(this,"Estadio creado correctamente", "Exito", JOptionPane.INFORMATION_MESSAGE);


                        //limpiar los campos
                        jTextFieldNombre.setText("");
                        jTextFieldDireccion.setText("");
                        jTextFieldCapacidad.setText("");
                        jTextFieldNombre.requestFocus();

                        //o volver al menu
                        //panel.mostrar(1);

                    }

                }
                catch(ServiceException e){
                    //Si el Service lanza una excepción
                    // e.getMessage() contiene el mensaje específico del error, por ejemplo:
                    // "Ya existe un estadio con ese nombre"
                    // "La capacidad debe ser mayor a 0"
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
                    //JOptionPane (informationMessage, errorMessage, warningMessage, questionmessage son iconos que aparecen en el mensaje
                }

            }


            //METODO CARGAR DATOS ---------------
            //solo en modificar. Este método llena los campos del formulario con los datos
            // del estadio que se va a modificar

            private void cargarDatos(Estadio estadio){
            jTextFieldNombre.setText(estadio.getNombre());
            jTextFieldDireccion.setText(estadio.getDireccion());
            jTextFieldCapacidad.setText(String.valueOf(estadio.getCapacidadTotal()));
            }// String.valueOf() convierte el int a String para mostrarlo en el JTextField


    }


     /* info de la clase con martha
            jButtonGuardar.addActionListener(new ActionListener() {
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

            */


