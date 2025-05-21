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
            System.err.println("Error al inicializar la conexión en AdoptanteDAO:");
            e.printStackTrace();
        }
    }

    /**
     * Guarda un adoptante en la base de datos. Si el adoptante ya existe, lo actualiza.
     *
     * @param adoptante el objeto Adoptante a guardar o actualizar
     * @return el objeto Adoptante guardado o actualizado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Adoptante save(Adoptante adoptante) throws SQLException {
        if (adoptante.getIdAdoptante() > 0) {
            // Actualizar adoptante existente
            String updatePersonaSQL = "UPDATE persona SET nombre = ?, email = ? WHERE idPersona = ?";
            String updateAdoptanteSQL = "UPDATE adoptante SET nombre = ?, telefono = ?, direccion = ?, idAnimal = ?, observaciones = ? WHERE idAdoptante = ?";

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
                    adoptanteStmt.setString(1, adoptante.getNombre());
                    adoptanteStmt.setString(2, adoptante.getTelefono());
                    adoptanteStmt.setString(3, adoptante.getDireccion());
                    adoptanteStmt.setInt(4, adoptante.getIdAnimal());
                    adoptanteStmt.setString(5, adoptante.getObservaciones());
                    adoptanteStmt.setInt(6, adoptante.getIdAdoptante());
                    adoptanteStmt.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            // Insertar nuevo adoptante
            String insertPersonaSQL = "INSERT INTO persona (nombre, email) VALUES (?, ?)";
            String insertAdoptanteSQL = "INSERT INTO adoptante (telefono, direccion, idPersona, idAnimal, observaciones, nombre) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = ConnectionDB.getConnection()) {
                conn.setAutoCommit(false);

                try (PreparedStatement personaStmt = conn.prepareStatement(insertPersonaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
                     PreparedStatement adoptanteStmt = conn.prepareStatement(insertAdoptanteSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

                    // Insertar en persona
                    personaStmt.setString(1, adoptante.getNombre());
                    personaStmt.setString(2, adoptante.getEmail());
                    personaStmt.executeUpdate();

                    ResultSet rs = personaStmt.getGeneratedKeys();
                    if (rs.next()) {
                        int idPersona = rs.getInt(1);

                        // Insertar en adoptante
                        adoptanteStmt.setString(1, adoptante.getTelefono());
                        adoptanteStmt.setString(2, adoptante.getDireccion());
                        adoptanteStmt.setInt(3, idPersona);
                        adoptanteStmt.setInt(4, adoptante.getIdAnimal());
                        adoptanteStmt.setString(5, adoptante.getObservaciones());
                        adoptanteStmt.setString(6, adoptante.getNombre());
                        adoptanteStmt.executeUpdate();

                        // Recuperar el idAdoptante generado
                        ResultSet rsAdoptante = adoptanteStmt.getGeneratedKeys();
                        if (rsAdoptante.next()) {
                            adoptante.setIdAdoptante(rsAdoptante.getInt(1));
                        }
                        adoptante.setIdPersona(idPersona);
                        conn.commit();
                    } else {
                        throw new SQLException("No se pudo generar el ID para la tabla 'persona'.");
                    }
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }
            }
        }
        return adoptante;
    }

    /**
     * Elimina un adoptante de la base de datos.
     *
     * @param adoptante el objeto Adoptante a eliminar
     * @return el objeto Adoptante eliminado
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Adoptante delete(Adoptante adoptante) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, adoptante.getIdAdoptante());
            stmt.executeUpdate();
        }
        return adoptante;
    }

    /**
     * Busca un adoptante por su ID.
     *
     * @param id el ID del adoptante
     * @return el objeto Adoptante encontrado o null si no existe
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    public List<Adoptante> findAllByTrabajador(int idTrabajador) throws SQLException {
        List<Adoptante> adoptantes = new ArrayList<>();
        String query = "SELECT a.*, p.email FROM adoptante a " +
                "LEFT JOIN persona p ON a.idPersona = p.idPersona " +
                "INNER JOIN adopta ad ON a.idAdoptante = ad.idAdoptante " +
                "INNER JOIN cuida c ON ad.idAnimal = c.idAnimal " +
                "WHERE c.idTrabajador = ?";
        try (PreparedStatement stmt = ConnectionDB.getConnection().prepareStatement(query)) {
            stmt.setInt(1, idTrabajador);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                adoptantes.add(mapResultSetToAdoptante(rs));
            }
        }
        return adoptantes;
    }

    /**
     * Busca un adoptante por el ID del animal adoptado.
     *
     * @param idAnimal el ID del animal
     * @return el objeto Adoptante encontrado o null si no existe
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Recupera todos los adoptantes de la base de datos.
     *
     * @return lista de objetos Adoptante
     * @throws SQLException si ocurre un error en la base de datos
     */
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

    /**
     * Convierte un ResultSet en un objeto Adoptante.
     *
     * @param rs el ResultSet de la consulta
     * @return el objeto Adoptante mapeado
     * @throws SQLException si ocurre un error al leer el ResultSet
     */
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

    /**
     * Cierra la conexión a la base de datos.
     *
     * @throws IOException si ocurre un error al cerrar la conexión
     */
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

    /**
     * Busca un adoptante por su email.
     *
     * @param email el email del adoptante
     * @return el objeto Adoptante encontrado o null si no existe
     * @throws SQLException si ocurre un error en la base de datos
     */
    @Override
    public Adoptante findByEmail(String email) throws SQLException {
        return null;
    }
}
