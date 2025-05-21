package github.danicastroo.model.entity;

import java.time.LocalDate;

/**
 * Clase que representa la entidad Adopta, que almacena información sobre una adopción de un animal.
 */
public class Adopta {

    private int idAdopta;

    private int idAdoptante;

    private int idAnimal;

    private LocalDate fechaAdopcion;

    private String observaciones;

    /**
     * Constructor vacío necesario para el uso de frameworks o deserialización.
     */
    public Adopta() {
    }

    /**
     * Constructor con todos los atributos de la clase.
     *
     * @param idAdopta       ID de la adopción
     * @param idAdoptante    ID del adoptante
     * @param idAnimal       ID del animal adoptado
     * @param fechaAdopcion  Fecha de la adopción
     * @param observaciones  Observaciones adicionales
     */
    public Adopta(int idAdopta, int idAdoptante, int idAnimal, LocalDate fechaAdopcion, String observaciones) {
        this.idAdopta = idAdopta;
        this.idAdoptante = idAdoptante;
        this.idAnimal = idAnimal;
        this.fechaAdopcion = fechaAdopcion;
        this.observaciones = observaciones;
    }

    /**
     * Obtiene el ID de la adopción.
     * @return ID de la adopción
     */
    public int getIdAdopta() {
        return idAdopta;
    }

    /**
     * Establece el ID de la adopción.
     * @param idAdopta ID de la adopción
     */
    public void setIdAdopta(int idAdopta) {
        this.idAdopta = idAdopta;
    }

    /**
     * Obtiene el ID del adoptante.
     * @return ID del adoptante
     */
    public int getIdAdoptante() {
        return idAdoptante;
    }

    /**
     * Establece el ID del adoptante.
     * @param idAdoptante ID del adoptante
     */
    public void setIdAdoptante(int idAdoptante) {
        this.idAdoptante = idAdoptante;
    }

    /**
     * Obtiene el ID del animal adoptado.
     * @return ID del animal
     */
    public int getIdAnimal() {
        return idAnimal;
    }

    /**
     * Establece el ID del animal adoptado.
     * @param idAnimal ID del animal
     */
    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    /**
     * Obtiene la fecha de adopción.
     * @return Fecha de la adopción
     */
    public LocalDate getFechaAdopcion() {
        return fechaAdopcion;
    }

    /**
     * Establece la fecha de adopción.
     * @param fechaAdopcion Fecha de la adopción
     */
    public void setFechaAdopcion(LocalDate fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }

    /**
     * Obtiene las observaciones de la adopción.
     * @return Observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Establece las observaciones de la adopción.
     * @param observaciones Observaciones adicionales
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
