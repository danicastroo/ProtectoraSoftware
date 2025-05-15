package github.danicastroo.view;

import github.danicastroo.model.entity.Trabajador;
import github.danicastroo.model.singleton.UserSession;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PantallaMainController extends Controller implements Initializable {
    @FXML
    private ImageView fondo;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnModuloInicio;

    @FXML
    private Button btnModuloAnimales;

    @FXML
    private Button btnModuloAdoptantes;

    @FXML
    private StackPane stackPane;

    @FXML
    private Label usuarioLabel;


    @Override
    public void onOpen(Object input) throws IOException {
        Trabajador usuario = UserSession.getUser();
        if (usuario != null) {
            usuarioLabel.setText("Trabajador: " + usuario.getNombre());
        }
    }

    @Override
    public void onClose(Object output) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarVista(Scenes.MODULO_INICIO); // Carga la vista de inicio por defecto
        // Coloca el ImageView fuera de la pantalla (por ejemplo, a la izquierda)
        fondo.setTranslateX(-400); // Ajusta según sea necesario

        // Crea una animación para que se deslice a su posición original
        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), fondo);
        slideIn.setToX(0);
        slideIn.setInterpolator(Interpolator.EASE_OUT);
        slideIn.play();

    }

    /**
     * Método para cerrar sesión y terminar el programa.
     */
    public void cerrarSesion() {
        boolean confirmar = mostrarConfirmacion();

        if (confirmar) {
            Platform.exit();
            System.exit(0);
            System.out.println("CasTrack cerrando sesión con usuario: " + UserSession.getUser().getNombre());
        }
    }

    private void cargarVista(Scenes scene) {
        try {
            URL fxmlLocation = getClass().getResource(scene.getUrl());
            System.out.println(" >> PANTALLA ABIERTA: " + fxmlLocation);
            if (fxmlLocation == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: " + scene.getUrl());
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load(); // Carga el FXML como un Parent
            stackPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método opcional para mostrar un cuadro de confirmación.
     * @return true si el usuario confirma, false en caso contrario.
     */
    private boolean mostrarConfirmacion() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Cerrar sesión");
        alerta.setHeaderText("¿Seguro que deseas cerrar sesión?");
        alerta.setContentText("Se cerrará el programa.");

        // Muestra el cuadro y espera la respuesta del usuario
        ButtonType resultado = alerta.showAndWait().orElse(ButtonType.CANCEL);

        // Retorna true si el usuario pulsa Aceptar; de lo contrario, false
        return resultado == ButtonType.OK;
    }

    @FXML
    private void mostrarInicio() {
        cargarVista(Scenes.MODULO_INICIO);
    }

    @FXML
    private void mostrarAnimales() {
        cargarVista(Scenes.MODULO_ANIMALES);
    }

    @FXML
    private void mostrarAdoptantes() {
        cargarVista(Scenes.MODULO_ADOPTANTES);
    }
}
