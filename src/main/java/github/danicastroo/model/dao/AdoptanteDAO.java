package github.danicastroo.model.dao;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.entity.Adoptante;
import github.danicastroo.model.interfaces.InterfaceAdoptanteDAO;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AdoptanteDAO implements InterfaceAdoptanteDAO<Adoptante> {

    private static final Logger logger = Logger.getLogger(AdoptanteDAO.class.getName());

    private final static String INSERT = "INSERT INTO adoptante (idAdoptante, telefono, email, idAnimal, observaciones) VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE = "UPDATE adoptante SET nombre = ?, telefono = ?, email = ?, direccion = ?, idAnimal = ?, observaciones = ? WHERE idAdoptante = ?";
    private final static String DELETE = "DELETE FROM adoptante WHERE idAdoptante = ?";
    private final static String FINDBYID = "SELECT * FROM adoptante WHERE idAdoptante = ?";
    private final static String FINDBYANIMALID = "SELECT * FROM adoptante WHERE idAnimal = ?";
    private final static String FINDALL = "SELECT a.*, p.email FROM adoptante a " +
            "LEFT JOIN persona p ON a.idPersona = p.idPersona " +
            "WHERE a.idAdoptante IS NOT NULL";

    private Connection conn;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     */
    public AdoptanteDAO() {
        try {
            this.conn = ConnectionDB.getConnection();
        } catch (SQLException e) {
            logger.severe("Error al inicializar la conexión en AdoptanteDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

   @Override
    public Adoptante save(Adoptante adoptante) throws SQLException {
        if (adoptante.getIdAdoptante() > 0) {
            String updatePersonaSQL = "UPDATE persona SET nombre = ?, email = ? WHERE idPersona = ?";
            String updateAdoptanteSQL = "UPDATE adoptante SET telefono = ?, direccion = ?, idAnimal = ?, observaciones = ? WHERE idAdoptante = ?";

            try (Connection conn = ConnectionDB.getConnection()) {
                conn.setAutoCommit(false);

                // Actualizar en persona
                try (PreparedStatement personaStmt = conn.prepareStatement(updatePersonaSQL)) {
                    personaStmt.setString(1, adoptante.getNombre());
                    personaStmt.setString(2, adoptante.getEmail());
                    personaStmt.setInt(3, adoptante.getIdPersona());
                    personaStmt.executeUpdate();
                }

                // Actualizar en adoptante
                try (PreparedStatement adoptanteStmt = conn.prepareStatement(updateAdoptanteSQL)) {
                    adoptanteStmt.setString(1, adoptante.getTelefono());
                    adoptanteStmt.setString(2, adoptante.getDireccion());
                    adoptanteStmt.setInt(3, adoptante.getIdAnimal());
                    adoptanteStmt.setString(4, adoptante.getObservaciones());
                    adoptanteStmt.setInt(5, adoptante.getIdAdoptante());
                    int info = adoptanteStmt.executeUpdate();
                    logger.info("AdoptanteDAO.save (update): Filas actualizadas = " + info);

                }

                conn.commit(); // Confirma la transacción
            } catch (SQLException e) {
                logger.severe("AdoptanteDAO.save (update): Error al actualizar en persona o adoptante: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        } else {
            // Código para insertar un nuevo adoptante (ya implementado)
        }
        return adoptante;
    }

    @Override
    public Adoptante delete(Adoptante adoptante) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, adoptante.getIdAdoptante());
            int info = stmt.executeUpdate();
            logger.info("AdoptanteDAO.delete: Filas eliminadas = " + info);
        }
        return adoptante;
    }

    @Override
    public Adoptante findById(int id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(FINDBYID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                logger.info("AdoptanteDAO.findById: Adoptante encontrado con id = " + id);
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
                logger.info("AdoptanteDAO.findAll: Adoptante encontrado");
                adoptantes.add(mapResultSetToAdoptante(rs));
            }
        }
        return adoptantes;
    }

    private Adoptante mapResultSetToAdoptante(ResultSet rs) throws SQLException {
        Adoptante adoptante = new Adoptante();
        adoptante.setIdAdoptante(rs.getInt("idAdoptante"));
        adoptante.setNombre(rs.getString("nombre")); // De persona
        adoptante.setTelefono(rs.getString("telefono"));
        adoptante.setEmail(rs.getString("email")); // De persona
        adoptante.setDireccion(rs.getString("direccion"));
        adoptante.setIdAnimal(rs.getInt("idAnimal"));
        adoptante.setObservaciones(rs.getString("observaciones"));
        adoptante.setIdPersona(rs.getInt("idPersona")); // Relación con persona
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