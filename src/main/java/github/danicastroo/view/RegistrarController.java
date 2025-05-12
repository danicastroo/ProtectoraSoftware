package github.danicastroo.view;

import github.danicastroo.App;
import github.danicastroo.model.dao.TrabajadorDAO;
import github.danicastroo.model.entity.Trabajador;
import github.danicastroo.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrarController extends Controller implements Initializable {
    @FXML
    private Button btnRegistrarse;
    @FXML
    private Text btnAtras;
    @FXML
    private Text btnIniciarSesion;
    @FXML
    private TextField CorreoField;
    @FXML
    private TextField ContrasenaField;
    @FXML
    private TextField NombreField;


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
    public void cambiarInicio() throws IOException {
        App.currentController.changeScene(Scenes.WELCOME, null);
    }

    @FXML
    public void cambiarInicioSesion() throws IOException {
        App.currentController.changeScene(Scenes.INICIO_SESION, null);
    }

    @FXML
    private void addUsuario() {
        boolean isValid = true;

        // Instancia del DAO
        TrabajadorDAO userDAO = new TrabajadorDAO();

        // Obtener textos de los campos
        String nombre = NombreField.getText() != null ? NombreField.getText().trim() : "";
        String email = CorreoField.getText() != null ? CorreoField.getText().trim() : "";
        String password = ContrasenaField.getText() != null ? ContrasenaField.getText().trim() : "";

        // Validar el campo "Nombre"
        if (nombre.isEmpty()) {
            Utils.Alert("Error", "El campo de nombre está vacío", "El nombre es obligatorio. Por favor, complételo.", Alert.AlertType.ERROR);
            isValid = false;
        }

        // Validar el campo "Correo"
        if (isValid && email.isEmpty()) {
            Utils.Alert("Error", "El campo correo está vacío", "El correo es obligatorio. Por favor, complételo.", Alert.AlertType.ERROR);
            isValid = false;
        }

        if (isValid && !Utils.EmailValidator.isValid(email)) {
            Utils.Alert("Correo inválido", "El correo ingresado no es válido.", "Por favor, introduce un correo válido.", Alert.AlertType.ERROR);
            isValid = false;
        }

        // Validar el campo "Contraseña"
        if (isValid && password.isEmpty()) {
            Utils.Alert("Error", "El campo contraseña está vacío", "La contraseña es obligatoria. Por favor, complétela.", Alert.AlertType.ERROR);
            isValid = false;
        }

        if (isValid) {
            try {
                // Comprobar si el nombre ya existe
                if (userDAO.findByUsername(nombre) != null) {
                    Utils.Alert("Error", "Nombre Existente", "El nombre ya está en uso. Por favor, elige otro.", Alert.AlertType.ERROR);
                    isValid = false;
                }

                if (isValid && userDAO.isEmailRegistered(email)) {
                    Utils.Alert("Error", "Correo ya registrado", "El correo electrónico ya está en uso. Por favor, usa otro.", Alert.AlertType.ERROR);
                    isValid = false;
                }

                if (isValid) {
                    // Encriptar la contraseña antes de guardarla
                    password = Utils.encryptSHA256(password);

                    // Crear y guardar el objeto Trabajador
                    Trabajador trabajador = new Trabajador(nombre, 0, null, email, password);
                    userDAO.save(trabajador);

                    // Notificar registro exitoso
                    Utils.Alert("Registro Exitoso", "Usuario Registrado", "El usuario se ha registrado correctamente.", Alert.AlertType.INFORMATION);
                }
            } catch (SQLException e) {
                Utils.Alert("Error", "Error de base de datos", "No se pudo registrar al usuario: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

}
