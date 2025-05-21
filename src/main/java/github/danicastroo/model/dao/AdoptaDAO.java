package github.danicastroo.model.dao;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.entity.Adopta;
import github.danicastroo.model.interfaces.InterfaceAdoptaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdoptaDAO implements InterfaceAdoptaDAO {

    /**
     * Inserta una nueva adopción en la base de datos.
     *
     * @param adopta el objeto Adopta a guardar
     * @return el objeto Adopta con el ID generado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Adopta save(Adopta adopta) throws SQLException {
        String query = "INSERT INTO adopta (idAdoptante, idAnimal, fechaAdopcion, observaciones) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, adopta.getIdAdoptante());
            stmt.setInt(2, adopta.getIdAnimal());
            stmt.setDate(3, Date.valueOf(adopta.getFechaAdopcion()));
            stmt.setString(4, adopta.getObservaciones());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                adopta.setIdAdopta(rs.getInt(1)); // Asigna el ID generado
            }
        }
        return adopta;
    }

    /**
     * Elimina una adopción de la base de datos.
     *
     * @param adopta el objeto Adopta a eliminar
     * @return el objeto Adopta eliminado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Adopta delete(Adopta adopta) throws SQLException {
        String query = "DELETE FROM adopta WHERE idAdopta = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, adopta.getIdAdopta());
            stmt.executeUpdate();
        }
        return adopta;
    }

    /**
     * Busca una adopción por su ID.
     *
     * @param id el ID de la adopción
     * @return el objeto Adopta encontrado o null si no existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Adopta findById(int id) throws SQLException {
        String query = "SELECT * FROM adopta WHERE idAdopta = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAdopta(rs);
            }
        }
        return null;
    }

    /**
     * Recupera todas las adopciones de la base de datos.
     *
     * @return lista de objetos Adopta
     * @throws SQLException si ocurre un error en la base de datos
     */
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
        }
        return adopciones;
    }

    /**
     * Convierte un ResultSet en un objeto Adopta.
     *
     * @param rs el ResultSet de la consulta
     * @return el objeto Adopta mapeado
     * @throws SQLException si ocurre un error al leer el ResultSet
     */
    private Adopta mapResultSetToAdopta(ResultSet rs) throws SQLException {
        Adopta adopta = new Adopta();
        adopta.setIdAdopta(rs.getInt("idAdopta"));
        adopta.setIdAdoptante(rs.getInt("idAdoptante"));
        adopta.setIdAnimal(rs.getInt("idAnimal"));
        adopta.setFechaAdopcion(rs.getDate("fechaAdopcion") != null ? rs.getDate("fechaAdopcion").toLocalDate() : null);
        adopta.setObservaciones(rs.getString("observaciones"));
        return adopta;
    }

    /**
     * Busca todas las adopciones realizadas por un adoptante específico.
     *
     * @param idAdoptante el ID del adoptante
     * @return lista de adopciones realizadas por ese adoptante
     * @throws SQLException si ocurre un error en la base de datos
     */
    public List<Adopta> findByAdoptanteId(int idAdoptante) throws SQLException {
        List<Adopta> adopciones = new ArrayList<>();
        String query = "SELECT * FROM adopta WHERE idAdoptante = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idAdoptante);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Adopta adopcion = new Adopta();
                adopcion.setIdAdopta(rs.getInt("idAdopta"));
                adopcion.setIdAdoptante(rs.getInt("idAdoptante"));
                adopcion.setIdAnimal(rs.getInt("idAnimal"));
                adopcion.setFechaAdopcion(rs.getDate("fechaAdopcion").toLocalDate());
                adopcion.setObservaciones(rs.getString("observaciones"));
                adopciones.add(adopcion);
            }
        }
        return adopciones;
    }
}
