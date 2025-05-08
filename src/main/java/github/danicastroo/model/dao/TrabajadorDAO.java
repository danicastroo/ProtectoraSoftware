package github.danicastroo.model.dao;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.entity.Trabajador;
import github.danicastroo.model.interfaces.InterfaceTrabajadorDAO;

import java.io.IOException;
import java.sql.*;
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

    public boolean isEmailRegistered(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM persona WHERE email = ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Si el conteo es mayor a 0, el correo ya existe
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
    public Trabajador checkLogin(String email, String password) throws SQLException {
        // Consulta SQL para validar credenciales y obtener datos adicionales (incluyendo el nombre)
        String query = "SELECT t.idTrabajador, t.estado, t.ubicacion, t.email, p.nombre " +
                "FROM trabajador t " +
                "INNER JOIN persona p ON t.idTrabajador = p.idPersona " +
                "WHERE t.email = ? AND t.password = ?";

        // Inicializamos el objeto Trabajador en null
        Trabajador trabajador = null;

        // Conexión a la base de datos
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Asignar los parámetros de la consulta
            stmt.setString(1, email);
            stmt.setString(2, password);

            // Ejecutar la consulta y procesar el resultado
            ResultSet resultSet = stmt.executeQuery();
            boolean encontrado = resultSet.next(); // Verificar si hay un resultado

            if (encontrado) {
                // Si se encuentra un resultado, creamos el objeto Trabajador
                trabajador = new Trabajador();
                trabajador.setIdTrabajador(resultSet.getInt("idTrabajador"));
                trabajador.setEstado(resultSet.getString("estado"));
                trabajador.setUbicacion(resultSet.getString("ubicacion"));
                trabajador.setEmail(resultSet.getString("email"));
                trabajador.setNombre(resultSet.getString("nombre")); // Establecer el nombre desde la tabla Persona
            }

            // Cerramos el ResultSet manualmente
            resultSet.close();
        }

        // Retornamos el objeto Trabajador (puede ser null si no se encontró nada)
        return trabajador;
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
            } catch (SQLIntegrityConstraintViolationException e) {
                conn.rollback(); // Revertir la transacción en caso de error
                throw new SQLException("El nombre ya está ocupado o el correo ya está en uso.", e);
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


    public boolean isNameRegistered(String nombre) throws SQLException {
        String query = "SELECT COUNT(*) FROM persona WHERE nombre = ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nombre); // Reemplaza el parámetro de la consulta con el nombre
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Si COUNT(*) > 0, el nombre ya existe
            }
        }
        return false; // Si no hay filas con ese nombre, el nombre no está registrado
    }
}
