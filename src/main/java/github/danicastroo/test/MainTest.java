package github.danicastroo;

import github.danicastroo.view.Scenes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar directamente el FXML de MAIN sin depender de AppController
        Parent root = FXMLLoader.load(getClass().getResource(Scenes.MAIN.getUrl()));

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Prueba MAIN");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
