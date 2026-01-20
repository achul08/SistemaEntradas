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

            //MODIFICADO: Ahora incluye foto_url en el INSERT
            //Antes era: INSERT INTO ESTADIO (nombre, direccion, capacidad_total) VALUES (?,?,?)
            //Ahora es: INSERT INTO ESTADIO (nombre, direccion, capacidad_total, foto_url) VALUES (?,?,?,?)
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO ESTADIO (nombre, direccion, capacidad_total, foto_url) VALUES (?,?,?,?)"
            );

            //PASO 1: Setear el nombre (parámetro 1)
            preparedStatement.setString(1, elemento.getNombre());

            //PASO 2: Setear la dirección (parámetro 2)
            preparedStatement.setString(2, elemento.getDireccion());

            //PASO 3: Setear la capacidad total (parámetro 3)
            preparedStatement.setInt(3, elemento.getCapacidadTotal());

            //PASO 4: Setear la foto URL (parámetro 4) ← NUEVO
            //Si elemento.getFotoUrl() es null, guarda NULL en la BD
            //Si tiene valor, guarda ese valor
            preparedStatement.setString(4, elemento.getFotoUrl());

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
    public void modificar(Estadio elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            //MODIFICADO: Ahora incluye foto_url en el UPDATE
            //Antes era: UPDATE ESTADIO SET nombre=?, direccion=?, capacidad_total=? WHERE id_estadio=?
            //Ahora es: UPDATE ESTADIO SET nombre=?, direccion=?, capacidad_total=?, foto_url=? WHERE id_estadio=?
            preparedStatement = connection.prepareStatement(
                    "UPDATE ESTADIO SET nombre=?, direccion=?, capacidad_total=?, foto_url=? WHERE id_estadio=?"
            );

            //PASO 1: Setear el nombre (parámetro 1)
            preparedStatement.setString(1, elemento.getNombre());

            //PASO 2: Setear la dirección (parámetro 2)
            preparedStatement.setString(2, elemento.getDireccion());

            //PASO 3: Setear la capacidad total (parámetro 3)
            preparedStatement.setInt(3, elemento.getCapacidadTotal());

            //PASO 4: Setear la foto URL (parámetro 4) ← NUEVO
            //Si elemento.getFotoUrl() es null, actualiza a NULL en la BD
            //Si tiene valor, actualiza a ese valor
            preparedStatement.setString(4, elemento.getFotoUrl());

            //PASO 5: Setear el ID del estadio (parámetro 5)
            //Este es el WHERE, indica QUÉ estadio modificar
            preparedStatement.setInt(5, elemento.getIdEstadio());

            //Ejecutar el UPDATE
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
                estadio.setFotoUrl(rs.getString("foto_url"));
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
                estadio.setFotoUrl(rs.getString("foto_url"));
                estadios.add(estadio);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        return estadios;
    }
}
