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
    public void cambiarInicioSesion() throws IOException {
        App.currentController.changeScene(Scenes.INICIO_SESION, null);
    }

}
