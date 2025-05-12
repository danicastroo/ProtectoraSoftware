package github.danicastroo.model.interfaces;

import github.danicastroo.model.entity.Adopta;

import java.sql.SQLException;
import java.util.List;

public interface AdoptaDAO {
    Adopta save(Adopta adopta) throws SQLException;
    Adopta delete(Adopta adopta) throws SQLException;
    Adopta findById(int id) throws SQLException;
    List<Adopta> findAll() throws SQLException;
}
