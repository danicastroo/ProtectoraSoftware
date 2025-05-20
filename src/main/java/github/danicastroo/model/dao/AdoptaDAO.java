package github.danicastroo.model.dao;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.entity.Adopta;
import github.danicastroo.model.interfaces.InterfaceAdoptaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AdoptaDAO implements InterfaceAdoptaDAO {

    private static final Logger logger = Logger.getLogger(AdoptaDAO.class.getName());

    @Override
    public Adopta save(Adopta adopta) throws SQLException {
        String query = "INSERT INTO adopta (idAdoptante, idAnimal, fechaAdopcion, observaciones) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, adopta.getIdAdoptante());
            stmt.setInt(2, adopta.getIdAnimal());
            stmt.setDate(3, Date.valueOf(adopta.getFechaAdopcion()));
            stmt.setString(4, adopta.getObservaciones());

            int info = stmt.executeUpdate();
            logger.info("AdoptaDAO.save: Filas insertadas = " + info);

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                adopta.setIdAdopta(rs.getInt(1)); // Asigna el ID generado
            }
        }
        return adopta;
    }

    @Override
    public Adopta delete(Adopta adopta) throws SQLException {
        String query = "DELETE FROM adopta WHERE idAdopta = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, adopta.getIdAdopta());
            int info = stmt.executeUpdate();
            logger.info("AdoptaDAO.delete: Filas eliminadas = " + info);
        } catch (SQLException e) {
            logger.severe("AdoptaDAO.delete: Error al eliminar adopta: " + e.getMessage());
            throw e;
        }
        return adopta;
    }



    @Override
    public Adopta findById(int id) throws SQLException {
        String query = "SELECT * FROM adopta WHERE idAdopta = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                logger.info("AdoptaDAO.findById: Adopta encontrado con id = " + id);
                return mapResultSetToAdopta(rs);
            } else {
                logger.info("AdoptaDAO.findById: No se encontró adopta con id = " + id);
            }
        } catch (SQLException e) {
            logger.severe("AdoptaDAO.findById: Error al buscar adopta: " + e.getMessage());
            throw e;
        }
        return null;
    }


    @Override
    public List<Adopta> findAll() throws SQLException {
        List<Adopta> adopciones = new ArrayList<>();
        String query = "SELECT * FROM adopta";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                adopciones.add(mapResultSetToAdopta(rs));
            }
            logger.info("AdoptaDAO.findAll: Total adopciones encontradas = " + adopciones.size());
        } catch (SQLException e) {
            logger.severe("AdoptaDAO.findAll: Error al obtener adopciones: " + e.getMessage());
            throw e;
        }
        return adopciones;
    }


    private Adopta mapResultSetToAdopta(ResultSet rs) throws SQLException {
        Adopta adopta = new Adopta();
        adopta.setIdAdopta(rs.getInt("idAdopta"));
        adopta.setIdAdoptante(rs.getInt("idAdoptante"));
        adopta.setIdAnimal(rs.getInt("idAnimal"));
        adopta.setFechaAdopcion(rs.getDate("fechaAdopcion") != null ? rs.getDate("fechaAdopcion").toLocalDate() : null);
        adopta.setObservaciones(rs.getString("observaciones"));
        return adopta;
    }

}

