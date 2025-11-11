package dao;

//pueden generarse problemas externos cuando accedo a la base de datos asi que lanzo una excepcion de tipo dao
//un exception general para todos, no vamos a hacer un dao exception por entidad

public class DaoException extends Exception {
    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }
}
