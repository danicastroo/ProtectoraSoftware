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

/**
 * Controlador para la pantalla de registro de usuarios (Trabajadores).
 * Implementa Initializable para gestionar la inicialización del controlador.
 */
public class RegistrarController extends Controller implements Initializable {

    @FXML
    private Button btnRegistrarse;         // Botón para registrar un nuevo usuario

    @FXML
    private Text btnAtras;                 // Texto clickable para regresar a la pantalla anterior

    @FXML
    private Text btnIniciarSesion;         // Texto clickable para ir a la pantalla de inicio de sesión

    @FXML
    private TextField CorreoField;         // Campo de texto para ingresar el correo electrónico

    @FXML
    private TextField ContrasenaField;     // Campo de texto para ingresar la contraseña

    @FXML
    private TextField NombreField;         // Campo de texto para ingresar el nombre del usuario


    /**
     * Método llamado cuando se abre la pantalla, recibe un objeto como parámetro (no usado aquí).
     */
    @Override
    public void onOpen(Object input) throws IOException {
        // No se implementa lógica específica al abrir esta vista
    }

    /**
     * Método llamado cuando se cierra la pantalla, recibe un objeto como parámetro (no usado aquí).
     */
    @Override
    public void onClose(Object output) {
        // No se implementa lógica específica al cerrar esta vista
    }

    /**
     * Inicialización del controlador.
     * Aquí se podría configurar lógica previa a la interacción del usuario.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // No se implementa lógica adicional en la inicialización
    }

    /**
     * Cambia la escena a la pantalla de bienvenida.
     * @throws IOException si falla la carga del recurso FXML.
     */
    @FXML
    public void cambiarInicio() throws IOException {
        App.currentController.changeScene(Scenes.WELCOME, null);
    }

    /**
     * Cambia la escena a la pantalla de inicio de sesión.
     * @throws IOException si falla la carga del recurso FXML.
     */
    @FXML
    public void cambiarInicioSesion() throws IOException {
        App.currentController.changeScene(Scenes.INICIO_SESION, null);
    }

    /**
     * Método que se ejecuta al presionar el botón de registrarse.
     * Valida los campos del formulario, verifica que el usuario no exista,
     * encripta la contraseña y registra un nuevo Trabajador en la base de datos.
     */
    @FXML
    private void addUsuario() {
        boolean isValid = true; // Controla si los datos son válidos para continuar

        // Instancia el DAO para acceder a la base de datos
        TrabajadorDAO userDAO = new TrabajadorDAO();

        // Obtiene el texto ingresado en cada campo y elimina espacios al inicio y final
        String nombre = NombreField.getText() != null ? NombreField.getText().trim() : "";
        String email = CorreoField.getText() != null ? CorreoField.getText().trim() : "";
        String password = ContrasenaField.getText() != null ? ContrasenaField.getText().trim() : "";

        // Validación: Nombre no puede estar vacío
        if (nombre.isEmpty()) {
            Utils.Alert("Error", "El campo de nombre está vacío", "El nombre es obligatorio. Por favor, complételo.", Alert.AlertType.ERROR, null);
            isValid = false;
        }

        // Validación: Correo no puede estar vacío si el nombre es válido
        if (isValid && email.isEmpty()) {
            Utils.Alert("Error", "El campo correo está vacío", "El correo es obligatorio. Por favor, complételo.", Alert.AlertType.ERROR, null);
            isValid = false;
        }

        // Validación: Correo debe tener formato válido si el correo no está vacío
        if (isValid && !Utils.EmailValidator.isValid(email)) {
            Utils.Alert("Correo inválido", "El correo ingresado no es válido.", "Por favor, introduce un correo válido.", Alert.AlertType.ERROR, null);
            isValid = false;
        }

        // Validación: Contraseña no puede estar vacía si los anteriores campos son válidos
        if (isValid && password.isEmpty()) {
            Utils.Alert("Error", "El campo contraseña está vacío", "La contraseña es obligatoria. Por favor, complétela.", Alert.AlertType.ERROR, null);
            isValid = false;
        }

        // Si los datos pasaron todas las validaciones
        if (isValid) {
            try {
                // Verifica si el nombre de usuario ya existe en la base de datos
                if (userDAO.findByUsername(nombre) != null) {
                    Utils.Alert("Error", "Nombre Existente", "El nombre ya está en uso. Por favor, elige otro.", Alert.AlertType.ERROR, null);
                    isValid = false;
                }

                // Verifica si el correo ya está registrado
                if (isValid && userDAO.isEmailRegistered(email)) {
                    Utils.Alert("Error", "Correo ya registrado", "El correo electrónico ya está en uso. Por favor, usa otro.", Alert.AlertType.ERROR, null);
                    isValid = false;
                }

                // Si nombre y correo son válidos y no existen en la BD
                if (isValid) {
                    // Encripta la contraseña usando SHA-256 antes de guardarla
                    password = Utils.encryptSHA256(password);

                    // Crea un nuevo objeto Trabajador con los datos proporcionados
                    Trabajador trabajador = new Trabajador(nombre, 0, null, email, password);

                    // Guarda el nuevo Trabajador en la base de datos
                    userDAO.save(trabajador);

                    // Muestra un mensaje informando que el registro fue exitoso
                    Utils.Alert("Registro Exitoso", "Usuario Registrado", "El usuario se ha registrado correctamente.", Alert.AlertType.INFORMATION, null);
                }
            } catch (SQLException e) {
                // En caso de error con la base de datos, muestra un mensaje de error
                Utils.Alert("Error", "Error de base de datos", "No se pudo registrar al usuario: " + e.getMessage(), Alert.AlertType.ERROR, null);
                e.printStackTrace();
            }
        }
    }

}
