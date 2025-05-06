package github.danicastroo.view;

import github.danicastroo.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
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


}
