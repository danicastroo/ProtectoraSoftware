package github.danicastroo.model.interfaces;

import github.danicastroo.model.entity.Trabajador;

import java.sql.SQLException;

public interface InterfaceTrabajadorDAO<T> extends DAO<T>{
    Trabajador findByUsername(String username) throws SQLException;
    String checkLogin(String username, String password) throws SQLException;
}
