package github.danicastroo.view;

import github.danicastroo.model.entity.Trabajador;
import github.danicastroo.model.singleton.UserSession;
import github.danicastroo.utils.Utils;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador principal para la pantalla Main.
 * Gestiona la navegación entre módulos y el cierre de sesión.
 */
public class PantallaMainController extends Controller implements Initializable {
    @FXML
    private ImageView fondo; // Imagen de fondo con animación

    @FXML
    private Button btnCerrarSesion; // Botón para cerrar sesión

    @FXML
    private Button btnModuloInicio; // Botón para mostrar módulo inicio

    @FXML
    private Button btnModuloAnimales; // Botón para mostrar módulo animales

    @FXML
    private Button btnModuloAdoptantes; // Botón para mostrar módulo adoptantes

    @FXML
    private StackPane stackPane; // Contenedor donde se cargan las vistas de los módulos

    @FXML
    private Label usuarioLabel; // Etiqueta para mostrar el nombre del usuario conectado


    /**
     * Método que se ejecuta al abrir esta pantalla.
     * Muestra el nombre del trabajador conectado.
     *
     * @param input Parámetro de entrada (no usado aquí).
     * @throws IOException si hay error al cargar datos.
     */
    @Override
    public void onOpen(Object input) throws IOException {
        Trabajador usuario = UserSession.getUser(); // Obtiene usuario activo
        if (usuario != null) {
            usuarioLabel.setText("Trabajador: " + usuario.getNombre()); // Muestra nombre en la etiqueta
        }
    }

    /**
     * Método que se ejecuta al cerrar esta pantalla.
     * Actualmente no realiza ninguna acción.
     *
     * @param output Parámetro de salida (no usado aquí).
     */
    @Override
    public void onClose(Object output) {
        // No se realiza ninguna acción al cerrar
    }


    /**
     * Método que se ejecuta al inicializar el controlador.
     * Carga la vista inicial y anima la imagen de fondo.
     *
     * @param url URL de recursos (no usado aquí).
     * @param resourceBundle Recursos locales (no usados aquí).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarVista(Scenes.MODULO_INICIO); // Carga la vista de inicio por defecto

        // Coloca el ImageView "fondo" fuera de la pantalla a la izquierda
        fondo.setTranslateX(-400); // Ajustar valor según diseño

        // Crea una animación para deslizar el fondo a su posición original (x=0)
        TranslateTransition slideIn = new TranslateTransition(Duration.seconds(1), fondo);
        slideIn.setToX(0); // Posición final en X
        slideIn.setInterpolator(Interpolator.EASE_OUT); // Suaviza la animación
        slideIn.play(); // Ejecuta la animación
    }

    /**
     * Método para cerrar sesión y salir de la aplicación.
     * Pregunta confirmación al usuario antes de cerrar.
     */
    public void cerrarSesion() {
        boolean confirmar = mostrarConfirmacion(); // Pregunta al usuario

        if (confirmar) {
            // Cierra la aplicación
            Platform.exit();
            System.exit(0);
            System.out.println("CasTrack cerrando sesión con usuario: " + UserSession.getUser().getNombre());
        }
    }

    /**
     * Método privado que carga una vista FXML en el StackPane principal.
     *
     * @param scene Enum Scenes que contiene la URL del FXML a cargar.
     */
    private void cargarVista(Scenes scene) {
        try {
            // Obtiene la URL del archivo FXML
            URL fxmlLocation = getClass().getResource(scene.getUrl());
            System.out.println(" >> PANTALLA ABIERTA: " + fxmlLocation);

            // Valida que el archivo exista
            if (fxmlLocation == null) {
                throw new IOException("No se pudo encontrar el archivo FXML: " + scene.getUrl());
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load(); // Carga la vista

            // Reemplaza el contenido actual del StackPane con la nueva vista
            stackPane.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra un cuadro de diálogo para confirmar el cierre de sesión.
     *
     * @return true si el usuario confirma, false si cancela.
     */
    private boolean mostrarConfirmacion() {
        Alert mensaje = new Alert(Alert.AlertType.CONFIRMATION);
        mensaje.setTitle("Cerrar sesión");
        mensaje.setHeaderText("¿Seguro que deseas cerrar sesión?");
        mensaje.setContentText("Se cerrará el programa.");
        mensaje.initOwner((Stage) fondo.getScene().getWindow()); // Asocia la ventana padre

        // Muestra el diálogo y espera respuesta del usuario
        ButtonType resultado = mensaje.showAndWait().orElse(ButtonType.CANCEL);

        // Retorna true solo si el usuario presionó OK
        return resultado == ButtonType.OK;
    }

    /**
     * Método enlazado al botón para mostrar el módulo de inicio.
     */
    @FXML
    private void mostrarInicio() {
        cargarVista(Scenes.MODULO_INICIO);
    }

    /**
     * Método enlazado al botón para mostrar el módulo de animales.
     */
    @FXML
    private void mostrarAnimales() {
        cargarVista(Scenes.MODULO_ANIMALES);
    }

    /**
     * Método enlazado al botón para mostrar el módulo de adoptantes.
     */
    @FXML
    private void mostrarAdoptantes() {
        cargarVista(Scenes.MODULO_ADOPTANTES);
    }
}
