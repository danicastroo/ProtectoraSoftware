package github.danicastroo.model.dao;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.entity.Trabajador;
import github.danicastroo.model.interfaces.InterfaceTrabajadorDAO;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

public class TrabajadorDAO implements InterfaceTrabajadorDAO<Trabajador> {

    private static final Logger logger = Logger.getLogger(TrabajadorDAO.class.getName());

    private final static String INSERT = "INSERT INTO trabajador (nombre, estado, email, password) VALUES (?,?,?,?)";
    private final static String UPDATE = "UPDATE trabajador SET nombre = ?, estado = ?, email = ?, password = ? WHERE idTrabajador = ?";
    private final static String DELETE = "DELETE FROM trabajador WHERE idTrabajador=?";
    private final static String FINDBYUSERNAME = "SELECT idTrabajador, nombre, estado, email, password FROM trabajador WHERE nombre=?";
    private final static String QUERY = "SELECT nombre FROM trabajador WHERE email=? AND password=?";
    private Connection conn;

    /**
     * Constructor that initializes the connection to the database.
     */
    public TrabajadorDAO() {
        try {
            // Initializing the connection once an instance of TrabajadorDAO is created
            this.conn = ConnectionDB.getConnection();
        } catch (SQLException e) {
            // Gestionar el error si la conexión no se puede inicializar
            logger.severe("Error al inicializar la conexión en TrabajadorDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isEmailRegistered(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM persona WHERE email = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean exists = rs.getInt(1) > 0;
                logger.info("TrabajadorDAO.isEmailRegistered: email " + email + " registrado = " + exists);
                return exists;
            }
        }
        return false;
    }



    public Trabajador findByUsername(String username) throws SQLException {
        final String FIND_BY_USERNAME = "SELECT * FROM trabajador WHERE email = ?";
        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(FIND_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Trabajador trabajador = new Trabajador();
                    trabajador.setIdTrabajador(rs.getInt("idTrabajador"));
                    trabajador.setNombre(rs.getString("nombre"));
                    trabajador.setEstado(rs.getString("estado"));
                    trabajador.setEmail(rs.getString("email"));
                    trabajador.setPassword(rs.getString("password"));
                    logger.info("TrabajadorDAO.findByUsername: Trabajador encontrado para username = " + username);
                    return trabajador;
                }
            }
        } catch (SQLException e) {
            logger.severe("TrabajadorDAO.findByUsername: Error al buscar trabajador: " + e.getMessage());
            throw e;
        }
        return null;
    }



    @Override
    public Trabajador checkLogin(String email, String password) throws SQLException {
        String query = "SELECT t.idTrabajador, t.estado, t.email, p.nombre " +
                "FROM trabajador t " +
                "INNER JOIN persona p ON t.idTrabajador = p.idPersona " +
                "WHERE t.email = ? AND t.password = ?";
        Trabajador trabajador = null;
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                trabajador = new Trabajador();
                trabajador.setIdTrabajador(resultSet.getInt("idTrabajador"));
                trabajador.setEstado(resultSet.getString("estado"));
                trabajador.setEmail(resultSet.getString("email"));
                trabajador.setNombre(resultSet.getString("nombre"));
                logger.info("TrabajadorDAO.checkLogin: Login correcto para email = " + email);
            } else {
                logger.info("TrabajadorDAO.checkLogin: Login fallido para email = " + email);
            }
            resultSet.close();
        } catch (SQLException e) {
            logger.severe("TrabajadorDAO.checkLogin: Error al comprobar login: " + e.getMessage());
            throw e;
        }
        return trabajador;
    }



    @Override
    public Trabajador save(Trabajador trabajador) throws SQLException {
        String insertPersonaSQL = "INSERT INTO persona (nombre, email) VALUES (?, ?)";
        String insertTrabajadorSQL = "INSERT INTO trabajador (idTrabajador, estado, email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement personaStmt = conn.prepareStatement(insertPersonaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement trabajadorStmt = conn.prepareStatement(insertTrabajadorSQL)) {
                personaStmt.setString(1, trabajador.getNombre());
                personaStmt.setString(2, trabajador.getEmail());
                personaStmt.executeUpdate();
                ResultSet rs = personaStmt.getGeneratedKeys();
                if (rs.next()) {
                    int idPersona = rs.getInt(1);
                    trabajadorStmt.setInt(1, idPersona);
                    trabajadorStmt.setString(2, trabajador.getEstado());
                    trabajadorStmt.setString(3, trabajador.getEmail());
                    trabajadorStmt.setString(4, trabajador.getPassword());
                    trabajadorStmt.executeUpdate();
                    conn.commit();
                    logger.info("TrabajadorDAO.save: Trabajador insertado con idPersona = " + idPersona);
                } else {
                    throw new SQLException("No se pudo generar el ID para la tabla 'persona'.");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                conn.rollback();
                logger.severe("TrabajadorDAO.save: Violación de restricción de integridad: " + e.getMessage());
                throw new SQLException("El nombre ya está ocupado o el correo ya está en uso.", e);
            } catch (SQLException e) {
                conn.rollback();
                logger.severe("TrabajadorDAO.save: Error al guardar trabajador: " + e.getMessage());
                throw e;
            }
        }
        return trabajador;
    }

    @Override
    public Trabajador delete(Trabajador entity) throws SQLException {
        return null;
    }

    @Override
    public Trabajador findById(int key) {
        return null;
    }

    @Override
    public List<Trabajador> findAll() {
        return List.of();
    }

    @Override
    public void close() throws IOException {
        if (conn != null) {
            try {
                conn.close();
                logger.info("TrabajadorDAO.close: Conexión cerrada correctamente.");
            } catch (SQLException e) {
                logger.severe("TrabajadorDAO.close: Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
