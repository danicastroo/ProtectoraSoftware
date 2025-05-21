package github.danicastroo.view;

import github.danicastroo.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InicioController extends Controller implements Initializable {
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Button btnRegistrar;

    /**
     * Se ejecuta cuando se abre esta vista.
     * @param input Objeto de entrada, no se usa aquí.
     * @throws IOException en caso de error al abrir recursos.
     */
    @Override
    public void onOpen(Object input) throws IOException {
        // No se necesita implementación por ahora
    }

    /**
     * Se ejecuta al cerrar esta vista.
     * @param output Objeto de salida, no se usa aquí.
     */
    @Override
    public void onClose(Object output) {
        // No se necesita implementación por ahora
    }

    /**
     * Inicializa la vista después de cargar el FXML.
     * @param url URL usado para la localización, no se usa aquí.
     * @param resourceBundle Recursos usados para internacionalización, no se usa aquí.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización adicional si se requiere
    }

    /**
     * Método para cambiar a la escena de inicio de sesión.
     * @throws IOException si falla la carga de la escena.
     */
    @FXML
    public void cambiarInicioSesion() throws IOException {
        App.currentController.changeScene(Scenes.INICIO_SESION, null);
    }

    /**
     * Método para cambiar a la escena de registro.
     * @throws IOException si falla la carga de la escena.
     */
    @FXML
    public void cambiarRegistro() throws IOException {
        App.currentController.changeScene(Scenes.REGISTRO, null);
    }
}
