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
    private final static String FINDALL = "SELECT a.*, p.email FROM adoptante a " +
            "JOIN persona p ON a.idPersona = p.idPersona";

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
        String checkEmailSQL = "SELECT COUNT(*) FROM persona WHERE email = ?";
        String insertPersonaSQL = "INSERT INTO persona (nombre, email) VALUES (?, ?)";
        String insertAdoptanteSQL = "INSERT INTO adoptante (telefono, direccion, idPersona, idAnimal, observaciones, nombre) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false); // Inicia una transacción

            // Verificar si el correo ya existe
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEmailSQL)) {
                checkStmt.setString(1, adoptante.getEmail());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("El correo electrónico ya está registrado: " + adoptante.getEmail());
                }
            }

            // Inserta en persona
            int idPersona;
            try (PreparedStatement personaStmt = conn.prepareStatement(insertPersonaSQL, Statement.RETURN_GENERATED_KEYS)) {
                personaStmt.setString(1, adoptante.getNombre());
                personaStmt.setString(2, adoptante.getEmail());
                personaStmt.executeUpdate();

                ResultSet rs = personaStmt.getGeneratedKeys();
                if (rs.next()) {
                    idPersona = rs.getInt(1); // Obtiene el ID generado
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para la persona.");
                }
            }

            // Inserta en adoptante
            try (PreparedStatement adoptanteStmt = conn.prepareStatement(insertAdoptanteSQL, Statement.RETURN_GENERATED_KEYS)) {
                adoptanteStmt.setString(1, adoptante.getTelefono());
                adoptanteStmt.setString(2, adoptante.getDireccion());
                adoptanteStmt.setInt(3, idPersona);
                adoptanteStmt.setInt(4, adoptante.getIdAnimal());
                adoptanteStmt.setString(5, adoptante.getObservaciones());
                adoptanteStmt.setString(6, adoptante.getNombre());
                adoptanteStmt.executeUpdate();

                ResultSet rs = adoptanteStmt.getGeneratedKeys();
                if (rs.next()) {
                    adoptante.setIdAdoptante(rs.getInt(1)); // Asigna el ID generado
                }
            }

            conn.commit(); // Confirma la transacción
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
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
        adoptante.setEmail(rs.getString("email")); // Ahora se obtiene del JOIN con persona
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