package github.danicastroo.model.connection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "connection")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    private String server;
    private String port;
    private String dataBase;
    private String user;
    private String password;

    /**
     * Constructor por defecto necesario para la deserialización XML.
     */
    public ConnectionProperties() {

    }

    /**
     * Constructor con todos los parámetros.
     *
     * @param server   el servidor de la base de datos
     * @param port     el puerto de conexión
     * @param dataBase el nombre de la base de datos
     * @param user     el usuario de la base de datos
     * @param password la contraseña del usuario
     */
    public ConnectionProperties(String server, String port, String dataBase, String user, String password) {
        this.server = server;
        this.port = port;
        this.dataBase = dataBase;
        this.user = user;
        this.password = password;
    }

    /**
     * Obtiene el servidor de la base de datos.
     *
     * @return el servidor
     */
    public String getServer() {
        return server;
    }

    /**
     * Establece el servidor de la base de datos.
     *
     * @param server el servidor a establecer
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Obtiene el puerto de conexión.
     *
     * @return el puerto
     */
    public String getPort() {
        return port;
    }

    /**
     * Establece el puerto de conexión.
     *
     * @param port el puerto a establecer
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Obtiene el nombre de la base de datos.
     *
     * @return el nombre de la base de datos
     */
    public String getDataBase() {
        return dataBase;
    }

    /**
     * Establece el nombre de la base de datos.
     *
     * @param dataBase el nombre de la base de datos a establecer
     */
    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    /**
     * Obtiene el usuario de la base de datos.
     *
     * @return el usuario
     */
    public String getUser() {
        return user;
    }

    /**
     * Establece el usuario de la base de datos.
     *
     * @param user el usuario a establecer
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return la contraseña
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password la contraseña a establecer
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Devuelve una representación en cadena del objeto ConnectionProperties.
     *
     * @return una cadena con los valores de las propiedades
     */
    @Override
    public String toString() {
        return "ConnectionProperties{" +
                "server='" + server + '\'' +
                ", port='" + port + '\'' +
                ", dataBase='" + dataBase + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * Devuelve la URL de conexión JDBC construida a partir de las propiedades.
     *
     * @return la URL de conexión JDBC
     */
    public String getURL(){
        return "jdbc:mysql://"+server+":"+port+"/"+dataBase;
    }
}
