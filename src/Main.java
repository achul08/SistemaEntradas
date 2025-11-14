import entidades.Estadio;
import gui.MenuPrincipal;
import service.ServiceEstadio;
import service.ServiceException;
import dao.DaoEstadio;
import dao.DaoException;
import gui.PanelManager;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
       /* Estadio estadio = new Estadio("Estadio Luna Park", "Av. Eduardo Madero 470", 8400);
        ServiceEstadio serviceEstadio = new ServiceEstadio();

        try {
            serviceEstadio.insertar(estadio);
            System.out.println("Estadio insertado correctamente");
        }
        catch (ServiceException e) {
            System.out.println("Error: " + e.getMessage());
        }
*/


        SwingUtilities.invokeLater(new Runnable() { //SwingUtilities.invokeLater() es una buena práctica en Swing
            // Asegura que todos los componentes gráficos se creen en el hilo correcto
            @Override
            public void run() {
                try {
                    // CREAR EL PANEL MANAGER
                    // Le pasamos 1 = código del MenuPrincipal
                    // Esto hace que al arrancar el programa, se muestre el menú
                    PanelManager panelManager = new PanelManager(1);

                    System.out.println("Sistema de gestion de estadios iniciado correctamente");

                } catch (Exception e) {
                    // Si algo falla al iniciar, error
                    System.err.println("ERROR AL INICIAR EL SISTEMA:");
                    System.err.println(e.getMessage());
                    e.printStackTrace();

                    // mensaje visual
                    JOptionPane.showMessageDialog(
                            null,
                            "Error al iniciar el sistema:\n" + e.getMessage() +
                                    "\n\nVerifique que la base de datos H2 esté CERRADA",
                            "Error de inicio",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });



    }





    //Tener H2 CERRADA para que funcione esto !!!!
    // Entidad con atributos, constructores, getters/setters
    // DAO que implementa IDAO con los 5 métodos
    // Service con al menos el método insertar

}