package github.danicastroo.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionDB {
    private final static String FILE = "connection.xml";
    private static Connection con;
    private static ConnectionDB _instance;

    private String url;
    private String user;
    private String password;


    private ConnectionDB() {
        ConnectionProperties properties = null;

        try {
            // Leer las propiedades desde el archivo de configuración (XML)
            properties = XMLManager.readXML(new ConnectionProperties(), FILE);

            // Asignar las propiedades de conexión
            this.url = properties.getURL();
            this.user = properties.getUser();
            this.password = properties.getPassword();

            // Intentar establecer la conexión
            con = DriverManager.getConnection(this.url, this.user, this.password);
            System.out.println("Conexión establecida correctamente.");

        } catch (NullPointerException e) {
            // Error si alguna propiedad devuelta es nula
            System.out.println("Error: Las propiedades de conexión no se pudieron leer correctamente.");
            e.printStackTrace();
            con = null;

        } catch (SQLException e) {
            // Error al intentar conectar con la base de datos
            System.out.println("Error: No se pudo establecer conexión con la base de datos.");
            e.printStackTrace();
            con = null;

        } catch (Exception e) {
            // Captura general para manejar cualquier error imprevisto
            System.out.println("Error inesperado al intentar conectar la base de datos:");
            e.printStackTrace();
            con = null;
        }
    }

    public static ConnectionDB getInstance() {
        if (_instance == null) {
            _instance = new ConnectionDB();
        }
        return _instance;
    }


    public static Connection getConnection() throws SQLException {
        // Obtener una nueva conexión a la base de datos cada vez que se llama
        try {
            ConnectionDB instance = getInstance();
            return DriverManager.getConnection(instance.url, instance.user, instance.password);
        } catch (SQLException e) {
            System.out.println("Error al conectarse a la base de datos:");
            e.printStackTrace();
            throw e;
        }
    }


    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

