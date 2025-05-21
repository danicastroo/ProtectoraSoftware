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

    /**
     * Método llamado al abrir la vista. Implementación personalizada si es necesario.
     *
     * @param input objeto de entrada opcional
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    public void onOpen(Object input) throws IOException {
        // Implementación personalizada si es necesario
    }

    /**
     * Método llamado al cerrar la vista. Implementación personalizada si es necesario.
     *
     * @param output objeto de salida opcional
     */
    @Override
    public void onClose(Object output) {
        // Implementación personalizada si es necesario
    }

    /**
     * Inicializa el controlador y carga el archivo redes.html en el WebView.
     *
     * @param location        la ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si no se conoce
     * @param resources       el recurso de internacionalización, o null si no se utiliza
     */
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
