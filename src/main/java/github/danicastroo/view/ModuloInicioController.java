package github.danicastroo.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModuloInicioController extends Controller implements Initializable {
    @FXML
    private WebView redes;

    @Override
    public void onOpen(Object input) throws IOException {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cargar el archivo redes.html en el WebView
        try {
            URL url = getClass().getResource("/github/danicastroo/images/redes.html");
            if (url != null) {
                redes.getEngine().load(url.toExternalForm());
            } else {
                System.err.println("No se pudo encontrar el archivo redes.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
