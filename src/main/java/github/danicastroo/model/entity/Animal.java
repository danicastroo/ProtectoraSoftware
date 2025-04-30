package github.danicastroo.model.entity;

import github.danicastroo.model.entity.EstadoAnimal;

import java.time.LocalDate;
import java.util.Objects;

public class Animal {
    private int idAnimal;
    private String nombre;
    private int edad;
    private String tipo;
    private LocalDate fechaAdopcion;
    private EstadoAnimal estado;

    // Constructor vacío
    public Animal() {}

    // Constructor
    public Animal(int idAnimal, String nombre, int edad, String tipo, LocalDate fechaAdopcion) {
        this.idAnimal = idAnimal;
        this.nombre = nombre;
        this.edad = edad;
        this.tipo = tipo;
        this.fechaAdopcion = fechaAdopcion;
    }

    //Getters y setters
    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    public EstadoAnimal getEstado() {
        return estado;
    }

    public void setEstado(EstadoAnimal estado) {
        this.estado = estado;
    }

    /**
     * Método para actualizar los datos del animal
     */
    public void actualizarDatos(String nombre, int edad, String tipo, LocalDate fechaAdopcion) {
        this.nombre = nombre;
        this.edad = edad;
        this.tipo = tipo;
        this.fechaAdopcion = fechaAdopcion;
    }

    /**
     * Método para verificar si el animal está disponible para adopción
     */
    public boolean estaDisponibleParaAdopcion() {
        return fechaAdopcion == null;
    }

    /**
     * Método para formatear información del animal para mostrar en la interfaz
     */
    public String formatearParaInterfaz() {
        return String.format("ID: %d | Nombre: %s | Edad: %d | Tipo: %s | Estado: %s",
                idAnimal, nombre, edad, tipo, (fechaAdopcion == null ? "En adopción" : "Adoptado"));
    }

    /**
     * Método para asignar una fecha de adopción
     */
    public void asignarFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    /**
     * Método para reiniciar el estado del animal (por ejemplo, si regresa a la protectora)
     */
    public void reiniciarEstado() {
        this.fechaAdopcion = null;
    }

    /**
     * Método para cambiar el estado del animal.
     *
     * @param nuevoEstado El nuevo estado del animal.
     */
    public void cambiarEstado(EstadoAnimal nuevoEstado) {
        this.estado = nuevoEstado;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return idAnimal == animal.idAnimal;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idAnimal);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "idAnimal=" + idAnimal +
                ", nombre='" + nombre + '\'' +
                ", edad=" + edad +
                ", tipo='" + tipo + '\'' +
                ", fechaAdopcion='" + fechaAdopcion + '\'' +
                '}';
    }
}
