package gui.ubicacion;

import entidades.Ubicacion;
import entidades.Estadio;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceUbicacion;
import service.ServiceEstadio;
import service.ServiceException;

import java.util.List;

public class ReporteUbicaciones extends ReporteBase {
    private ServiceUbicacion serviceUbicacion = new ServiceUbicacion();
    private ServiceEstadio serviceEstadio = new ServiceEstadio();

    public ReporteUbicaciones(PanelManager panelManager) {
        super(panelManager);
        inicializar();
    }

    @Override
    public String getTitulo() {
        return "REPORTE DE UBICACIONES";
    }

    @Override
    public String[] obtenerNombresColumnas() {
        return new String[]{
                "ID Ubicación",
                "Estadio",
                "Nombre",
                "Precio",
                "Capacidad"
        };
    }

    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceUbicacion.consultarTodos();
    }

    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Ubicacion ubicacion = (Ubicacion) elemento;

        String nombreEstadio = "(Desconocido)";
        try {
            Estadio estadio = serviceEstadio.consultar(ubicacion.getIdEstadio());
            if(estadio != null && estadio.getIdEstadio() != 0) {
                nombreEstadio = estadio.getNombre();
            }
        } catch (ServiceException e) {
        }

        return new Object[]{
                ubicacion.getIdUbicacion(),
                nombreEstadio,
                ubicacion.getNombre(),
                "$" + ubicacion.getPrecio(),
                ubicacion.getCapacidad()
        };
    }

    @Override
    public String getMensajeSinElementos() {
        return "No hay ubicaciones registradas";
    }

    @Override
    public int getCodigoMenuPrincipal() {
        return 30;
    }
}