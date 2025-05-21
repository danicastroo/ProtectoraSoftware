package github.danicastroo.view;

import github.danicastroo.App;
import github.danicastroo.model.dao.TrabajadorDAO;
import github.danicastroo.model.entity.Trabajador;
import github.danicastroo.model.singleton.UserSession;
import github.danicastroo.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InicioSesionController extends Controller implements Initializable {
    @FXML
    private Text btnRegistrarse;
    @FXML
    private Text btnAtras;
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private TextField CorreoField;
    @FXML
    private TextField ContrasenaField;
    @FXML
    private WebView ratoncito;

    private TrabajadorDAO trabajadorDAO;

    public InicioSesionController() {
        this.trabajadorDAO = new TrabajadorDAO();
    }

    /**
     * Se ejecuta al abrir esta vista.
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
        // Detectar tecla Enter en el campo de contraseña para disparar el login
        ContrasenaField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    btnIniciarSesion.fire();
                    break;
                default:
                    break;
            }
        });

        // Cargar contenido del WebView
        cargarRatoncito();
    }

    /**
     * Carga el archivo HTML del ratoncito en el WebView.
     */
    @FXML
    private void cargarRatoncito(){
        String htmlPath = getClass().getResource("/github/danicastroo/images/ratoncito.html").toExternalForm();
        ratoncito.setStyle("-fx-background-color: transparent;");
        ratoncito.setOpacity(1.0);
        ratoncito.getEngine().load(htmlPath);
    }

    /**
     * Cambia a la escena principal en modo pantalla completa.
     * @throws IOException si falla la carga de la escena.
     */
    @FXML
    private void cambiarUsuario() throws IOException {
        App.currentController.changeSceneFullScreen(Scenes.MAIN, null, true);
    }

    /**
     * Cambia a la escena de bienvenida.
     * @throws IOException si falla la carga de la escena.
     */
    @FXML
    public void cambiarInicio() throws IOException {
        App.currentController.changeScene(Scenes.WELCOME, null);
    }

    /**
     * Cambia a la escena de registro.
     * @throws IOException si falla la carga de la escena.
     */
    @FXML
    public void cambiarRegistro() throws IOException {
        App.currentController.changeScene(Scenes.REGISTRO, null);
    }

    /**
     * Maneja el proceso de login del usuario.
     * Valida los datos, encripta la contraseña y verifica credenciales con la base de datos.
     * @throws SQLException si hay error en la consulta a la base de datos.
     * @throws IOException si falla la carga de la siguiente escena.
     */
    @FXML
    private void login() throws SQLException, IOException {
        boolean isValid = true;

        String email = CorreoField.getText().trim();
        String password = ContrasenaField.getText().trim();

        // Validar campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Utils.ShowAlert("Falta algún campo por introducir.");
            isValid = false;
        }

        // Validar formato del correo
        if (isValid && !Utils.EmailValidator.isValid(email)) {
            Utils.Alert("Correo inválido", "El correo ingresado no es válido.", "Por favor, introduce un correo válido.", Alert.AlertType.ERROR, null);
            isValid = false;
        }

        if (isValid) {
            // Encriptar la contraseña
            String hashedPassword = Utils.encryptSHA256(password);

            try {
                // Verificar credenciales
                Trabajador trabajador = trabajadorDAO.checkLogin(email, hashedPassword);

                if (trabajador != null) {
                    UserSession.login(trabajador);
                    Utils.ShowAlert("Inicio de sesión exitoso. Bienvenido, " + trabajador.getNombre());
                    System.out.println("Inicio de sesión exitoso. Usuario: " + trabajador.getNombre());
                    cambiarUsuario();
                } else {
                    UserSession.logout();
                    Utils.ShowAlert("Correo o contraseña incorrectos. Inténtelo de nuevo.");
                }
            } catch (SQLException e) {
                Utils.ShowAlert("Error al intentar iniciar sesión. Por favor, inténtelo más tarde.");
                e.printStackTrace();
            }
        }
    }
}
