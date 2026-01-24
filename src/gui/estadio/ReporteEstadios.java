package gui.estadio;

import entidades.Estadio;
import gui.PanelManager;
import gui.base.ReporteBase;
import service.ServiceEstadio;
import service.ServiceException;

import java.util.List;

public class ReporteEstadios extends ReporteBase {
    private ServiceEstadio serviceEstadio = new ServiceEstadio();

    public ReporteEstadios(PanelManager panelManager) {
        super(panelManager);
        inicializar();
    }

    @Override
    public String getTitulo() {
        return "REPORTE DE ESTADIOS";
    }

    @Override
    public String[] obtenerNombresColumnas() {
        return new String[]{
                "ID Estadio",
                "Nombre",
                "Dirección",
                "Capacidad Total"
        };
    }

    @Override
    public List<?> consultarTodos() throws ServiceException {
        return serviceEstadio.consultarTodos();
    }

    @Override
    public Object[] convertirElementoAFila(Object elemento) {
        Estadio estadio = (Estadio) elemento;

        return new Object[]{
                estadio.getIdEstadio(),
                estadio.getNombre(),
                estadio.getDireccion(),
                estadio.getCapacidadTotal()
        };
    }

    @Override
    public String getMensajeSinElementos() {
        return "No hay estadios registrados";
    }

    @Override
    public int getCodigoMenuPrincipal() {
        return 1;
    }
}