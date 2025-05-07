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
    }

    @FXML
    private void cambiarUsuario() throws IOException {
        App.currentController.changeScene(Scenes.MAIN, null);
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
        // Obtener datos ingresados por el usuario
        String email = CorreoField.getText().trim();
        String password = ContrasenaField.getText().trim();

        // Validar que los campos no estén vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Utils.ShowAlert("Falta algún campo por introducir.");
            return;
        }

        // Encriptar la contraseña ingresada para compararla con la base de datos
        String hashedPassword = Utils.encryptSHA256(password);

        try {
            // Verificar las credenciales utilizando el DAO
            Trabajador trabajador = trabajadorDAO.checkLogin(email, hashedPassword);

            if (trabajador != null) {
                // Inicio de sesión exitoso, guardar datos en la sesión y mostrar mensaje
                UserSession.login(email, hashedPassword);
                Utils.ShowAlert("Inicio de sesión exitoso. Bienvenido, " + trabajador.getNombre());
                cambiarUsuario(); // Cambiar a la escena correspondiente al inicio exitoso
            } else {
                // Credenciales inválidas, se interrumpe inicio de sesión
                UserSession.logout();
                Utils.ShowAlert("Correo o contraseña incorrectos. Inténtelo de nuevo.");
            }
        } catch (SQLException e) {
            // Manejo de errores en la base de datos
            Utils.ShowAlert("Error al intentar iniciar sesión. Por favor, inténtelo más tarde.");
            e.printStackTrace();
        }
    }


}
