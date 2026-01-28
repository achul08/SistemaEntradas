import entidades.Usuario;
import gui.PanelManager;
import service.ServiceUsuario;
import service.ServiceException;
import entidades.Administrador;
import entidades.Vendedor;
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

        //Cargar usuarios precargados
        cargarUsuariosPrecargados();


        SwingUtilities.invokeLater(new Runnable() { //SwingUtilities.invokeLater() es una buena práctica en Swing
            // Asegura que todos los componentes gráficos se creen en el hilo correcto
            @Override
            public void run() {
                try {
                    // CREAR EL PANEL MANAGER
                    // Le pasamos 1 = código del MenuPrincipal
                    // Esto hace que al arrancar el programa, se muestre el menú
                    PanelManager panelManager = new PanelManager(0);

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
               private static void cargarUsuariosPrecargados() {
                    ServiceUsuario serviceUsuario = new ServiceUsuario();

            try {
                System.out.println("===========================================");
                System.out.println("Verificando usuarios precargados...");
                System.out.println("===========================================");


                //USUARIO 1: ADMINISTRADOR
                boolean adminExiste = false;
                try {
                    Usuario usuarioExistente = serviceUsuario.consultar(1);
                    if(usuarioExistente != null && usuarioExistente.getIdUsuario() != 0) {
                        adminExiste = true;
                        System.out.println("✓ Administrador ya existe (ID: 1)");
                    }
                } catch (Exception e) {}

                if(!adminExiste) {
                    Administrador admin = new Administrador("Azu", "M.", "admin", "admin123");
                    serviceUsuario.insertar(admin);
                    System.out.println("✓ Administrador creado");
                }


                //USUARIO 2: VENDEDOR 1
                boolean vendedor1Existe = false;
                try {
                    Usuario usuarioExistente = serviceUsuario.consultar(2);
                    if(usuarioExistente != null && usuarioExistente.getIdUsuario() != 0) {
                        vendedor1Existe = true;
                        System.out.println("✓ Vendedor 1 ya existe (ID: 2)");
                    }
                } catch (Exception e) {}

                if(!vendedor1Existe) {
                    Vendedor vendedor1 = new Vendedor("Orne", "C.", "vendedor1", "vend123");
                    serviceUsuario.insertar(vendedor1);
                    System.out.println("✓ Vendedor 1 creado");
                }


                //USUARIO 3: VENDEDOR 2
                boolean vendedor2Existe = false;
                try {
                    Usuario usuarioExistente = serviceUsuario.consultar(3);
                    if(usuarioExistente != null && usuarioExistente.getIdUsuario() != 0) {
                        vendedor2Existe = true;
                        System.out.println("✓ Vendedor 2 ya existe (ID: 3)");
                    }
                } catch (Exception e) {}

                if(!vendedor2Existe) {
                    Vendedor vendedor2 = new Vendedor("Facu", "M.", "vendedor2", "vend123");
                    serviceUsuario.insertar(vendedor2);
                    System.out.println("✓ Vendedor 2 creado");
                }


                System.out.println("\n===========================================");
                System.out.println("USUARIOS DISPONIBLES PARA LOGIN:");
                System.out.println("===========================================");
                System.out.println("ADMINISTRADOR:");
                System.out.println("  Username: admin");
                System.out.println("  Password: admin123");
                System.out.println("");
                System.out.println("VENDEDORES:");
                System.out.println("  Username: vendedor1  Password: vend123");
                System.out.println("  Username: vendedor2  Password: vend123");
                System.out.println("===========================================\n");


            } catch (ServiceException e) {
                System.err.println("ERROR al cargar usuarios: " + e.getMessage());
                e.printStackTrace();
            }
        }



    }





    //Tener H2 CERRADA para que funcione esto !!!!
    // Entidad con atributos, constructores, getters/setters
    // DAO que implementa IDAO con los 5 métodos
    // Service con al menos el método insertar

