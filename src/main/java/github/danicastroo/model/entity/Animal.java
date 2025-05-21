package github.danicastroo.model.entity;

import java.time.LocalDate;
import java.util.Objects;


public class Animal {
    private int idAnimal;
    private String nombre;
    private String chip;
    private int edad;
    private TipoAnimal tipo;
    private LocalDate fechaAdopcion;
    private EstadoAnimal estado;

    /**
     * Constructor por defecto.
     */
    public Animal() {}

    /**
     * Constructor con parámetros para inicializar un animal.
     *
     * @param idAnimal       ID único del animal.
     * @param nombre         Nombre del animal.
     * @param chip           Código del chip identificador.
     * @param edad           Edad del animal.
     * @param tipo           Tipo de animal (perro, gato, etc.).
     * @param fechaAdopcion  Fecha en que fue adoptado. Puede ser {@code null} si aún no ha sido adoptado.
     * @param estado         Estado actual del animal (disponible, adoptado, etc.).
     */
    public Animal(int idAnimal, String nombre, String chip, int edad, TipoAnimal tipo, LocalDate fechaAdopcion, EstadoAnimal estado) {
        this.idAnimal = idAnimal;
        this.nombre = nombre;
        this.chip = chip;
        this.edad = edad;
        this.tipo = tipo;
        this.fechaAdopcion = fechaAdopcion;
        this.estado = estado;
    }

    // Getters y setters

    /**
     * Obtiene el chip del animal.
     *
     * @return Chip del animal.
     */
    public String getChip() {
        return chip;
    }

    /**
     * Establece el chip del animal.
     *
     * @param chip Código del chip identificador.
     */
    public void setChip(String chip) {
        this.chip = chip;
    }

    /**
     * Obtiene el ID del animal.
     *
     * @return ID del animal.
     */
    public int getIdAnimal() {
        return idAnimal;
    }

    /**
     * Establece el ID del animal.
     *
     * @param idAnimal ID del animal.
     */
    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    /**
     * Obtiene el nombre del animal.
     *
     * @return Nombre del animal.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del animal.
     *
     * @param nombre Nombre del animal.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la edad del animal.
     *
     * @return Edad del animal.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad del animal.
     *
     * @param edad Edad del animal.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Obtiene la fecha de adopción del animal.
     *
     * @return Fecha de adopción o {@code null} si no ha sido adoptado.
     */
    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    /**
     * Establece la fecha de adopción del animal.
     *
     * @param fechaAdopcion Fecha de adopción.
     */
    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    /**
     * Obtiene el estado actual del animal.
     *
     * @return Estado del animal.
     */
    public EstadoAnimal getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual del animal.
     *
     * @param estado Estado del animal.
     */
    public void setEstado(EstadoAnimal estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el tipo de animal.
     *
     * @return Tipo del animal.
     */
    public TipoAnimal getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de animal.
     *
     * @param tipo Tipo del animal.
     */
    public void setTipo(TipoAnimal tipo) {
        this.tipo = tipo;
    }

    /**
     * Actualiza varios datos del animal.
     *
     * @param nombre         Nuevo nombre.
     * @param chip           Nuevo chip.
     * @param edad           Nueva edad.
     * @param tipo           Nuevo tipo.
     * @param fechaAdopcion  Nueva fecha de adopción.
     */
    public void actualizarDatos(String nombre, String chip, int edad, TipoAnimal tipo, LocalDate fechaAdopcion) {
        this.nombre = nombre;
        this.chip = chip;
        this.edad = edad;
        this.tipo = tipo;
        this.fechaAdopcion = fechaAdopcion;
    }

    /**
     * Indica si el animal está disponible para ser adoptado.
     *
     * @return {@code true} si no tiene fecha de adopción, {@code false} en caso contrario.
     */
    public boolean estaDisponibleParaAdopcion() {
        return fechaAdopcion == null;
    }

    /**
     * Asigna una fecha de adopción al animal.
     *
     * @param fechaAdopcion Fecha en la que el animal fue adoptado.
     */
    public void asignarFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    /**
     * Reinicia el estado del animal eliminando su fecha de adopción.
     */
    public void reiniciarEstado() {
        this.fechaAdopcion = null;
    }

    /**
     * Cambia el estado del animal.
     *
     * @param nuevoEstado Nuevo estado a asignar.
     */
    public void cambiarEstado(EstadoAnimal nuevoEstado) {
        this.estado = nuevoEstado;
    }

    /**
     * Compara este objeto con otro para determinar si son iguales según su ID.
     *
     * @param o Objeto a comparar.
     * @return {@code true} si el objeto es de tipo Animal y tiene el mismo ID.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return idAnimal == animal.idAnimal;
    }

    /**
     * Calcula el código hash en base al ID del animal.
     *
     * @return Código hash del animal.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(idAnimal);
    }

    /**
     * Devuelve una representación en cadena del animal.
     *
     * @return Cadena con tipo, nombre y chip (si tiene).
     */
    @Override
    public String toString() {
        return tipo + " - " + nombre + " (Chip: " + (chip != null && !chip.isEmpty() ? chip : "Sin chip") + ")";
    }
}
