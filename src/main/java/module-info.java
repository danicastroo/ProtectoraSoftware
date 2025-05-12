module github.danicastroo {
    requires javafx.fxml;
    requires java.xml.bind;  // Módulo para JAXB
    requires java.sql;
    requires java.management;
    requires javafx.web;

    // Apertura para JavaFX
    opens github.danicastroo to javafx.fxml;
    opens github.danicastroo.view to javafx.fxml;

    // Abre el paquete que contiene las clases JAXB (como ConnectionProperties)
    opens github.danicastroo.model.connection to java.xml.bind;

    // Exporta los paquetes principales
    exports github.danicastroo;
    exports github.danicastroo.view;
}
