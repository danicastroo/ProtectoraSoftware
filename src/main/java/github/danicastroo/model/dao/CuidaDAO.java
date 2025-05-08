package github.danicastroo.model.dao;

import github.danicastroo.model.entity.Cuida;
import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.interfaces.DAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuidaDAO implements DAO<Cuida> {

    @Override
    public Cuida save(Cuida cuida) throws SQLException {
        String query = "INSERT INTO cuida (idTrabajador, idAnimal, observaciones, tipo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, cuida.getIdTrabajador());
            stmt.setInt(2, cuida.getIdAnimal());
            stmt.setString(3, cuida.getObservaciones());
            stmt.setString(4, cuida.getTipo());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                // Si necesitas el ID generado, puedes asignarlo aquí
                // cuida.setIdCuida(rs.getInt(1));
            }
        }
        return cuida;
    }

    @Override
    public Cuida delete(Cuida cuida) throws SQLException {
        String query = "DELETE FROM cuida WHERE idCuida = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, cuida.getIdCuida());
            stmt.executeUpdate();
        }
        return cuida;
    }

    @Override
    public Cuida findById(int id) {
        String query = "SELECT * FROM cuida WHERE idCuida = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCuida(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cuida> findAll() {
        List<Cuida> cuidas = new ArrayList<>();
        String query = "SELECT * FROM cuida";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cuidas.add(mapResultSetToCuida(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cuidas;
    }

    private Cuida mapResultSetToCuida(ResultSet rs) throws SQLException {
        return new Cuida(
                rs.getInt("idAnimal"),
                rs.getInt("idTrabajador"),
                rs.getString("observaciones"),
                rs.getString("tipo")
        );
    }

    @Override
    public void close() throws IOException {
        // No resources to close
    }
}