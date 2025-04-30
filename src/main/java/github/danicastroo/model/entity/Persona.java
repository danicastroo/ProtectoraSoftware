package github.danicastroo.model.entity;

public abstract class Persona {
    protected String nombre;

    // Constructor vacío
    public Persona() {}

    // Constructor
    public Persona(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método para mostrar información de la persona
    public abstract void mostrarDatos();
}
