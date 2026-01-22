package gui.ubicacion;

import entidades.Ubicacion;
import entidades.Estadio;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceUbicacion;
import service.ServiceEstadio;
import service.ServiceException;

// IMPORTS NUEVOS para manejar imágenes
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.net.URL;
import java.util.List;

//REPORTE UBICACIONES - Hereda de ReporteBase
//Muestra una tabla con todas las ubicaciones
//AHORA CON FOTOS: Muestra el NOMBRE del estadio y la FOTO de la ubicación

//RELACIÓN CON OTRAS CLASES:
//- Hereda de ReporteBase (extends)
//- ServiceUbicacion: para obtener todas las ubicaciones de la BD
//- ServiceEstadio: para obtener el nombre del estadio de cada ubicación
//- PanelManager: para cambiar de pantalla


public class ReporteUbicaciones extends ReporteBase {
    //ATRIBUTOS ESPECÍFICOS DE UBICACION -----
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion(); //para consultar las ubicaciones
    private ServiceEstadio serviceEstadio = new ServiceEstadio(); //para obtener nombres de estadios
    //Ambos Services se crean ANTES del constructor
    //Por eso cuando llamemos a inicializar() ya van a existir


    //CONSTRUCTOR -----
    public ReporteUbicaciones(PanelManager panelManager) {
        super(panelManager); //llama al constructor de ReporteBase
        //En este momento:
        //1. serviceUbicacion y serviceEstadio ya existen
        //2. ReporteBase ya armó toda la estructura visual
        //3. La tabla todavía está vacía

        //NUEVO: Configurar el renderer para mostrar fotos ANTES de inicializar
        //Esto es MUY IMPORTANTE: si no lo hacemos antes, las fotos no se van a ver
        configurarRendererFotos();

        inicializar(); //carga las columnas y los datos
    }


