package github.danicastroo.model.entity;


public abstract class Persona {
    /**
     * Nombre de la persona.
     */
    protected String nombre;

    /**
     * Constructor por defecto.
     */
    public Persona() {}

    /**
     * Constructor que inicializa el nombre de la persona.
     *
     * @param nombre Nombre de la persona.
     */
    public Persona(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre de la persona.
     *
     * @return Nombre de la persona.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la persona.
     *
     * @param nombre Nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método abstracto para mostrar los datos de la persona.
     * Debe ser implementado por las clases hijas.
     */
    public abstract void mostrarDatos();
}
