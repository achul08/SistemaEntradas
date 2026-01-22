package dao;

import entidades.Ubicacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DaoUbicacion implements IDAO<Ubicacion>{
    private String DB_JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL = "jdbc:h2:~/test";
    private String DB_USER = "azu";
    private String DB_PASSWORD = "";


    @Override
    public void insertar(Ubicacion elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            System.out.println("Cargó el driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Se conectó a la base de datos");

            //MODIFICADO: Ahora incluye foto_url en el INSERT
            //Antes era: INSERT INTO UBICACION (id_estadio, nombre, precio, capacidad) VALUES (?,?,?,?)
            //Ahora es: INSERT INTO UBICACION (id_estadio, nombre, precio, capacidad, foto_url) VALUES (?,?,?,?,?)
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO UBICACION (id_estadio, nombre, precio, capacidad, foto_url) VALUES (?,?,?,?,?)"
            );

            //PASO 1: Setear el ID del estadio (parámetro 1)
            preparedStatement.setInt(1, elemento.getIdEstadio());

            //PASO 2: Setear el nombre (parámetro 2)
            preparedStatement.setString(2, elemento.getNombre());

            //PASO 3: Setear el precio (parámetro 3)
            preparedStatement.setDouble(3, elemento.getPrecio());

            //PASO 4: Setear la capacidad (parámetro 4)
            preparedStatement.setInt(4, elemento.getCapacidad());

            //PASO 5: Setear la foto URL (parámetro 5) ← NUEVO
            //Si elemento.getFotoUrl() es null, guarda NULL en la BD
            //Si tiene valor, guarda ese valor
            preparedStatement.setString(5, elemento.getFotoUrl());

            //Ejecutar el INSERT
            int resultado = preparedStatement.executeUpdate();
            System.out.println("Se insertó correctamente. Filas afectadas: " + resultado);
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw new DaoException("Fallo la base de datos");
        }
    }

    @Override
    public void modificar(Ubicacion elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);


            preparedStatement = connection.prepareStatement(
                    "UPDATE UBICACION SET id_estadio=?, nombre=?, precio=?, capacidad=?, foto_url=? WHERE id_ubicacion=?"
            );

            preparedStatement.setInt(1, elemento.getIdEstadio());
            preparedStatement.setString(2, elemento.getNombre());
            preparedStatement.setDouble(3, elemento.getPrecio());
            preparedStatement.setInt(4, elemento.getCapacidad());

            preparedStatement.setString(5, elemento.getFotoUrl());
            preparedStatement.setInt(6, elemento.getIdUbicacion());
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
            preparedStatement = connection.prepareStatement("DELETE FROM UBICACION WHERE id_ubicacion=?");

            preparedStatement.setInt(1, id);

            int resultado = preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Fallo la base de datos");
        }
    }


    @Override
    public Ubicacion consultar(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Ubicacion ubicacion = new Ubicacion();
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM UBICACION WHERE id_ubicacion=?");
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                ubicacion.setIdUbicacion(rs.getInt("id_ubicacion"));
                ubicacion.setIdEstadio(rs.getInt("id_estadio"));
                ubicacion.setNombre(rs.getString("nombre"));
                ubicacion.setPrecio(rs.getDouble("precio"));
                ubicacion.setCapacidad(rs.getInt("capacidad"));
                ubicacion.setFotoUrl(rs.getString("foto_url"));
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        return ubicacion;
    }


    @Override
    public List<Ubicacion> consultarTodos() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Ubicacion> ubicaciones = new ArrayList<>();
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM UBICACION");

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setIdUbicacion(rs.getInt("id_ubicacion"));
                ubicacion.setIdEstadio(rs.getInt("id_estadio"));
                ubicacion.setNombre(rs.getString("nombre"));
                ubicacion.setPrecio(rs.getDouble("precio"));
                ubicacion.setCapacidad(rs.getInt("capacidad"));
                ubicacion.setFotoUrl(rs.getString("foto_url"));
                ubicaciones.add(ubicacion);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        return ubicaciones;
}
}
