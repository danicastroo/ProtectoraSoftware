package github.danicastroo.test;

import github.danicastroo.model.connection.ConnectionProperties;
import github.danicastroo.model.connection.XMLManager;

public class LoadConnection {
    public static void main(String[] args) {
        // Cargar la conexión desde el archivo XML
        ConnectionProperties c = XMLManager.readXML(new ConnectionProperties(),"connection.xml");
        System.out.println(c);
        System.out.println(">> Conexion correcta ✅");
    }
}