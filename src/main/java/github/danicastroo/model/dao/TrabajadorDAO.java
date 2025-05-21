package github.danicastroo.model.dao;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.entity.Trabajador;
import github.danicastroo.model.interfaces.InterfaceTrabajadorDAO;

import java.io.IOException;
import java.sql.*;
import java.util.List;


public class TrabajadorDAO implements InterfaceTrabajadorDAO<Trabajador> {

    private Connection conn;

    /**
     * Constructor que inicializa la conexión a la base de datos.
     */
    public TrabajadorDAO() {
        try {
            this.conn = ConnectionDB.getConnection();
        } catch (SQLException e) {
            System.err.println("Error al inicializar la conexión en TrabajadorDAO:");
            e.printStackTrace();
        }
    }

    /**
     * Verifica si un email ya está registrado en la tabla persona.
     *
     * @param email el email a verificar.
     * @return true si ya está registrado, false si no.
     * @throws SQLException si ocurre un error en la consulta.
     */
    public boolean isEmailRegistered(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM persona WHERE email = ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Busca un trabajador en la base de datos por su nombre de usuario (email).
     *
     * @param username el nombre de usuario (email).
     * @return el objeto Trabajador si se encuentra, null en caso contrario.
     * @throws SQLException si ocurre un error al ejecutar la consulta.
     */
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
                    return trabajador;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return null;
    }

    /**
     * Comprueba si un trabajador puede iniciar sesión con el email y contraseña proporcionados.
     *
     * @param email    el email del trabajador.
     * @param password la contraseña del trabajador.
     * @return el objeto Trabajador si las credenciales son válidas, null si no lo son.
     * @throws SQLException si ocurre un error en la consulta.
     */
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
            }
            resultSet.close();
        }
        return trabajador;
    }

    /**
     * Guarda un nuevo trabajador en la base de datos, insertando primero en persona y luego en trabajador.
     *
     * @param trabajador el objeto Trabajador a guardar.
     * @return el mismo objeto Trabajador guardado.
     * @throws SQLException si ocurre un error al insertar.
     */
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
                } else {
                    throw new SQLException("No se pudo generar el ID para la tabla 'persona'.");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                conn.rollback();
                throw new SQLException("El nombre ya está ocupado o el correo ya está en uso.", e);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
        return trabajador;
    }

    /**
     * Método sin implementar para eliminar un trabajador.
     *
     * @param entity el objeto Trabajador a eliminar.
     * @return null actualmente.
     * @throws SQLException si ocurre un error (no implementado).
     */
    @Override
    public Trabajador delete(Trabajador entity) throws SQLException {
        return null;
    }

    /**
     * Método sin implementar para buscar un trabajador por ID.
     *
     * @param key el ID del trabajador.
     * @return null actualmente.
     */
    @Override
    public Trabajador findById(int key) {
        return null;
    }

    /**
     * Método sin implementar que debería retornar todos los trabajadores.
     *
     * @return lista vacía actualmente.
     */
    @Override
    public List<Trabajador> findAll() {
        return List.of();
    }

    /**
     * Cierra los recursos de DAO. Actualmente sin implementación.
     *
     * @throws IOException si ocurre un error al cerrar.
     */
    @Override
    public void close() throws IOException {
        // Método sin implementar
    }

}
