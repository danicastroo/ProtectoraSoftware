package github.danicastroo.view;

import github.danicastroo.App;
import github.danicastroo.model.dao.AnimalDAO;
import github.danicastroo.model.dao.CuidaDAO;
import github.danicastroo.model.entity.*;
import github.danicastroo.model.singleton.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ModuloAnimalesController extends Controller implements Initializable {

    @FXML
    private GridPane listaAnimales;

    @FXML
    private TextField nombreField, edadField, microchipField, observacionesField, tipoCuidadoField;

    @FXML
    private ComboBox<TipoAnimal> tipoComboBox;

    @FXML
    private ComboBox<EstadoAnimal> estadoComboBox;

    @FXML
    private DatePicker fechaAdopcionPicker;

    @FXML
    private Button guardarAnimalButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarAnimales();
        tipoComboBox.getItems().setAll(TipoAnimal.values());
        estadoComboBox.getItems().setAll(EstadoAnimal.values());
        guardarAnimalButton.setOnAction(event -> guardarAnimal());
    }

    private void guardarAnimal() {
        try {
            // Validar que los campos no estén vacíos
            if (nombreField.getText().isEmpty() || edadField.getText().isEmpty() || microchipField.getText().isEmpty() ||
                    tipoCuidadoField.getText().isEmpty() || observacionesField.getText().isEmpty()) {
                throw new IllegalArgumentException("Todos los campos obligatorios deben estar llenos.");
            }

            // Validar que los valores numéricos sean correctos
            int edad = Integer.parseInt(edadField.getText());
            int microchip = Integer.parseInt(microchipField.getText());

            // Validar que se haya seleccionado un tipo y estado
            TipoAnimal tipo = tipoComboBox.getValue();
            EstadoAnimal estado = estadoComboBox.getValue();
            if (tipo == null || estado == null) {
                throw new IllegalArgumentException("Debe seleccionar un tipo y un estado para el animal.");
            }

            // Validar la fecha de adopción
            LocalDate fechaAdopcion = fechaAdopcionPicker.getValue();
            if (fechaAdopcion == null) {
                throw new IllegalArgumentException("Debe seleccionar una fecha de adopción.");
            }

            // Crear el objeto Animal
            String nombre = nombreField.getText();
            Animal animal = new Animal(0, nombre, microchip, edad, tipo, fechaAdopcion, estado);

            // Guardar el animal y la relación en Cuida
            try {
                AnimalDAO animalDAO = new AnimalDAO();
                CuidaDAO cuidaDAO = new CuidaDAO();

                // Guardar el animal en la base de datos
                animal = animalDAO.save(animal);

                // Obtener el ID del trabajador actual
                int idTrabajador = UserSession.getUser().getIdTrabajador();
                System.out.println("ID del trabajador autenticado: " + idTrabajador); // Depuración

                // Crear y guardar la relación en Cuida
                String tipoCuidado = tipoCuidadoField.getText();
                String observaciones = observacionesField.getText();
                Cuida cuida = new Cuida(animal.getIdAnimal(), idTrabajador, observaciones, tipoCuidado);
                cuidaDAO.save(cuida);

                // Mostrar mensaje de éxito
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Animal y datos de cuidado guardados correctamente.");
                alert.showAndWait();

                // Recargar la lista de animales
                cargarAnimales();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al guardar el animal y los datos de cuidado.");
            }
        } catch (IllegalArgumentException e) {
            // Mostrar mensaje de error al usuario
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            // Manejar otros errores inesperados
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al guardar el animal y los datos de cuidado.");
            alert.showAndWait();
        }
    }

    private HBox crearFilaAnimal(Animal animal) {
        HBox fila = new HBox();
        fila.setSpacing(10); // Espaciado entre elementos
        fila.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");

        // Etiqueta con el nombre del animal
        Label nombreLabel = new Label(animal.getNombre());
        nombreLabel.getStyleClass().add("animal-label");

        // Etiqueta con el microchip del animal
        Label microchipLabel = new Label("Microchip: " + animal.getChip());
        microchipLabel.getStyleClass().add("animal-label");

        // Botón para ver detalles
        Button detallesButton = new Button("Ver detalles");
        detallesButton.setOnAction(event -> verDetalles(animal));

        // Botón para eliminar
        Button eliminarButton = new Button("Eliminar");
        eliminarButton.setOnAction(event -> eliminarAnimal(animal));

        // Añadir los elementos al HBox
        fila.getChildren().addAll(nombreLabel, microchipLabel, detallesButton, eliminarButton);

        return fila;
    }

    private void cargarAnimales() {
        listaAnimales.getChildren().clear(); // Limpiar el GridPane antes de llenarlo
        int idTrabajador = UserSession.getUser().getIdTrabajador(); // Obtener el ID del trabajador actual

        try {
            CuidaDAO cuidaDAO = new CuidaDAO();
            List<Cuida> cuidados = cuidaDAO.findAll(); // Obtener todos los registros de cuidados

            AnimalDAO animalDAO = new AnimalDAO();
            int row = 0; // Contador de filas

            for (Cuida cuidado : cuidados) {
                if (cuidado.getIdTrabajador() == idTrabajador) { // Filtrar por idTrabajador
                    Animal animal = animalDAO.findById(cuidado.getIdAnimal());
                    if (animal != null) {
                        HBox fila = crearFilaAnimal(animal); // Crear la fila del animal
                        fila.getStyleClass().add("fila-animal"); // Asignar clase CSS
                        listaAnimales.add(fila, 0, row); // Añadir la fila al GridPane
                        row++; // Incrementar la fila
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar los animales.");
            alert.showAndWait();
        }
    }

    private void verDetalles(Animal animal) {
        try {
            CuidaDAO cuidaDAO = new CuidaDAO();
            List<Cuida> cuidados = cuidaDAO.findAll(); // Obtener todos los registros de cuidados

            Cuida cuidado = null;
            for (Cuida c : cuidados) {
                if (c.getIdAnimal() == animal.getIdAnimal()) {
                    cuidado = c;
                    break; // Salir del bucle una vez encontrado
                }
            }

            String tipoCuidado = (cuidado != null) ? cuidado.getTipo() : "No especificado";
            String observaciones = (cuidado != null) ? cuidado.getObservaciones() : "No especificado";

            // Mostrar los detalles en un cuadro de diálogo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Detalles del Animal");
            alert.setHeaderText("Información de " + animal.getNombre());
            alert.setContentText(
                    "Nombre: " + animal.getNombre() + "\n" +
                            "Edad: " + animal.getEdad() + "\n" +
                            "Microchip: " + animal.getChip() + "\n" +
                            "Tipo: " + animal.getTipo() + "\n" +
                            "Estado: " + animal.getEstado() + "\n" +
                            "Fecha de Adopción: " + animal.getFechaAdopcion() + "\n" +
                            "Tipo de Cuidado: " + tipoCuidado + "\n" +
                            "Observaciones: " + observaciones
            );
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al obtener los detalles del animal.");
            alert.showAndWait();
        }
    }

    private void editarAnimal(Animal animal) {
        // Aquí puedes abrir un formulario para editar el animal
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Función para editar " + animal.getNombre() + " aún no implementada.");
        alert.showAndWait();
    }

    private void eliminarAnimal(Animal animal) {
        try {
            AnimalDAO animalDAO = new AnimalDAO();
            animalDAO.delete(animal); // Eliminar el animal de la base de datos
            cargarAnimales(); // Recargar el GridPane
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Animal eliminado correctamente.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al eliminar el animal.");
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