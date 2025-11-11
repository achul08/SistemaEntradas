package dao;
import java.util.List;

public interface IDAO<T> {
    public void insertar(T elemento) throws DaoException;
    public void modificar(T elemento) throws DaoException;
    public void eliminar(int id) throws DaoException;
    public T consultar(int id) throws DaoException;
    public List<T> consultarTodos() throws DaoException;



}
