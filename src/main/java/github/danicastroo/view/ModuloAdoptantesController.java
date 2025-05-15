package github.danicastroo.view;

import github.danicastroo.model.connection.ConnectionDB;
import github.danicastroo.model.dao.AdoptaDAO;
import github.danicastroo.model.dao.AdoptanteDAO;
import github.danicastroo.model.dao.AnimalDAO;
import github.danicastroo.model.dao.CuidaDAO;
import github.danicastroo.model.entity.Adopta;
import github.danicastroo.model.entity.Adoptante;
import github.danicastroo.model.entity.Animal;
import github.danicastroo.model.entity.EstadoAnimal;
import github.danicastroo.model.interfaces.InterfaceAdoptaDAO;
import github.danicastroo.model.singleton.UserSession;
import github.danicastroo.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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

    @FXML
    private TableView <Adoptante> tablaAdoptantes;

    @FXML
    private TableColumn<Adoptante, String> colNombre, colTelefono, colEmail, colDireccion, colAnimal;

    private final AnimalDAO animalDAO = new AnimalDAO();
    private final AdoptanteDAO adoptanteDAO = new AdoptanteDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();
        cargarAnimalesAdoptados();
        cargarAnimalesAdoptados();
        guardarAdoptanteButton.setOnAction(event -> guardarAdoptante());
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colDireccion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));
        colAnimal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getIdAnimal() > 0 ? obtenerNombreAnimal(data.getValue().getIdAnimal()) : "N/A"));

        // Ajustar el ancho de las columnas
        colNombre.setPrefWidth(150);
        colTelefono.setPrefWidth(120);
        colEmail.setPrefWidth(200);
        colDireccion.setPrefWidth(180);
        colAnimal.setPrefWidth(150);

        // Habilitar el ajuste automático del tamaño de las columnas
        tablaAdoptantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void cargarAnimalesAdoptados() {
        try {
            int idTrabajador = UserSession.getUser().getIdTrabajador(); // Obtén el ID del usuario actual
            List<Animal> animales = new ArrayList<>();

            // Obtener los IDs de los animales relacionados con el trabajador actual
            List<Integer> idsAnimalesRelacionados = new CuidaDAO().findAnimalIdsByTrabajadorId(idTrabajador);
            for (int idAnimal : idsAnimalesRelacionados) {
                Animal animal = animalDAO.findById(idAnimal);
                if (animal != null && animal.getEstado() == EstadoAnimal.ADOPTADO) {
                    animales.add(animal);
                }
            }

            animalesComboBox.getItems().clear();
            animalesComboBox.getItems().addAll(animales);
        } catch (Exception e) {
            Utils.Alert("Error", "Error al cargar los animales adoptados", e.getMessage(), Alert.AlertType.ERROR, (Stage) tablaAdoptantes.getScene().getWindow());
            e.printStackTrace();
        }
    }

    @FXML
    private void guardarAdoptante() {
        try (Connection conn = ConnectionDB.getConnection()) {
            conn.setAutoCommit(false); // Inicia la transacción

            // Validar campos obligatorios
            String nombre = nombreField.getText().trim();
            String telefono = telefonoField.getText().trim();
            String email = emailField.getText().trim();
            String direccion = direccionField.getText().trim();
            String observaciones = observacionesField.getText().trim();
            Animal animalSeleccionado = animalesComboBox.getValue();

            if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || direccion.isEmpty() || animalSeleccionado == null) {
                throw new IllegalArgumentException("Todos los campos obligatorios deben estar llenos.");
            }

            // Validar formato del email
            if (!Utils.EmailValidator.isValid(email)) {
                throw new IllegalArgumentException("El formato del correo electrónico no es válido.");
            }

            // Verificar estado del animal
            if (!animalSeleccionado.getEstado().name().equalsIgnoreCase("ADOPTADO")) {
                throw new IllegalArgumentException("El animal seleccionado no está disponible para adopción.");
            }

            // Crear y guardar el adoptante
            Adoptante adoptante = new Adoptante(0, nombre, telefono, email, direccion, animalSeleccionado.getIdAnimal(), observaciones);
            adoptanteDAO.save(adoptante);

            // Crear y guardar la adopción
            Adopta adopta = new Adopta(0, adoptante.getIdAdoptante(), animalSeleccionado.getIdAnimal(), java.time.LocalDate.now(), observaciones);
            new AdoptaDAO().save(adopta);

            conn.commit(); // Confirma la transacción

            Utils.Alert("Éxito", "Adoptante guardado", "El adoptante y la adopción han sido registrados correctamente.", Alert.AlertType.INFORMATION, (Stage) tablaAdoptantes.getScene().getWindow());

            limpiarCampos();
            cargarAdoptantes(); // Actualiza la tabla
        } catch (IllegalArgumentException e) {
            Utils.Alert("Error", "Error al guardar el adoptante", e.getMessage(), Alert.AlertType.ERROR, (Stage) tablaAdoptantes.getScene().getWindow());
        } catch (SQLException e) {
            e.printStackTrace();
            Utils.Alert("Error", "Error al guardar el adoptante", "Ocurrió un error al guardar en la base de datos.", Alert.AlertType.ERROR, (Stage) tablaAdoptantes.getScene().getWindow());
        }
    }

    private String obtenerNombreAnimal(int idAnimal) {
        Animal animal = animalDAO.findById(idAnimal);
        return animal != null ? animal.getNombre() : "Desconocido";
    }

    private void cargarAdoptantes() {
        try {
            List<Adoptante> adoptantes = adoptanteDAO.findAll();
            tablaAdoptantes.getItems().clear();
            tablaAdoptantes.getItems().addAll(adoptantes);
        } catch (SQLException e) {
            Utils.Alert("Error", "Error al cargar los adoptantes", e.getMessage(), Alert.AlertType.ERROR, (Stage) tablaAdoptantes.getScene().getWindow());
            e.printStackTrace();
        }
    }


    private void limpiarCampos() {
        nombreField.clear();
        telefonoField.clear();
        emailField.clear();
        direccionField.clear();
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