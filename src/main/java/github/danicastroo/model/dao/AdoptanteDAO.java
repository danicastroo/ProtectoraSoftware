package github.danicastroo.model.dao;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.entity.Adoptante;
import github.danicastroo.model.interfaces.InterfaceAdoptanteDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdoptanteDAO implements InterfaceAdoptanteDAO<Adoptante> {

    private final static String INSERT = "INSERT INTO adoptante (idAdoptante, telefono, email, idAnimal, observaciones) VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE = "UPDATE adoptante SET nombre = ?, telefono = ?, email = ?, direccion = ?, idAnimal = ?, observaciones = ? WHERE idAdoptante = ?";    private final static String DELETE = "DELETE FROM adoptante WHERE idAdoptante = ?";
    private final static String FINDBYID = "SELECT * FROM adoptante WHERE idAdoptante = ?";
    private final static String FINDBYANIMALID = "SELECT * FROM adoptante WHERE idAnimal = ?";
    private final static String FINDALL = "SELECT * FROM adoptante";

    private Connection conn;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     */
    public AdoptanteDAO() {
        try {
            this.conn = ConnectionDB.getConnection();
        } catch (SQLException e) {
            System.err.println("Error al inicializar la conexión en AdoptanteDAO:");
            e.printStackTrace();
        }
    }

    @Override
    public Adoptante save(Adoptante adoptante) throws SQLException {
        String insertAdoptanteSQL = "INSERT INTO adoptante (nombre, telefono, direccion, idPersona, idAnimal, observaciones) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement adoptanteStmt = conn.prepareStatement(insertAdoptanteSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Insertar en adoptante
            adoptanteStmt.setString(1, adoptante.getNombre());
            adoptanteStmt.setString(2, adoptante.getTelefono());
            adoptanteStmt.setString(3, adoptante.getDireccion());
            adoptanteStmt.setInt(4, adoptante.getIdAdoptante()); // idPersona
            adoptanteStmt.setInt(5, adoptante.getIdAnimal());
            adoptanteStmt.setString(6, adoptante.getObservaciones());
            adoptanteStmt.executeUpdate();

            ResultSet rs = adoptanteStmt.getGeneratedKeys();
            if (rs.next()) {
                adoptante.setIdAdoptante(rs.getInt(1));
            }
        }
        return adoptante;
    }



    @Override
    public Adoptante delete(Adoptante adoptante) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, adoptante.getIdAdoptante());
            stmt.executeUpdate();
        }
        return adoptante;
    }

    @Override
    public Adoptante findById(int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(FINDBYID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToAdoptante(rs);
            }
        }
        return null;
    }

    public Adoptante findByAnimalId(int idAnimal) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(FINDBYANIMALID)) {
            stmt.setInt(1, idAnimal);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToAdoptante(rs);
            }
        }
        return null;
    }

    @Override
    public List<Adoptante> findAll() throws SQLException {
        List<Adoptante> adoptantes = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(FINDALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                adoptantes.add(mapResultSetToAdoptante(rs));
            }
        }
        return adoptantes;
    }

    private Adoptante mapResultSetToAdoptante(ResultSet rs) throws SQLException {
        Adoptante adoptante = new Adoptante();
        adoptante.setIdAdoptante(rs.getInt("idAdoptante"));
        adoptante.setNombre(rs.getString("nombre"));
        adoptante.setTelefono(rs.getString("telefono"));
        adoptante.setEmail(rs.getString("email"));
        adoptante.setDireccion(rs.getString("direccion"));
        adoptante.setIdAnimal(rs.getInt("idAnimal"));
        adoptante.setObservaciones(rs.getString("observaciones"));
        return adoptante;
    }

    @Override
    public void close() throws IOException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Adoptante findByEmail(String email) throws SQLException {
        return null;
    }
}