package github.danicastroo.model.dao;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.entity.Trabajador;
import github.danicastroo.model.interfaces.InterfaceTrabajadorDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TrabajadorDAO implements InterfaceTrabajadorDAO<Trabajador> {

    private final static String INSERT = "INSERT INTO trabajador (nombre, estado, ubicacion, email, password) VALUES (?,?,?,?,?)";
    private final static String UPDATE = "SET nombre = ?, estado = ?, ubicacion = ?, email = ?, password = ? + WHERE idTrabajador = ?";
    private final static String DELETE = "DELETE FROM trabajador WHERE idTrabajador=?";
    private final static String FINDBYUSERNAME = "SELECT idTrabajador, nombre, estado, ubicacion, email, password FROM trabajador WHERE nombre=?";
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
            System.err.println("Error al inicializar la conexión en TrabajadorDAO:");
            e.printStackTrace();
        }
    }



    public Trabajador findByUsername(String username) throws SQLException {
        final String FIND_BY_USERNAME = "SELECT * FROM trabajador WHERE email = ?";

        try (PreparedStatement ps = ConnectionDB.getConnection().prepareStatement(FIND_BY_USERNAME)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Trabajador trabajador = new Trabajador();
                    trabajador.setIdTrabajador(rs.getInt("idTrabajador"));
                    trabajador.setNombre(rs.getString("nombre"));  // Cambia el tipo al corregir la tabla
                    trabajador.setEstado(rs.getString("estado"));
                    trabajador.setUbicacion(rs.getString("ubicacion"));
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


    @Override
    public String checkLogin(String email, String password) throws SQLException {
        try (PreparedStatement pst = ConnectionDB.getConnection().prepareStatement(QUERY)) {
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                return res.getString("nombre");
            }
        }
        return null;
    }

    @Override
    public Trabajador save(Trabajador trabajador) throws SQLException {
        String insertPersonaSQL = "INSERT INTO persona (nombre, email) VALUES (?, ?)";
        String insertTrabajadorSQL = "INSERT INTO trabajador (idTrabajador, estado, ubicacion, email, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false); // Habilitar transacciones

            try (PreparedStatement personaStmt = conn.prepareStatement(insertPersonaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement trabajadorStmt = conn.prepareStatement(insertTrabajadorSQL)) {

                // Insertar en tabla 'persona'
                personaStmt.setString(1, trabajador.getNombre());
                personaStmt.setString(2, trabajador.getEmail());
                personaStmt.executeUpdate();

                // Obtener id generado para persona
                ResultSet rs = personaStmt.getGeneratedKeys();
                if (rs.next()) {
                    int idPersona = rs.getInt(1);

                    // Insertar en tabla 'trabajador' usando id generado
                    trabajadorStmt.setInt(1, idPersona);
                    trabajadorStmt.setString(2, trabajador.getEstado());
                    trabajadorStmt.setString(3, trabajador.getUbicacion());
                    trabajadorStmt.setString(4, trabajador.getEmail());
                    trabajadorStmt.setString(5, trabajador.getPassword());
                    trabajadorStmt.executeUpdate();

                    conn.commit(); // Confirmar la transacción
                } else {
                    throw new SQLException("No se pudo generar el ID para la tabla 'persona'.");
                }
            } catch (SQLException e) {
                conn.rollback(); // Revertir la transacción en caso de error
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

    }

    public int generateId() throws SQLException {
        String sql = "SELECT MAX(idPersona) FROM persona"; // Buscamos el último ID en persona
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) + 1; // Si hay IDs, devuelve el siguiente disponible.
            }
        }
        return 1; // Si la tabla está vacía, empieza en 1.
    }
}
