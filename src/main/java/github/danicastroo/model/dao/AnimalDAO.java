package github.danicastroo.model.dao;

             import github.danicastroo.model.entity.Animal;
             import github.danicastroo.model.entity.EstadoAnimal;
             import github.danicastroo.model.entity.TipoAnimal;
             import github.danicastroo.model.connection.ConnectionDB;
             import github.danicastroo.model.interfaces.DAO;

             import java.io.IOException;
             import java.sql.*;
             import java.util.ArrayList;
             import java.util.List;

             public class AnimalDAO implements DAO<Animal> {

                 /**
                  * Inserta un nuevo animal en la base de datos.
                  *
                  * @param animal el objeto Animal a guardar
                  * @return el objeto Animal con el ID generado
                  * @throws SQLException si ocurre un error en la base de datos
                  */
                 @Override
                 public Animal save(Animal animal) throws SQLException {
                     String query = "INSERT INTO animal (nombre, chip, edad, tipo, estado, fechaAdopcion) VALUES (?, ?, ?, ?, ?, ?)";
                     try (Connection conn = ConnectionDB.getConnection();
                          PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                         stmt.setString(1, animal.getNombre());
                         stmt.setString(2, animal.getChip());
                         stmt.setInt(3, animal.getEdad());
                         stmt.setString(4, animal.getTipo().toString());
                         stmt.setString(5, animal.getEstado().toString());

                         if (animal.getFechaAdopcion() != null) {
                             stmt.setDate(6, Date.valueOf(animal.getFechaAdopcion()));
                         } else {
                             stmt.setNull(6, Types.DATE);
                         }

                         stmt.executeUpdate();

                         ResultSet rs = stmt.getGeneratedKeys();
                         if (rs.next()) {
                             animal.setIdAnimal(rs.getInt(1));
                         }
                     }
                     return animal;
                 }

                 /**
                  * Actualiza los datos de un animal en la base de datos.
                  *
                  * @param animal el objeto Animal a actualizar
                  * @return el objeto Animal actualizado
                  * @throws SQLException si ocurre un error en la base de datos
                  */
                 public Animal update(Animal animal) throws SQLException {
                     String query = "UPDATE animal SET nombre = ?, chip = ?, edad = ?, tipo = ?, estado = ?, fechaAdopcion = ? WHERE idAnimal = ?";
                     try (Connection conn = ConnectionDB.getConnection();
                          PreparedStatement stmt = conn.prepareStatement(query)) {

                         stmt.setString(1, animal.getNombre());
                         stmt.setString(2, animal.getChip());
                         stmt.setInt(3, animal.getEdad());
                         stmt.setString(4, animal.getTipo().toString());
                         stmt.setString(5, animal.getEstado().toString());

                         if (animal.getFechaAdopcion() != null) {
                             stmt.setDate(6, Date.valueOf(animal.getFechaAdopcion()));
                         } else {
                             stmt.setNull(6, Types.DATE);
                         }

                         stmt.setInt(7, animal.getIdAnimal());

                         stmt.executeUpdate();
                     }
                     return animal;
                 }

                 /**
                  * Elimina un animal de la base de datos.
                  *
                  * @param animal el objeto Animal a eliminar
                  * @return el objeto Animal eliminado
                  * @throws SQLException si ocurre un error en la base de datos
                  */
                 @Override
                 public Animal delete(Animal animal) throws SQLException {
                     String query = "DELETE FROM animal WHERE idAnimal = ?";
                     try (Connection conn = ConnectionDB.getConnection();
                          PreparedStatement stmt = conn.prepareStatement(query)) {

                         stmt.setInt(1, animal.getIdAnimal());
                         stmt.executeUpdate();
                     }
                     return animal;
                 }

                 /**
                  * Busca un animal por su ID.
                  *
                  * @param id el ID del animal
                  * @return el objeto Animal encontrado o null si no existe
                  */
                 @Override
                 public Animal findById(int id) {
                     String query = "SELECT * FROM animal WHERE idAnimal = ?";
                     try (Connection conn = ConnectionDB.getConnection();
                          PreparedStatement stmt = conn.prepareStatement(query)) {

                         stmt.setInt(1, id);
                         ResultSet rs = stmt.executeQuery();

                         if (rs.next()) {
                             return mapResultSetToAnimal(rs);
                         }
                     } catch (SQLException e) {
                         e.printStackTrace();
                     }
                     return null;
                 }

                 /**
                  * Recupera todos los animales de la base de datos.
                  *
                  * @return lista de objetos Animal
                  */
                 @Override
                 public List<Animal> findAll() {
                     List<Animal> animals = new ArrayList<>();
                     String query = "SELECT * FROM animal";
                     try (Connection conn = ConnectionDB.getConnection();
                          PreparedStatement stmt = conn.prepareStatement(query);
                          ResultSet rs = stmt.executeQuery()) {

                         while (rs.next()) {
                             animals.add(mapResultSetToAnimal(rs));
                         }
                     } catch (SQLException e) {
                         e.printStackTrace();
                     }
                     return animals;
                 }

                 /**
                  * Convierte un ResultSet en un objeto Animal.
                  *
                  * @param rs el ResultSet de la consulta
                  * @return el objeto Animal mapeado
                  * @throws SQLException si ocurre un error al leer el ResultSet
                  */
                 private Animal mapResultSetToAnimal(ResultSet rs) throws SQLException {
                     Animal animal = new Animal();
                     animal.setIdAnimal(rs.getInt("idAnimal"));
                     animal.setNombre(rs.getString("nombre"));
                     animal.setChip(rs.getString("chip"));
                     animal.setEdad(rs.getInt("edad"));
                     animal.setTipo(TipoAnimal.valueOf(rs.getString("tipo").toUpperCase()));
                     animal.setFechaAdopcion(rs.getDate("fechaAdopcion") != null ? rs.getDate("fechaAdopcion").toLocalDate() : null);
                     animal.setEstado(EstadoAnimal.fromString(rs.getString("estado")));
                     return animal;
                 }

                 /**
                  * Cierra recursos si es necesario.
                  *
                  * @throws IOException si ocurre un error al cerrar recursos
                  */
                 @Override
                 public void close() throws IOException {
                 }
             }
