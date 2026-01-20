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


    //═════════════════════════════════════════════════════════════════════
    // MÉTODO NUEVO: CONFIGURAR RENDERER PARA FOTOS
    //═════════════════════════════════════════════════════════════════════
    /*
     * ¿Qué hace este método?
     * Por defecto, JTable muestra TODO como texto.
     * Este método le dice a la tabla: "En la columna 5, en lugar de mostrar texto,
     * mostrá una imagen cargada desde la URL"
     *
     * Es como decirle a Excel: "Esta columna no son números, son imágenes"
     */
    private void configurarRendererFotos() {
        //getTable() es un método de ReporteBase que devuelve la JTable
        //setDefaultRenderer() le dice a la tabla cómo dibujar las celdas
        //Object.class significa "esto aplica a todas las columnas"
        getTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            //Este método se ejecuta CADA VEZ que la tabla dibuja una celda
            //Parámetros:
            // - table: la tabla completa
            // - value: el valor de esta celda (puede ser texto, número, URL, etc.)
            // - isSelected: true si el usuario seleccionó esta fila
            // - hasFocus: true si esta celda tiene el foco del teclado
            // - row: número de fila (0, 1, 2, ...)
            // - column: número de columna (0, 1, 2, ...)
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                //PREGUNTA: ¿Esta es la columna 5 (Foto)?
                //column == 5 → columna "Foto"
                //value != null → hay algo en esta celda
                //!value.toString().isEmpty() → no es un texto vacío
                if (column == 5 && value != null && !value.toString().isEmpty()) {
                    //═════════════════════════════════════════════════════════
                    // SÍ ES LA COLUMNA DE FOTO → Mostrar imagen
                    //═════════════════════════════════════════════════════════

                    //Crear un JLabel (etiqueta) para mostrar la imagen
                    //Un JLabel puede mostrar texto O imágenes
                    JLabel label = new JLabel();

                    //Centrar la imagen dentro del JLabel
                    label.setHorizontalAlignment(JLabel.CENTER);

                    try {
                        //PASO 1: Obtener la URL como String
                        //value es un Object, lo convertimos a String
                        String urlString = value.toString();
                        //Ejemplo: "https://ejemplo.com/foto-platea.jpg"

                        //PASO 2: Crear un objeto URL de Java
                        //Este objeto sabe cómo conectarse a internet y descargar la imagen
                        URL url = new URL(urlString);

                        //PASO 3: Crear un ImageIcon a partir de la URL
                        //ImageIcon descarga la imagen de internet automáticamente
                        ImageIcon icon = new ImageIcon(url);

                        //PASO 4: Obtener la imagen "cruda" del ImageIcon
                        //Esto nos da acceso a la imagen en memoria
                        Image img = icon.getImage();

                        //PASO 5: Redimensionar la imagen a un tamaño fijo
                        //100x75 píxeles (ancho x alto)
                        //Image.SCALE_SMOOTH hace que la imagen se vea bien al redimensionar
                        //(sin píxeles dentados)
                        Image imgEscalada = img.getScaledInstance(100, 75, Image.SCALE_SMOOTH);

                        //PASO 6: Crear un nuevo ImageIcon con la imagen redimensionada
                        ImageIcon iconEscalado = new ImageIcon(imgEscalada);

                        //PASO 7: Poner el icono (imagen) en el JLabel
                        label.setIcon(iconEscalado);

                    } catch (Exception e) {
                        //Si algo salió mal (URL inválida, sin internet, etc.)
                        //mostrar un mensaje de error en lugar de la imagen
                        label.setText("(Error al cargar foto)");
                    }

                    //PASO 8: Configurar colores según si está seleccionado
                    if (isSelected) {
                        //Si el usuario seleccionó esta fila, usar colores de selección
                        label.setBackground(table.getSelectionBackground()); //fondo azul
                        label.setForeground(table.getSelectionForeground()); //texto blanco
                    } else {
                        //Si no está seleccionado, usar colores normales
                        label.setBackground(table.getBackground()); //fondo blanco
                        label.setForeground(table.getForeground()); //texto negro
                    }

                    //setOpaque(true) hace que el fondo del JLabel sea visible
                    //(si no, sería transparente)
                    label.setOpaque(true);

                    //PASO 9: Devolver el JLabel con la imagen
                    //Esto es lo que se va a dibujar en la celda
                    return label;

                } else {
                    //═════════════════════════════════════════════════════════
                    // NO ES LA COLUMNA DE FOTO → Mostrar texto normal
                    //═════════════════════════════════════════════════════════

                    //super.getTableCellRendererComponent() llama al método original
                    //que dibuja texto normal (como antes)
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
            }
        });

        //IMPORTANTE: Aumentar la altura de las filas para que se vean las fotos
        //Por defecto, las filas tienen ~20 píxeles de alto
        //Con 80 píxeles, las fotos de 75px se ven completas
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