module github.danicastroo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;  // Módulo para JAXB
    requires java.desktop;
    requires java.sql;

    // Apertura para JavaFX
    opens github.danicastroo to javafx.fxml;
    opens github.danicastroo.view to javafx.fxml;

    // Abre el paquete que contiene las clases JAXB (como ConnectionProperties)
    opens github.danicastroo.model.connection to java.xml.bind;

    // Exporta los paquetes principales
    exports github.danicastroo;
    exports github.danicastroo.view;
}
