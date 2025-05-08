package github.danicastroo.model.entity;

public class Cuida {
    private int idCuida; // Nuevo atributo para la clave primaria
    private int idAnimal; // FK
    private int idTrabajador; // FK
    private String observaciones;
    private String tipo;

    // Constructor vacío
    public Cuida() {}

    // Constructor
    public Cuida(int idAnimal, int idTrabajador, String observaciones, String tipo) {
        this.idAnimal = idAnimal;
        this.idTrabajador = idTrabajador;
        this.observaciones = observaciones;
        this.tipo = tipo;
    }

    // Getters y setters
    public int getIdCuida() { return idCuida; }
    public void setIdCuida(int idCuida) { this.idCuida = idCuida; }

    public int getIdAnimal() { return idAnimal; }
    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }

    public int getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(int idTrabajador) { this.idTrabajador = idTrabajador; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return "Cuida{" +
                "idCuida=" + idCuida +
                ", idAnimal=" + idAnimal +
                ", idTrabajador=" + idTrabajador +
                ", observaciones='" + observaciones + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}