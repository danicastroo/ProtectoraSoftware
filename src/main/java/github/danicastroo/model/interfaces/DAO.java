package github.danicastroo.model.interfaces;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

public interface DAO <T> extends Closeable {

    T save(T entity) throws SQLException;
    T delete(T entity) throws SQLException;
    T findById(int key);
    List<T> findAll();
}
