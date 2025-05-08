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

        scene = new Scene(view.scene, 900, 600);
        stage.centerOnScreen(); // Centra la ventana en la pantalla
        stage.setResizable(true); // Permite redimensionar la ventana

        currentController=(AppController) view.controller;
        currentController.onOpen(null);
        stage.setScene(scene);
        stage.setTitle("CasTrack - Software de gestión de protectoras");
        //Icono del programa
        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/github/danicastroo/images/logo.png")));


        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }

}