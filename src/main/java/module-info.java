module github.danicastroo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;
    requires java.desktop;
    requires java.sql;

    opens github.danicastroo to javafx.fxml;
    opens github.danicastroo.view to javafx.fxml;
    exports github.danicastroo;
    exports github.danicastroo.view;
}
