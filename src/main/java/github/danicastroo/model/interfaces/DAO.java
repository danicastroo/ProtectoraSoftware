package github.danicastroo.model.interfaces;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

public interface DAO <T> extends Closeable {

    /**
     * Guarda un objeto en la base de datos.
     *
     * @param entity el objeto a guardar
     * @return el objeto guardado
     * @throws SQLException si ocurre un error en la base de datos
     */
    T save(T entity) throws SQLException;
    T delete(T entity) throws SQLException;
    T findById(int key) throws SQLException;
    List<T> findAll() throws SQLException;
}
