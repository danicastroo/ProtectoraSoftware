package github.danicastroo;


import github.danicastroo.view.AppController;
import github.danicastroo.view.Scenes;
import github.danicastroo.view.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**

 JavaFX App*/
public class App extends Application {
    public static Scene scene ;
    public static Stage stage;
    public static AppController currentController;


    @Override
    public void start(Stage stage) throws IOException {
        View view = AppController.loadFXML(Scenes.ROOT);

        scene = new Scene(view.scene,1920,1080);
        currentController=(AppController) view.controller;
        currentController.onOpen(null);
        stage.setScene(scene);
        stage.setTitle("Protectora"); // Opcional, tu título
        stage.setResizable(false);    // Opcional, bloquea el cambio de tamaño
        stage.show();
    }




    public static void main(String[] args) {
        launch();
    }

}