package github.danicastroo.view;

import github.danicastroo.App;
import github.danicastroo.model.dao.TrabajadorDAO;
import github.danicastroo.model.entity.Trabajador;
import github.danicastroo.model.singleton.UserSession;
import github.danicastroo.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

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


    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Detectar la tecla Enter en el campo de contraseña
        ContrasenaField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    btnIniciarSesion.fire(); // Simula un clic en el botón
                    break;
                default:
                    break;
            }
        });

        // Cargar el contenido del WebView
        cargarRatoncito();


    }

    @FXML
    private void cargarRatoncito(){
        String htmlPath = getClass().getResource("/github/danicastroo/images/ratoncito.html").toExternalForm();
        ratoncito.setStyle("-fx-background-color: transparent;");
        ratoncito.setOpacity(1.0); // Asegura que sea visible
        ratoncito.getEngine().load(htmlPath);
    }


    @FXML
    private void cambiarUsuario() throws IOException {
        App.currentController.changeSceneFullScreen(Scenes.MAIN, null, true);
    }

    @FXML
    public void cambiarInicio() throws IOException {
        App.currentController.changeScene(Scenes.WELCOME, null);
    }

    @FXML
    public void cambiarRegistro() throws IOException {
        App.currentController.changeScene(Scenes.REGISTRO, null);
    }

    @FXML
    private void login() throws SQLException, IOException {
        String email = CorreoField.getText().trim();
        String password = ContrasenaField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Utils.ShowAlert("Falta algún campo por introducir.");
            return;
        }

        String hashedPassword = Utils.encryptSHA256(password);

        try {
            Trabajador trabajador = trabajadorDAO.checkLogin(email, hashedPassword);

            if (trabajador != null) {
                UserSession.login(trabajador); // Guarda el trabajador autenticado en la sesión
                Utils.ShowAlert("Inicio de sesión exitoso. Bienvenido, " + trabajador.getNombre());
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
