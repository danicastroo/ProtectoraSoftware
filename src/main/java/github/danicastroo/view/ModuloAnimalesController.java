package github.danicastroo.view;

import github.danicastroo.model.dao.AnimalDAO;
import github.danicastroo.model.dao.CuidaDAO;
import github.danicastroo.model.entity.Animal;
import github.danicastroo.model.entity.Cuida;
import github.danicastroo.model.entity.EstadoAnimal;
import github.danicastroo.model.entity.TipoAnimal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModuloAnimalesController extends Controller implements Initializable {

    @FXML
    private TextField nombreField, edadField, microchipField, observacionesField, tipoCuidadoField;

    @FXML
    private ComboBox<TipoAnimal> tipoComboBox;

    @FXML
    private ComboBox<EstadoAnimal> estadoComboBox;

    @FXML
    private DatePicker fechaAdopcionPicker;

    @FXML
    private Button guardarAnimalButton, guardarCuidadoButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoComboBox.getItems().setAll(TipoAnimal.values());
        estadoComboBox.getItems().setAll(EstadoAnimal.values());

        guardarAnimalButton.setOnAction(event -> guardarAnimal());
        guardarCuidadoButton.setOnAction(event -> guardarCuidado());
    }

    private void guardarAnimal() {
        try {
            String nombre = nombreField.getText();
            int edad = Integer.parseInt(edadField.getText());
            TipoAnimal tipo = tipoComboBox.getValue();
            EstadoAnimal estado = estadoComboBox.getValue();
            LocalDate fechaAdopcion = fechaAdopcionPicker.getValue();
            int microchip = Integer.parseInt(microchipField.getText());

            Animal animal = new Animal(0, nombre, microchip, edad, tipo, fechaAdopcion);
            animal.setEstado(estado);

            AnimalDAO animalDAO = new AnimalDAO();
            animalDAO.save(animal);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Animal guardado correctamente.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al guardar el animal.");
            alert.showAndWait();
        }
    }

    private void guardarCuidado() {
        try {
            int idAnimal = Integer.parseInt(observacionesField.getText()); // Ejemplo: obtener ID del campo de texto
            int idTrabajador = 1; // Ejemplo: ID del trabajador actual (puedes reemplazarlo con lógica real)
            String observaciones = observacionesField.getText();
            String tipoCuidado = tipoCuidadoField.getText();

            Cuida cuida = new Cuida(idAnimal, idTrabajador, observaciones, tipoCuidado);

            CuidaDAO cuidaDAO = new CuidaDAO();
            cuidaDAO.save(cuida);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cuidado guardado correctamente.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al guardar el cuidado.");
            alert.showAndWait();
        }
    }

    @Override
    public void onOpen(Object input) throws IOException {
        // Implementación personalizada si es necesario
    }

    @Override
    public void onClose(Object output) {
        // Implementación personalizada si es necesario
    }
}