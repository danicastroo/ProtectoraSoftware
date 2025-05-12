package github.danicastroo.model.interfaces;

import github.danicastroo.model.entity.Adoptante;

import java.sql.SQLException;

public interface InterfaceAdoptanteDAO<T> extends DAO<T> {
    Adoptante findByEmail(String email) throws SQLException;
}
