package github.danicastroo.view;

import github.danicastroo.App;
import github.danicastroo.model.dao.AnimalDAO;
import github.danicastroo.model.dao.CuidaDAO;
import github.danicastroo.model.entity.*;
import github.danicastroo.model.singleton.UserSession;
import github.danicastroo.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    /**
     * Inicializa el controlador, carga los animales y configura los ComboBox y el botón de guardar.
     *
     * @param url            la ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si no se conoce
     * @param resourceBundle el recurso de internacionalización, o null si no se utiliza
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarAnimales();
        tipoComboBox.getItems().setAll(TipoAnimal.values());
        estadoComboBox.getItems().setAll(EstadoAnimal.values());
        guardarAnimalButton.setOnAction(event -> guardarAnimal());
    }

    /**
     * Guarda un nuevo animal y su relación de cuidado en la base de datos.
     * Valida los campos y muestra alertas en caso de error.
     */
    private void guardarAnimal() {
        try {
            // Validar que los campos no estén vacíos
            if (nombreField.getText().isEmpty() || edadField.getText().isEmpty() || microchipField.getText().isEmpty() ||
                    tipoCuidadoField.getText().isEmpty() || observacionesField.getText().isEmpty()) {
                throw new IllegalArgumentException("Todos los campos obligatorios deben estar llenos.");
            }

            String edadTexto = edadField.getText();
            if (!edadTexto.matches("\\d{1,3}")) {
                throw new IllegalArgumentException("La edad debe ser un número entero de máximo 3 dígitos.");
            }
            int edad = Integer.parseInt(edadTexto);


            String microchip = microchipField.getText();

            // Validar que se haya seleccionado un tipo y estado
            TipoAnimal tipo = tipoComboBox.getValue();
            EstadoAnimal estado = estadoComboBox.getValue();
            if (tipo == null || estado == null) {
                throw new IllegalArgumentException("Debe seleccionar un tipo y un estado para el animal.");
            }

            // Validar la fecha de adopción
            LocalDate fechaAdopcion = fechaAdopcionPicker.getValue();
            if (estado == EstadoAnimal.ADOPTADO && fechaAdopcion == null) {
                throw new IllegalArgumentException("Debes colocar una fecha de adopción porque está adoptado.");
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
                Utils.Alert("Aviso", "Animal y datos de cuidado guardados correctamente.", "Los datos se han guardado correctamente.", Alert.AlertType.INFORMATION, (Stage) listaAnimales.getScene().getWindow());


                cargarAnimales();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al guardar el animal y los datos de cuidado.");
            }
        } catch (IllegalArgumentException e) {
            Utils.Alert("Aviso", "Ha ocurrido un problema.", e.getMessage(), Alert.AlertType.WARNING, (Stage) listaAnimales.getScene().getWindow());

        } catch (Exception e) {
            // Manejar otros errores inesperados
            e.printStackTrace();
            Utils.Alert("Error", "Error al guardar el animal y los datos de cuidado.", e.getMessage(), Alert.AlertType.ERROR, (Stage) listaAnimales.getScene().getWindow());
        }
    }

    /**
     * Crea una fila visual (HBox) para mostrar un animal en la lista.
     *
     * @param animal el animal a mostrar
     * @return un HBox con los controles y datos del animal
     */
    private HBox crearFilaAnimal(Animal animal) {
        HBox fila = new HBox();
        fila.setSpacing(10); // Espaciado entre elementos
        fila.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");

        // Botón para ver detalles
        Button detallesButton = new Button("Ver detalles");
        detallesButton.setOnAction(event -> verDetalles(animal));

        // Botón para editar
        Button editarButton = new Button("Editar");
        editarButton.setOnAction(event -> editarAnimal(animal));

        // Botón para eliminar
        Button eliminarButton = new Button("Eliminar");
        eliminarButton.setOnAction(event -> eliminarAnimal(animal));

        // Etiqueta con el nombre del animal
        Label nombreLabel = new Label(animal.getNombre());
        nombreLabel.getStyleClass().add("animal-label");

        // Etiqueta con el microchip del animal
        Label microchipLabel = new Label("Microchip: " + animal.getChip());
        microchipLabel.getStyleClass().add("animal-label");

        // Añadir los elementos al HBox en el orden deseado
        fila.getChildren().addAll(detallesButton, editarButton, eliminarButton, nombreLabel, microchipLabel);

        return fila;
    }

    /**
     * Carga y muestra los animales asociados al trabajador actual en el GridPane.
     * Muestra alertas en caso de error.
     */
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
            Utils.Alert("Error", "Error al cargar los animales", e.getMessage(), Alert.AlertType.ERROR, (Stage) listaAnimales.getScene().getWindow());
        }
    }

    /**
     * Muestra los detalles de un animal en un cuadro de diálogo.
     *
     * @param animal el animal del que se mostrarán los detalles
     */
    private void verDetalles(Animal animal) {
        try {
            CuidaDAO cuidaDAO = new CuidaDAO();
            List<Cuida> cuidados = cuidaDAO.findAll();

            Cuida cuidado = null;
            for (Cuida c : cuidados) {
                if (c.getIdAnimal() == animal.getIdAnimal()) {
                    cuidado = c;
                    break;
                }
            }

            String tipoCuidado = (cuidado != null) ? cuidado.getTipo() : "No especificado";
            String observaciones = (cuidado != null) ? cuidado.getObservaciones() : "No especificado";

            // Usar Utils.Alert para mostrar los detalles
            Utils.Alert(
                    "Detalles del Animal",
                    "Información de " + animal.getNombre(),
                    "Nombre: " + animal.getNombre() + "\n" +
                            "Edad: " + animal.getEdad() + "\n" +
                            "Microchip: " + animal.getChip() + "\n" +
                            "Tipo: " + animal.getTipo() + "\n" +
                            "Estado: " + animal.getEstado() + "\n" +
                            "Fecha de Adopción: " + animal.getFechaAdopcion() + "\n" +
                            "Tipo de Cuidado: " + tipoCuidado + "\n" +
                            "Observaciones: " + observaciones,
                    Alert.AlertType.INFORMATION,
                    (Stage) listaAnimales.getScene().getWindow()
            );
        } catch (Exception e) {
            e.printStackTrace();
            Utils.Alert("Error", "Error al obtener los detalles del animal.", e.getMessage(), Alert.AlertType.ERROR, null);
        }
    }

    /**
     * Carga los datos de un animal en los campos para su edición y prepara el botón para actualizar.
     *
     * @param animal el animal a editar
     */
    private void editarAnimal(Animal animal) {
        try {
            // Cargar los datos del animal en los campos
            nombreField.setText(animal.getNombre());
            edadField.setText(String.valueOf(animal.getEdad()));
            microchipField.setText(animal.getChip());
            tipoComboBox.setValue(animal.getTipo());
            estadoComboBox.setValue(animal.getEstado());
            fechaAdopcionPicker.setValue(animal.getFechaAdopcion());

            // Obtener los datos de Cuida relacionados con el animal
            CuidaDAO cuidaDAO = new CuidaDAO();
            Cuida cuidado = cuidaDAO.findByAnimalId(animal.getIdAnimal());
            if (cuidado != null) {
                tipoCuidadoField.setText(cuidado.getTipo());
                observacionesField.setText(cuidado.getObservaciones());
            } else {
                tipoCuidadoField.clear();
                observacionesField.clear();
            }

            // Cambiar la acción del botón "Guardar" para actualizar
            guardarAnimalButton.setText("Actualizar");
            guardarAnimalButton.setOnAction(event -> actualizarAnimal(animal));
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar los datos del animal para editar.");
            alert.showAndWait();
        }
    }

    /**
     * Actualiza los datos de un animal y su relación de cuidado en la base de datos.
     * Valida los campos y muestra alertas en caso de error.
     *
     * @param animal el animal a actualizar
     */
    private void actualizarAnimal(Animal animal) {
        try {
            // Validar los campos como en el método guardarAnimal
            if (nombreField.getText().isEmpty() || edadField.getText().isEmpty() || microchipField.getText().isEmpty() ||
                    tipoCuidadoField.getText().isEmpty() || observacionesField.getText().isEmpty()) {
                throw new IllegalArgumentException("Todos los campos obligatorios deben estar llenos.");
            }

            String edadTexto = edadField.getText();
            if (!edadTexto.matches("\\d{1,3}")) {
                throw new IllegalArgumentException("La edad debe ser un número entero de máximo 3 dígitos.");
            }
            int edad = Integer.parseInt(edadTexto);

            String microchip = microchipField.getText();
            TipoAnimal tipo = tipoComboBox.getValue();
            EstadoAnimal estado = estadoComboBox.getValue();
            LocalDate fechaAdopcion = fechaAdopcionPicker.getValue();

            if (estado == EstadoAnimal.ADOPTADO && fechaAdopcion == null) {
                throw new IllegalArgumentException("Debes colocar una fecha de adopción porque está adoptado.");
            }

            // Actualizar los datos del animal
            animal.actualizarDatos(nombreField.getText(), microchip, edad, tipo, fechaAdopcion);
            animal.setEstado(estado);

            // Guardar los cambios en la base de datos
            AnimalDAO animalDAO = new AnimalDAO();
            animalDAO.update(animal);

            // Actualizar los datos de Cuida
            CuidaDAO cuidaDAO = new CuidaDAO();
            Cuida cuidado = cuidaDAO.findByAnimalId(animal.getIdAnimal());
            if (cuidado != null) {
                cuidado.setTipo(tipoCuidadoField.getText());
                cuidado.setObservaciones(observacionesField.getText());
                cuidaDAO.update(cuidado); // Actualizar el registro existente
            } else {
                throw new IllegalArgumentException("No se encontraron datos de cuidado para este animal.");
            }

            // Restaurar el botón "Guardar"
            guardarAnimalButton.setText("Guardar Animal");
            guardarAnimalButton.setOnAction(event -> guardarAnimal());

            // Recargar la lista de animales
            cargarAnimales();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Animal y datos de cuidado actualizados correctamente.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al actualizar el animal y los datos de cuidado.");
            alert.showAndWait();
        }
    }

    /**
     * Elimina un animal de la base de datos y actualiza la lista visual.
     * Muestra alertas en caso de error.
     *
     * @param animal el animal a eliminar
     */
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
            //Aquí he utilziado el Alert de javafx porque el que tengo en utils es un show(), para que funcione como yo quiero tiene que ser un showAndWait()
        }
    }

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
}
