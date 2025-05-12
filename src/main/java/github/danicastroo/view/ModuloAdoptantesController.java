package github.danicastroo.view;

import github.danicastroo.model.dao.AdoptanteDAO;
import github.danicastroo.model.dao.AnimalDAO;
import github.danicastroo.model.entity.Adopta;
import github.danicastroo.model.entity.Adoptante;
import github.danicastroo.model.entity.Animal;
import github.danicastroo.model.entity.EstadoAnimal;
import github.danicastroo.model.singleton.UserSession;
import github.danicastroo.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModuloAdoptantesController extends Controller implements Initializable {
    @FXML
    private TextField direccionField;
    @FXML
    private ComboBox<Animal> animalesComboBox;
    @FXML
    private TextField nombreField, telefonoField, emailField, observacionesField;
    @FXML
    private Button guardarAdoptanteButton;

    private final AnimalDAO animalDAO = new AnimalDAO();
    private final AdoptanteDAO adoptanteDAO = new AdoptanteDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarAnimalesAdoptados();
        guardarAdoptanteButton.setOnAction(event -> guardarAdoptante());
    }

    private void cargarAnimalesAdoptados() {
        try {
            // Obtener el usuario conectado
            int idTrabajador = UserSession.getUser().getIdTrabajador();

            // Filtrar los animales adoptados asignados al usuario conectado
            List<Animal> animalesAdoptados = new ArrayList<>();
            for (Animal animal : animalDAO.findAll()) {
                if (animal.getEstado() == EstadoAnimal.ADOPTADO) {
                    animalesAdoptados.add(animal);
                }
            }

            // Actualizar el ComboBox
            animalesComboBox.getItems().clear();
            if (animalesAdoptados.isEmpty()) {
                Utils.Alert("Información", "No hay animales adoptados disponibles para este usuario", "Por favor, verifica los datos.", Alert.AlertType.INFORMATION);
            } else {
                animalesComboBox.getItems().addAll(animalesAdoptados);
            }
        } catch (Exception e) {
            Utils.Alert("Error", "Error al cargar los animales adoptados", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarAdoptante() {
        try {
            // Validar campos obligatorios
            String nombre = nombreField.getText().trim();
            String telefono = telefonoField.getText().trim();
            String email = emailField.getText().trim();
            String direccion = direccionField.getText().trim(); // Obtener dirección
            String observaciones = observacionesField.getText().trim();
            Animal animalSeleccionado = animalesComboBox.getValue();

            if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || direccion.isEmpty() || animalSeleccionado == null) {
                throw new IllegalArgumentException("Todos los campos obligatorios deben estar llenos.");
            }

            // Validar formato del email
            if (!Utils.EmailValidator.isValid(email)) {
                throw new IllegalArgumentException("El formato del correo electrónico no es válido.");
            }

            // Crear y guardar el adoptante
            Adoptante adoptante = new Adoptante(0, nombre, telefono, email, direccion, animalSeleccionado.getIdAnimal(), observaciones);
            adoptanteDAO.save(adoptante);

            Utils.Alert("Éxito", "Adoptante guardado", "El adoptante ha sido asignado correctamente.", Alert.AlertType.INFORMATION);

            // Limpiar los campos después de guardar
            limpiarCampos();
        } catch (IllegalArgumentException e) {
            Utils.Alert("Error", "Error al guardar el adoptante", e.getMessage(), Alert.AlertType.ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.Alert("Error", "Error al guardar el adoptante", "Ocurrió un error al guardar en la base de datos.", Alert.AlertType.ERROR);
        }
    }

    private void asociarAdoptanteConAnimal() {
        try {
            // Validar que los campos no estén vacíos
            if (animalesComboBox.getValue() == null || nombreField.getText().isEmpty() ||
                    telefonoField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    direccionField.getText().isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }

            // Validar el formato del email
            if (!Utils.EmailValidator.isValid(emailField.getText())) {
                throw new IllegalArgumentException("El correo electrónico no es válido.");
            }

            // Obtener el animal seleccionado
            Animal animal = animalesComboBox.getValue();
            if (animal.getEstado() != EstadoAnimal.ADOPTADO) {
                throw new IllegalArgumentException("El animal seleccionado no está adoptado.");
            }

            // Crear el objeto Adoptante
            Adoptante adoptante = new Adoptante();
            adoptante.setNombre(nombreField.getText());
            adoptante.setTelefono(telefonoField.getText());
            adoptante.setEmail(emailField.getText());
            adoptante.setDireccion(direccionField.getText());
            adoptante.setIdAnimal(animal.getIdAnimal());
            adoptante.setObservaciones(observacionesField.getText());

            // Guardar el adoptante en la base de datos
            AdoptanteDAO adoptanteDAO = new AdoptanteDAO();
            adoptanteDAO.save(adoptante);

            // Mostrar mensaje de éxito
            Utils.ShowAlert("Adoptante asociado correctamente al animal.");

            // Limpiar los campos
            limpiarCampos();
        } catch (IllegalArgumentException e) {
            Utils.Alert("Advertencia", "Datos inválidos", e.getMessage(), Alert.AlertType.WARNING);
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.Alert("Error", "Error al guardar el adoptante", "Ocurrió un error al guardar en la base de datos.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        nombreField.clear();
        telefonoField.clear();
        emailField.clear();
        direccionField.clear(); // Limpiar dirección
        observacionesField.clear();
        animalesComboBox.setValue(null);
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