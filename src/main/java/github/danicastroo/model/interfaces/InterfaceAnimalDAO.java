package github.danicastroo.model.interfaces;


import java.sql.SQLException;

public interface InterfaceAnimalDAO<T> extends DAO<T>{
    void setAnimal(T entity) throws SQLException;
    T findByNickname(String name);
    void update(T entity);
}
