package github.danicastroo.model.entity;

import java.time.LocalDate;

public class Adopta {
    private int idAdopta;
    private int idAdoptante;
    private int idAnimal;
    private LocalDate fechaAdopcion;
    private String observaciones;

    public Adopta() {}

    public Adopta(int idAdopta, int idAdoptante, int idAnimal, LocalDate fechaAdopcion, String observaciones) {
        this.idAdopta = idAdopta;
        this.idAdoptante = idAdoptante;
        this.idAnimal = idAnimal;
        this.fechaAdopcion = fechaAdopcion;
        this.observaciones = observaciones;
    }

    public int getIdAdopta() { return idAdopta; }
    public void setIdAdopta(int idAdopta) { this.idAdopta = idAdopta; }

    public int getIdAdoptante() { return idAdoptante; }
    public void setIdAdoptante(int idAdoptante) { this.idAdoptante = idAdoptante; }

    public int getIdAnimal() { return idAnimal; }
    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }

    public LocalDate getFechaAdopcion() { return fechaAdopcion; }
    public void setFechaAdopcion(LocalDate fechaAdopcion) { this.fechaAdopcion = fechaAdopcion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}