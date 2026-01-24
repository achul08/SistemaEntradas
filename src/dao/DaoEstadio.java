package dao;

//un dao por cada entidad
//PRIMER ENTIDAD HECHA !!!!

import entidades.Estadio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DaoEstadio implements IDAO<Estadio>{
    private String DB_JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL = "jdbc:h2:~/test";
    private String DB_USER = "azu";
    private String DB_PASSWORD = "";


    @Override
    public void insertar(Estadio elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            System.out.println("Cargó el driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Se conectó a la base de datos");
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO ESTADIO (nombre, direccion, capacidad_total) VALUES (?,?,?)"
            );
            preparedStatement.setString(1, elemento.getNombre());
            preparedStatement.setString(2, elemento.getDireccion());
            preparedStatement.setInt(3, elemento.getCapacidadTotal());

            int resultado = preparedStatement.executeUpdate();
            System.out.println("Se insertó correctamente. Filas afectadas: " + resultado);
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw new DaoException("Fallo la base de datos");
        }
    }

    @Override
    public void modificar(Estadio elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            preparedStatement = connection.prepareStatement(
                    "UPDATE ESTADIO SET nombre=?, direccion=?, capacidad_total=? WHERE id_estadio=?"
            );

            preparedStatement.setString(1, elemento.getNombre());
            preparedStatement.setString(2, elemento.getDireccion());
            preparedStatement.setInt(3, elemento.getCapacidadTotal());
            preparedStatement.setInt(4, elemento.getIdEstadio());

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
            preparedStatement = connection.prepareStatement("DELETE FROM ESTADIO WHERE id_estadio=?");

            preparedStatement.setInt(1, id);

            int resultado = preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Fallo la base de datos");
        }
    }


    @Override
    public Estadio consultar(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Estadio estadio = new Estadio();
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM ESTADIO WHERE id_estadio=?");
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                estadio.setIdEstadio(rs.getInt("id_estadio"));
                estadio.setNombre(rs.getString("nombre"));
                estadio.setDireccion(rs.getString("direccion"));
                estadio.setCapacidadTotal(rs.getInt("capacidad_total"));
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        return estadio;
    }


    @Override
    public List<Estadio> consultarTodos() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Estadio> estadios = new ArrayList<>();
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM ESTADIO");

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Estadio estadio = new Estadio();
                estadio.setIdEstadio(rs.getInt("id_estadio"));
                estadio.setNombre(rs.getString("nombre"));
                estadio.setDireccion(rs.getString("direccion"));
                estadio.setCapacidadTotal(rs.getInt("capacidad_total"));
                estadios.add(estadio);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        return estadios;
    }
}
