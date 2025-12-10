package dao;

//DAO VENTA - implementa IVentaDAO
//Maneja todas las operaciones de base de datos para Venta

import entidades.Venta;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DaoVenta implements IVentaDAO {
    private String DB_JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL = "jdbc:h2:~/test";
    private String DB_USER = "azu";
    private String DB_PASSWORD = "";


    //═════════════════════════════════════════════════════════════════════
    // MÉTODOS BÁSICOS DE IDAO (heredados)
    //═════════════════════════════════════════════════════════════════════

    @Override
    public void insertar(Venta elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            System.out.println("Cargó el driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Se conectó a la base de datos");
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO VENTA (id_espectaculo, id_ubicacion, id_vendedor, fecha_venta, precio_final, nombre_cliente, dni_cliente) VALUES (?,?,?,?,?,?,?)"
            );

            preparedStatement.setInt(1, elemento.getIdEspectaculo());
            preparedStatement.setInt(2, elemento.getIdUbicacion());
            preparedStatement.setInt(3, elemento.getIdVendedor());
            preparedStatement.setTimestamp(4, elemento.getFechaVenta());
            preparedStatement.setDouble(5, elemento.getPrecioFinal());
            preparedStatement.setString(6, elemento.getNombreCliente());
            preparedStatement.setString(7, elemento.getDniCliente());

            int resultado = preparedStatement.executeUpdate();
            System.out.println("Se insertó correctamente. Filas afectadas: " + resultado);
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw new DaoException("Fallo la base de datos");
        }
        finally {
            //cerrar recursos para evitar memory leaks
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }


    @Override
    public void modificar(Venta elemento) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement(
                    "UPDATE VENTA SET id_espectaculo=?, id_ubicacion=?, id_vendedor=?, fecha_venta=?, precio_final=?, nombre_cliente=?, dni_cliente=? WHERE id_venta=?"
            );

            preparedStatement.setInt(1, elemento.getIdEspectaculo());
            preparedStatement.setInt(2, elemento.getIdUbicacion());
            preparedStatement.setInt(3, elemento.getIdVendedor());
            preparedStatement.setTimestamp(4, elemento.getFechaVenta());
            preparedStatement.setDouble(5, elemento.getPrecioFinal());
            preparedStatement.setString(6, elemento.getNombreCliente());
            preparedStatement.setString(7, elemento.getDniCliente());
            preparedStatement.setInt(8, elemento.getIdVenta());

            int resultado = preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Fallo la base de datos");
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }


    @Override
    public void eliminar(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("DELETE FROM VENTA WHERE id_venta=?");

            preparedStatement.setInt(1, id);

            int resultado = preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Fallo la base de datos");
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }


    @Override
    public Venta consultar(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Venta venta = new Venta();
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM VENTA WHERE id_venta=?");
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setIdEspectaculo(rs.getInt("id_espectaculo"));
                venta.setIdUbicacion(rs.getInt("id_ubicacion"));
                venta.setIdVendedor(rs.getInt("id_vendedor"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                venta.setPrecioFinal(rs.getDouble("precio_final"));
                venta.setNombreCliente(rs.getString("nombre_cliente"));
                venta.setDniCliente(rs.getString("dni_cliente"));
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return venta;
    }


    @Override
    public List<Venta> consultarTodos() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Venta> ventas = new ArrayList<>();
        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM VENTA");

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setIdEspectaculo(rs.getInt("id_espectaculo"));
                venta.setIdUbicacion(rs.getInt("id_ubicacion"));
                venta.setIdVendedor(rs.getInt("id_vendedor"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                venta.setPrecioFinal(rs.getDouble("precio_final"));
                venta.setNombreCliente(rs.getString("nombre_cliente"));
                venta.setDniCliente(rs.getString("dni_cliente"));
                ventas.add(venta);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error en la consulta");
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return ventas;
    }


    //═════════════════════════════════════════════════════════════════════
    // MÉTODOS ESPECÍFICOS DE IVentaDAO
    //═════════════════════════════════════════════════════════════════════

    @Override
    public List<Venta> consultarPorVendedor(int idVendedor) throws DaoException {
        //busca todas las ventas donde id_vendedor = idVendedor
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Venta> ventas = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            //SQL con WHERE para filtrar por vendedor
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM VENTA WHERE id_vendedor = ?"
            );
            preparedStatement.setInt(1, idVendedor);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setIdEspectaculo(rs.getInt("id_espectaculo"));
                venta.setIdUbicacion(rs.getInt("id_ubicacion"));
                venta.setIdVendedor(rs.getInt("id_vendedor"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                venta.setPrecioFinal(rs.getDouble("precio_final"));
                venta.setNombreCliente(rs.getString("nombre_cliente"));
                venta.setDniCliente(rs.getString("dni_cliente"));
                ventas.add(venta);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error al consultar ventas por vendedor");
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return ventas;
    }


    @Override
    public List<Venta> consultarPorEspectaculo(int idEspectaculo) throws DaoException {
        //busca todas las ventas donde id_espectaculo = idEspectaculo
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Venta> ventas = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            //SQL con WHERE para filtrar por espectáculo
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM VENTA WHERE id_espectaculo = ?"
            );
            preparedStatement.setInt(1, idEspectaculo);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setIdEspectaculo(rs.getInt("id_espectaculo"));
                venta.setIdUbicacion(rs.getInt("id_ubicacion"));
                venta.setIdVendedor(rs.getInt("id_vendedor"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                venta.setPrecioFinal(rs.getDouble("precio_final"));
                venta.setNombreCliente(rs.getString("nombre_cliente"));
                venta.setDniCliente(rs.getString("dni_cliente"));
                ventas.add(venta);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error al consultar ventas por espectáculo");
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return ventas;
    }


    @Override
    public List<Venta> consultarPorFecha(Date fechaInicio, Date fechaFin) throws DaoException {
        //busca todas las ventas donde fecha_venta esté entre fechaInicio y fechaFin
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Venta> ventas = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            //SQL con BETWEEN para filtrar por rango de fechas
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM VENTA WHERE fecha_venta BETWEEN ? AND ?"
            );
            //convertir java.util.Date a java.sql.Timestamp
            preparedStatement.setTimestamp(1, new Timestamp(fechaInicio.getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(fechaFin.getTime()));

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setIdEspectaculo(rs.getInt("id_espectaculo"));
                venta.setIdUbicacion(rs.getInt("id_ubicacion"));
                venta.setIdVendedor(rs.getInt("id_vendedor"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                venta.setPrecioFinal(rs.getDouble("precio_final"));
                venta.setNombreCliente(rs.getString("nombre_cliente"));
                venta.setDniCliente(rs.getString("dni_cliente"));
                ventas.add(venta);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error al consultar ventas por fecha");
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return ventas;
    }


    @Override
    public List<Venta> consultarPorEspectaculoYFecha(int idEspectaculo, Date fechaInicio, Date fechaFin) throws DaoException {
        //busca ventas que cumplan AMBAS condiciones:
        //1. id_espectaculo = idEspectaculo
        //2. fecha_venta entre fechaInicio y fechaFin
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Venta> ventas = new ArrayList<>();

        try {
            Class.forName(DB_JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            //SQL con WHERE para filtrar por espectáculo Y rango de fechas
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM VENTA WHERE id_espectaculo = ? AND fecha_venta BETWEEN ? AND ?"
            );
            preparedStatement.setInt(1, idEspectaculo);
            preparedStatement.setTimestamp(2, new Timestamp(fechaInicio.getTime()));
            preparedStatement.setTimestamp(3, new Timestamp(fechaFin.getTime()));

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setIdEspectaculo(rs.getInt("id_espectaculo"));
                venta.setIdUbicacion(rs.getInt("id_ubicacion"));
                venta.setIdVendedor(rs.getInt("id_vendedor"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                venta.setPrecioFinal(rs.getDouble("precio_final"));
                venta.setNombreCliente(rs.getString("nombre_cliente"));
                venta.setDniCliente(rs.getString("dni_cliente"));
                ventas.add(venta);
            }
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new DaoException("Error al consultar ventas por espectáculo y fecha");
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return ventas;
    }
}