    //METODO: Configurar el renderer para mostrar fotos -----
//Carga imágenes desde la carpeta resources/imagenes/ubicaciones/
    private void configurarRendererFotos() {
        //getTable() devuelve la JTable (heredado de ReporteBase)
        //setDefaultRenderer() le dice a la tabla cómo dibujar cada celda
        getTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            //Este método se ejecuta para CADA celda de la tabla
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                //PREGUNTA: ¿Es la columna 5 (Foto) y tiene contenido?
                //NOTA: En ubicaciones la foto es la columna 5 (no 4 como en estadios)
                //Columnas: 0=ID, 1=Estadio, 2=Nombre, 3=Precio, 4=Capacidad, 5=Foto
                if (column == 5 && value != null && !value.toString().isEmpty() && !value.toString().equals("(Sin foto)")) {

                    //Crear un JLabel para mostrar la imagen
                    JLabel label = new JLabel();
                    label.setHorizontalAlignment(JLabel.CENTER); //centrar la imagen

                    try {
                        //PASO 1: Obtener el nombre del archivo desde la base de datos
                        //Ejemplo: "ubicacionesLuna.jpg"
                        String nombreArchivo = value.toString();

                        //PASO 2: Construir la ruta usando getResource()
                        //IMPORTANTE: NO incluir "resources/" porque ya está marcado como Resources Root
                        //Solo ponemos "imagenes/ubicaciones/" + nombreArchivo
                        java.net.URL urlImagen = getClass().getClassLoader().getResource("imagenes/ubicaciones/" + nombreArchivo);

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
                        } else {
                            //Si NO encontró la imagen, mostrar mensaje de error
                            label.setText("(No encontrada)");
                            label.setForeground(Color.RED);
                        }
                    } catch (Exception e) {
                        //Si hubo algún error al cargar, mostrar el mensaje del error
                        label.setText("(Error: " + e.getMessage() + ")");
                        label.setForeground(Color.RED);
                    }

                    //Configurar colores del label según si está seleccionado
                    if (isSelected) {
                        //Fila seleccionada = fondo azul
                        label.setBackground(table.getSelectionBackground());
                    } else {
                        //Fila normal = fondo blanco
                        label.setBackground(table.getBackground());
                    }
                    label.setOpaque(true); //hacer visible el fondo

                    //Devolver el label con la imagen (esto se dibuja en la celda)
                    return label;

                } else {
                    //Para las demás columnas (ID, Estadio, etc.), mostrar texto normal
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
        });

        //Aumentar la altura de las filas a 80 píxeles
        //Así las fotos de 75px entran bien
        getTable().setRowHeight(80);
    }


    //═════════════════════════════════════════════════════════════════════
    // IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS
    //═════════════════════════════════════════════════════════════════════

    //METODO 1 - getTitulo() -----
    @Override
    public String getTitulo() {
        return "REPORTE DE UBICACIONES";
    }


    //METODO 2 - obtenerNombresColumnas() -----
    //Define los nombres de las columnas de la tabla
    //MODIFICADO: Ahora incluye la columna "Foto"
    @Override
    public String[] obtenerNombresColumnas() {
        return new String[]{
                "ID Ubicación",    // columna 0
                "Estadio",         // columna 1
                "Nombre",          // columna 2
                "Precio",          // columna 3
                "Capacidad",       // columna 4
                "Foto"             // columna 5 ← NUEVA COLUMNA
        };
    }


    //METODO 3 - consultarTodos() -----
    //Consulta todas las ubicaciones de la BD
    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUbicacion.consultarTodos(); //devuelve List<Ubicacion>
    }


    //METODO 4 - convertirElementoAFila() -----
    //Convierte una ubicación en un array de Object para mostrar en la tabla
    //El ORDEN debe coincidir con el orden de las columnas
    //MODIFICADO: Ahora incluye la fotoUrl en la última posición
    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento; //cast de Object a Ubicacion

        //PASO 1: Obtener el nombre del estadio en lugar de solo mostrar el ID
        String nombreEstadio = "(Desconocido)"; //valor por defecto por si hay error

        try {
            //Consultar el estadio por su ID
            Estadio estadio = serviceEstadio.consultar(ubicacion.getIdEstadio());

            //Verificar que el estadio exista (consultar puede devolver un objeto vacío)
            if(estadio != null && estadio.getIdEstadio() != 0) {
                //Si existe, usar su nombre
                nombreEstadio = estadio.getNombre();
            }
        } catch (ServiceException e) {
            //Si hay error al consultar, usar el valor por defecto
            //No mostramos error al usuario porque no es crítico
        }

        //PASO 2: Obtener la URL de la foto
        String fotoUrl = ubicacion.getFotoUrl();

        //Si no tiene foto, mostrar texto indicativo
        if (fotoUrl == null || fotoUrl.trim().isEmpty()) {
            fotoUrl = "(Sin foto)";
        }

        //PASO 3: crear el array con los datos de la ubicación
        //IMPORTANTE: el orden debe ser igual al de obtenerNombresColumnas()
        return new Object[]{
                ubicacion.getIdUbicacion(),    // columna 0: "ID Ubicación"
                nombreEstadio,                 // columna 1: "Estadio"
                ubicacion.getNombre(),         // columna 2: "Nombre"
                "$" + ubicacion.getPrecio(),   // columna 3: "Precio"
                ubicacion.getCapacidad(),      // columna 4: "Capacidad"
                fotoUrl                        // columna 5: "Foto" ← NUEVO
        };
    }


    //METODO 5 - getMensajeSinElementos() -----
    @Override
    public String getMensajeSinElementos() {
        return "No hay ubicaciones registradas";
    }


    //METODO 6 - getCodigoMenuPrincipal() -----
    //Devuelve el código del menú al que debe volver
    @Override
    public int getCodigoMenuPrincipal() {
        return 30; //código 30 = MenuGestionUbicaciones
    }
}