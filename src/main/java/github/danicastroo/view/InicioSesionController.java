package github.danicastroo.view;

import github.danicastroo.App;
import github.danicastroo.model.dao.TrabajadorDAO;
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
        App.currentController.changeScene(Scenes.WELCOME, null);
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
        password = Utils.encryptSHA256(password);


        if(email.equals("") || password.equals("")) {
            Utils.ShowAlert("Falta algun campo por introducir");
        }else {
            TrabajadorDAO mDAO = new TrabajadorDAO();
            String nameUser;
            if((nameUser=mDAO.checkLogin(email, password))!=null) {
                UserSession.login(email, password);
                Utils.ShowAlert("Login exitoso, Se ha logeago el Usuario correctamente.");
                cambiarUsuario();
            }else {
                UserSession.logout();
                Utils.ShowAlert("No se ha podido logear, Intentelo de nuevo.");
            }

        }

    }
}
