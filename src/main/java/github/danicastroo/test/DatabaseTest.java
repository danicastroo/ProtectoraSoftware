package github.danicastroo.test;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.dao.TrabajadorDAO;
import github.danicastroo.model.entity.Trabajador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase de prueba para la configuración de la base de datos.
 */
public class DatabaseTest {

    public static void main(String[] args) {
        System.out.println("Iniciando pruebas de la base de datos...");

        // 1. Verificar la conexión
        testDatabaseConnection();

        // 2. Probar inserción y lectura de datos
        testInsertAndRead();
    }

    /**
     * Método para verificar si la conexión a la base de datos es exitosa.
     */
    public static void testDatabaseConnection() {
        try (Connection connection = ConnectionDB.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Conexión exitosa a la base de datos.");
            } else {
                System.out.println("Error: No se pudo establecer la conexión a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión a la base de datos:");
            e.printStackTrace();
        }
    }

    /**
     * Método para probar la inserción y lectura de datos en la base de datos.
     */
    public static void testInsertAndRead() {
        try {
            TrabajadorDAO trabajadorDAO = new TrabajadorDAO();

            // Crear un nuevo trabajador
            Trabajador trabajador = new Trabajador();
            trabajador.setIdTrabajador(1);
            trabajador.setNombre("John Doe");
            trabajador.setEstado("Activo");
            trabajador.setEmail("john.doe@example.com");
            trabajador.setPassword("password123");

            // Insertar trabajador en la base de datos
            trabajadorDAO.save(trabajador);
            System.out.println("Inserción exitosa de un nuevo trabajador.");

            // Leer los datos insertados
            try (Connection conn = ConnectionDB.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM trabajador WHERE idTrabajador = 1")) {

                if (rs.next()) {
                    System.out.println("Datos del trabajador recuperados correctamente:");
                    System.out.println("ID: " + rs.getInt("idTrabajador"));
                    System.out.println("Nombre: " + rs.getString("nombre"));
                    System.out.println("Estado: " + rs.getString("estado"));
                    System.out.println("Ubicación: " + rs.getString("ubicacion"));
                    System.out.println("Email: " + rs.getString("email"));
                } else {
                    System.out.println("Error: No se encontraron datos del trabajador.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error durante la prueba de inserción y lectura:");
            e.printStackTrace();
        }
    }
}