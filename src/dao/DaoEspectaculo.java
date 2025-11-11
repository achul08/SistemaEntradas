package dao;

//un dao por cada entidad

import entidades.Espectaculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DaoEspectaculo implements IDAO<Espectaculo>{
    private String DB_JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL = "jdbc:h2:~/test";
    private String DB_USER = "azu";
    private String DB_PASSWORD = "";


    @Override
    public void insertar(Espectaculo elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            System.out.println("Cargó el driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Se conectó a la base de datos");
            preparedStatement = connection.prepareStatement("INSERT INTO ESPECTACULO (nombre, fecha, id_estadio, activo) VALUES (?,?,?,?)");

            preparedStatement.setString(1, elemento.getNombre());
            preparedStatement.setDate(2, new java.sql.Date(elemento.getFecha().getTime())); // Convierte java.util.Date a java.sql.Date (que necesita H2) usando milisegundos como intermediario
            /*
            getFecha() da java.util.Date, pero H2 necesita java.sql.Date.
            El .getTime() saca los milisegundos de la fecha, y new java.sql.Date()
            crea el tipo correcto usando esos milisegundos.
            */
            preparedStatement.setInt(3, elemento.getIdEstadio());
            preparedStatement.setBoolean(4, elemento.isActivo());

            int resultado = preparedStatement.executeUpdate();
            System.out.println("Se insertó correctamente. Filas afectadas: " + resultado);
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw new DaoException("Fallo la base de datos");
        }
    }

    @Override
    public void modificar(Espectaculo elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("UPDATE ESPECTACULO SET nombre=?, fecha=?, id_estadio=?, activo = ? WHERE id_espectaculo=?");

            preparedStatement.setString(1, elemento.getNombre());
            preparedStatement.setDate(2, new java.sql.Date(elemento.getFecha().getTime()));
            preparedStatement.setInt(3, elemento.getIdEstadio());
            preparedStatement.setBoolean(4, elemento.isActivo());
            preparedStatement.setInt(5, elemento.getIdEspectaculo());

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
            preparedStatement = connection.prepareStatement("DELETE FROM ESPECTACULO WHERE id_espectaculo=?");

            preparedStatement.setInt(1, id);

            int resultado = preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Fallo la base de datos");
        }
    }


    @Override
    public Espectaculo consultar(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
            Espectaculo espectaculo = new Espectaculo();
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM ESPECTACULO WHERE id_espectaculo=?");
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                espectaculo.setIdEspectaculo(rs.getInt("id_espectaculo"));
                espectaculo.setNombre(rs.getString("nombre"));
                espectaculo.setFecha(rs.getDate("fecha"));
                espectaculo.setIdEstadio(rs.getInt("id_estadio"));
                espectaculo.setActivo(rs.getBoolean("activo"));
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        return espectaculo;
    }


    @Override
    public List<Espectaculo> consultarTodos() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
            List<Espectaculo> espectaculos = new ArrayList<>();
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                preparedStatement = connection.prepareStatement("SELECT * FROM ESPECTACULO");

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Espectaculo espectaculo = new Espectaculo();
                espectaculo.setIdEspectaculo(rs.getInt("id_espectaculo"));
                espectaculo.setNombre(rs.getString("nombre"));
                espectaculo.setFecha(rs.getDate("fecha"));
                espectaculo.setIdEstadio(rs.getInt("id_estadio"));
                espectaculo.setActivo(rs.getBoolean("activo"));
                espectaculos.add(espectaculo);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        return espectaculos;
    }
}
