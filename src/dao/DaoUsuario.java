package dao;

//trabaja con herencia
//devuelve objetos admin o vend según el rol en la BD

import entidades.Usuario;
import entidades.Administrador;
import entidades.Vendedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DaoUsuario implements IDAO<Usuario>{
    private String DB_JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL = "jdbc:h2:~/test";
    private String DB_USER = "azu";
    private String DB_PASSWORD = "";


    @Override
    public void insertar(Usuario elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            System.out.println("Cargó el driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Se conectó a la base de datos");

            //INSERTAR con el rol que devuelve getRol() de la clase hija
            preparedStatement = connection.prepareStatement("INSERT INTO USUARIO (nombre, apellido, username, password, rol) VALUES (?,?,?,?,?)");

            preparedStatement.setString(1, elemento.getNombre());
            preparedStatement.setString(2, elemento.getApellido());
            preparedStatement.setString(3, elemento.getUsername());
            preparedStatement.setString(4, elemento.getPassword());
            preparedStatement.setString(5, elemento.getRol()); //llama a getRol() de admin o vend

            int resultado = preparedStatement.executeUpdate();
            System.out.println("Se insertó correctamente. Filas afectadas: " + resultado);
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw new DaoException("Fallo la base de datos");
        }
    }

    @Override
    public void modificar(Usuario elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            //MODIFICAR con el rol que devuelve getRol() de la clase hija
            preparedStatement = connection.prepareStatement("UPDATE USUARIO SET nombre=?, apellido=?, username=?, password=?, rol=? WHERE id_usuario=?");

            preparedStatement.setString(1, elemento.getNombre());
            preparedStatement.setString(2, elemento.getApellido());
            preparedStatement.setString(3, elemento.getUsername());
            preparedStatement.setString(4, elemento.getPassword());
            preparedStatement.setString(5, elemento.getRol()); //llama a getRol() de admin o vend
            preparedStatement.setInt(6, elemento.getIdUsuario());

            int resultado = preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Fallo la base de datos");
        }
    }


    @Override
    public void eliminar(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("DELETE FROM USUARIO WHERE id_usuario=?");

            preparedStatement.setInt(1, id);

            int resultado = preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Fallo la base de datos");
        }
    }


    @Override
    public Usuario consultar(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Usuario usuario = null; //empezamos con null

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM USUARIO WHERE id_usuario=?");
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                //LEER EL ROL DE LA BD
                String rol = rs.getString("rol");

                //CREAR EL OBJETO CORRECTO SEGÚN EL ROL
                if (rol.equals("ADMINISTRADOR")) {
                    usuario = new Administrador(); //crear un Administrador
                } else if (rol.equals("VENDEDOR")) {
                    usuario = new Vendedor(); //crear un Vendedor
                } else {
                    //si el rol no es ninguno de los dos, crear un Vendedor por defecto
                    usuario = new Vendedor();
                }

                //LLENAR LOS DATOS (igual para ambos porque heredan de Usuario)
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                //NO seteamos el rol porque getRol() ya devuelve el correcto
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }

        //si no encuentra nada, devuelve null
        //si encuentra, devuelve el Administrador o Vendedor creado
        return usuario;
    }


    @Override
    public List<Usuario> consultarTodos() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM USUARIO");

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                //LEER EL ROL DE LA BD
                String rol = rs.getString("rol");

                //CREAR EL OBJETO CORRECTO SEGÚN EL ROL
                Usuario usuario;
                if (rol.equals("ADMINISTRADOR")) {
                    usuario = new Administrador(); //crear un Administrador
                } else if (rol.equals("VENDEDOR")) {
                    usuario = new Vendedor(); //crear un Vendedor
                } else {
                    //si el rol no es ninguno de los dos, crear un Vendedor por defecto
                    usuario = new Vendedor();
                }

                //LLENAR LOS DATOS (igual para ambos porque heredan de Usuario)
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                //NO seteamos el rol porque getRol() ya devuelve el correcto

                //agregar a la lista
                usuarios.add(usuario);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        return usuarios;
    }
}