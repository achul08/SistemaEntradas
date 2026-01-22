package gui.estadio;

import entidades.Estadio;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceEstadio;
import service.ServiceException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.util.List;

//REPORTE ESTADIOS - Hereda de ReporteBase
//Muestra una tabla con todos los estadios
//MEJORA: Ahora muestra las FOTOS de los estadios en la tabla

//RELACIÓN CON OTRAS CLASES:
//- Hereda de ReporteBase (extends)
//- ServiceEstadio: para obtener todos los estadios de la BD
//- PanelManager: para cambiar de pantalla


public class ReporteEstadios extends ReporteBase {
    //ATRIBUTOS ESPECÍFICOS DE ESTADIO -----
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para consultar los estadios

    //CONSTRUCTOR -----
    public ReporteEstadios(PanelManager panelManager) {
        super(panelManager); //llama al constructor de ReporteBase

        //Configurar el renderer personalizado ANTES de inicializar
        //para que las fotos se muestren correctamente
        configurarRendererFotos();

        inicializar(); //carga los datos
    }


    //METODO: Configurar el renderer para mostrar fotos -----
//Carga imágenes desde la carpeta resources/imagenes/estadios/
    private void configurarRendererFotos() {
        //getTable() devuelve la JTable (heredado de ReporteBase)
        //setDefaultRenderer() le dice a la tabla cómo dibujar cada celda
        getTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            //Este método se ejecuta para CADA celda de la tabla
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                //PREGUNTA: ¿Es la columna 4 (Foto) y tiene contenido?
                if (column == 4 && value != null && !value.toString().isEmpty() && !value.toString().equals("(Sin foto)")) {

                    //Crear un JLabel para mostrar la imagen
                    JLabel label = new JLabel();
                    label.setHorizontalAlignment(JLabel.CENTER); //centrar la imagen

                    try {
                        //PASO 1: Obtener el nombre del archivo desde la base de datos
                        String nombreArchivo = value.toString();

                        //PASO 2: Construir la ruta usando getResource()
                        String rutaRecurso = "imagenes/estadios/" + nombreArchivo;
                        java.net.URL urlImagen = getClass().getClassLoader().getResource(rutaRecurso);

                        //PASO 3: Verificar si encontró la imagen
                        if (urlImagen != null) {
                            //Si encontró la imagen, cargarla
                            ImageIcon icono = new ImageIcon(urlImagen);

                            //PASO 4: Redimensionar la imagen a 100x75 píxeles
                            Image imagenOriginal = icono.getImage();
                            Image imagenChica = imagenOriginal.getScaledInstance(100, 75, Image.SCALE_SMOOTH);
                            ImageIcon iconoChico = new ImageIcon(imagenChica);

                            //PASO 5: Poner la imagen en el label
                            label.setIcon(iconoChico);

                            //PASO 6: Configurar colores (ESTO FALTABA DENTRO DEL IF)
                            if (isSelected) {
                                label.setBackground(table.getSelectionBackground());
                                label.setForeground(table.getSelectionForeground());
                            } else {
                                label.setBackground(table.getBackground());
                                label.setForeground(table.getForeground());
                            }
                            label.setOpaque(true);

                        } else {
                            //Si NO encontró la imagen, mostrar mensaje de error
                            label.setText("(No encontrada)");
                            label.setForeground(Color.RED);

                            //Configurar colores de fondo
                            if (isSelected) {
                                label.setBackground(table.getSelectionBackground());
                            } else {
                                label.setBackground(table.getBackground());
                            }
                            label.setOpaque(true);
                        }
                    } catch (Exception e) {
                        //Si hubo error, mostrar mensaje
                        label.setText("(Error: " + e.getMessage() + ")");
                        label.setForeground(Color.RED);

                        //Configurar colores de fondo
                        if (isSelected) {
                            label.setBackground(table.getSelectionBackground());
                        } else {
                            label.setBackground(table.getBackground());
                        }
                        label.setOpaque(true);
                    }

                    //IMPORTANTE: Devolver el label con la imagen
                    return label;

                } else {
                    //Para las demás columnas (ID, Nombre, etc.), mostrar texto normal
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
        });

        //Aumentar la altura de las filas a 80 píxeles
        getTable().setRowHeight(80);
    }


    //IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS -----

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "REPORTE DE ESTADIOS";
    }


    //METODO 2 - obtenerNombresColumnas() -----
    //Define los nombres de las columnas de la tabla
    //CAMBIO: Ahora incluimos la columna "Foto"
    @Override
    public String[] obtenerNombresColumnas() {
        return new String[]{
                "ID Estadio",
                "Nombre",
                "Dirección",
                "Capacidad Total",
                "Foto"  //NUEVO: columna para mostrar la foto
        };
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todos los estadios de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEstadio.consultarTodos(); //devuelve List<Estadio>
    }


    //METODO 4 - convertirElementoAFila() -----
    //Convierte un estadio en un array de Object para mostrar en la tabla
    //El ORDEN debe coincidir con el orden de las columnas
    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Estadio estadio = (Estadio) elemento; //cast de Object a Estadio

        //Obtener la URL de la foto (puede ser null o vacía)
        String fotoUrl = estadio.getFotoUrl();

        //Si no tiene foto, mostrar texto indicativo
        if (fotoUrl == null || fotoUrl.trim().isEmpty()) {
            fotoUrl = "(Sin foto)";
        }

        //crear el array con los datos del estadio
        //IMPORTANTE: el orden debe ser igual al de obtenerNombresColumnas()
        return new Object[]{
                estadio.getIdEstadio(),      // columna 0: "ID Estadio"
                estadio.getNombre(),         // columna 1: "Nombre"
                estadio.getDireccion(),      // columna 2: "Dirección"
                estadio.getCapacidadTotal(), // columna 3: "Capacidad Total"
                fotoUrl                      // columna 4: "Foto" (URL o texto)
        };
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay estadios registrados";
    }


    //METODO 6 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 1; //código 1 = MenuPrincipal de estadios
    }
}


/*
¿Qué hace este código?

1. **`configurarRendererFotos()`** (línea 44):
   - Crea un "renderer" personalizado para la tabla
   - Cuando llega a la columna 4 (Foto), en lugar de mostrar texto, carga la imagen desde la URL
   - Redimensiona la imagen a 100x75 píxeles para que entre bien en la tabla
   - Si falla cargar la imagen, muestra "(Error al cargar foto)"

2. **`setRowHeight(80)`** (línea 84):
   - Aumenta la altura de las filas de 20 píxeles a 80 píxeles
   - Así las fotos se ven completas

3. **Nueva columna "Foto"** (línea 98):
   - Agregamos una columna extra en la tabla

4. **Manejo de URLs vacías** (línea 122):
   - Si un estadio no tiene foto, muestra "(Sin foto)"
 */