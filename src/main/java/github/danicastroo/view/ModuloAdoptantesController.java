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
    private Button eliminarButton;
    @FXML
    private Button editarButton;
    @FXML
    private ComboBox<Animal> animalesComboBox;
    @FXML
    private TextField nombreField, telefonoField, emailField, observacionesField;
    @FXML
    private Button guardarAdoptanteButton;

    @FXML
    private TableView <Adoptante> tablaAdoptantes;

    @FXML
    private TableColumn<Adoptante, String> colNombre, colTelefono, colEmail, colDireccion, colAnimal, colObservaciones;

    private final AnimalDAO animalDAO = new AnimalDAO();
    private final AdoptanteDAO adoptanteDAO = new AdoptanteDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();
        cargarAnimalesAdoptados();
        cargarAdoptantes();
        guardarAdoptanteButton.setOnAction(event -> guardarAdoptante());
        editarButton.setOnAction(event -> editarAdoptante());
        eliminarButton.setOnAction(event -> eliminarAdoptante());
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colDireccion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));
        colAnimal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getIdAnimal() > 0 ? obtenerNombreAnimal(data.getValue().getIdAnimal()) : "Ninguno"));
        colObservaciones.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getObservaciones()));

        // Ajustar el ancho de las columnas
        colNombre.setPrefWidth(150);
        colTelefono.setPrefWidth(120);
        colEmail.setPrefWidth(200);
        colDireccion.setPrefWidth(180);
        colAnimal.setPrefWidth(150);


        // Habilitar el ajuste automático del tamaño de las columnas
        tablaAdoptantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Dentro de ModuloAdoptantesController.java

    @FXML
    private void editarAdoptante() {
        Adoptante adoptanteSeleccionado = tablaAdoptantes.getSelectionModel().getSelectedItem();
        if (adoptanteSeleccionado == null) {
            Utils.Alert("Error", "Selección vacía", "Por favor, selecciona un adoptante para editar.", Alert.AlertType.WARNING, (Stage) tablaAdoptantes.getScene().getWindow());
            return;
        }

        // Cargar los datos del adoptante seleccionado en los campos
        nombreField.setText(adoptanteSeleccionado.getNombre());
        telefonoField.setText(adoptanteSeleccionado.getTelefono());
        emailField.setText(adoptanteSeleccionado.getEmail());
        direccionField.setText(adoptanteSeleccionado.getDireccion());
        observacionesField.setText(adoptanteSeleccionado.getObservaciones());
        animalesComboBox.setValue(animalDAO.findById(adoptanteSeleccionado.getIdAnimal()));

        // Cambiar el texto y la acción del botón "Guardar"
        guardarAdoptanteButton.setText("Actualizar Adoptante");
        guardarAdoptanteButton.setOnAction(event -> {
            actualizarAdoptante(adoptanteSeleccionado);
            guardarAdoptanteButton.setText("Guardar Adoptante");
            guardarAdoptanteButton.setOnAction(e -> guardarAdoptante());
        });
    }

    private void actualizarAdoptante(Adoptante adoptante) {
        try {
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
            if (!Utils.EmailValidator.isValid(email)) {
                throw new IllegalArgumentException("El formato del correo electrónico no es válido.");
            }

            // Actualizar los datos del adoptante
            adoptante.setNombre(nombre);
            adoptante.setTelefono(telefono);
            adoptante.setEmail(email);
            adoptante.setDireccion(direccion);
            adoptante.setObservaciones(observaciones);
            adoptante.setIdAnimal(animalSeleccionado.getIdAnimal());

            adoptanteDAO.save(adoptante);

            Utils.Alert("Éxito", "Adoptante actualizado", "El adoptante ha sido actualizado correctamente.", Alert.AlertType.INFORMATION, (Stage) tablaAdoptantes.getScene().getWindow());

            limpiarCampos();
            cargarAdoptantes();
        } catch (SQLException e) {
            Utils.Alert("Error", "Error al actualizar", "No se pudo actualizar el adoptante: " + e.getMessage(), Alert.AlertType.ERROR, (Stage) tablaAdoptantes.getScene().getWindow());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Utils.Alert("Error", "Error al actualizar", e.getMessage(), Alert.AlertType.ERROR, (Stage) tablaAdoptantes.getScene().getWindow());
        }
    }
    @FXML
    private void eliminarAdoptante() {
        Adoptante adoptanteSeleccionado = tablaAdoptantes.getSelectionModel().getSelectedItem();
        if (adoptanteSeleccionado == null) {
            Utils.Alert("Error", "Selección vacía", "Por favor, selecciona un adoptante para eliminar.", Alert.AlertType.WARNING, (Stage) tablaAdoptantes.getScene().getWindow());
            return;
        }

        // Mostrar cuadro de confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar este adoptante?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");
        confirmacion.initOwner((Stage) tablaAdoptantes.getScene().getWindow());

        // Esperar la respuesta del usuario
        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                adoptanteDAO.delete(adoptanteSeleccionado);
                Utils.Alert("Éxito", "Adoptante eliminado", "El adoptante ha sido eliminado correctamente.", Alert.AlertType.INFORMATION, (Stage) tablaAdoptantes.getScene().getWindow());
                cargarAdoptantes(); // Actualiza la tabla
            } catch (SQLException e) {
                Utils.Alert("Error", "Error al eliminar", "No se pudo eliminar el adoptante: " + e.getMessage(), Alert.AlertType.ERROR, (Stage) tablaAdoptantes.getScene().getWindow());
                e.printStackTrace();
            }
        }
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

    private void guardarAdoptante() {
        try {
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

            // Crear y guardar el adoptante
            Adoptante adoptante = new Adoptante(0, nombre, telefono, email, direccion, animalSeleccionado.getIdAnimal(), observaciones);
            adoptanteDAO.save(adoptante);

            // Crear la relación en adopta
            AdoptaDAO adoptaDAO = new AdoptaDAO();
            Adopta adopta = new Adopta();
            adopta.setIdAdoptante(adoptante.getIdAdoptante());
            adopta.setIdAnimal(animalSeleccionado.getIdAnimal());
            adopta.setFechaAdopcion(java.time.LocalDate.now());
            adopta.setObservaciones(observaciones);
            adoptaDAO.save(adopta);

            Utils.Alert("Éxito", "Adoptante guardado", "El adoptante ha sido registrado correctamente.", Alert.AlertType.INFORMATION, (Stage) tablaAdoptantes.getScene().getWindow());

            limpiarCampos();
            cargarAdoptantes(); // Actualiza la tabla
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en consola
            Utils.Alert("Error", "Error al guardar el adoptante", "Ocurrió un error al guardar en la base de datos:\n" + e.getMessage(), Alert.AlertType.ERROR, (Stage) tablaAdoptantes.getScene().getWindow());
        } catch (IllegalArgumentException e) {
            Utils.Alert("Error", "Error al guardar el adoptante", e.getMessage(), Alert.AlertType.ERROR, (Stage) tablaAdoptantes.getScene().getWindow());
        }
    }

    private String obtenerNombreAnimal(int idAnimal) {
        Animal animal = animalDAO.findById(idAnimal);
        return animal != null ? animal.getNombre() + " (Chip: " + animal.getChip() + ")" : "Desconocido";
    }

    private void cargarAdoptantes() {
        try {
            int idTrabajador = UserSession.getUser().getIdTrabajador();
            List<Adoptante> adoptantes = adoptanteDAO.findAllByTrabajador(idTrabajador);
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
    }

    @Override
    public void onClose(Object output) {
    }
}