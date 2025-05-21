package github.danicastroo.model.entity;

/**
 * Clase que representa a un adoptante, el cual extiende de la clase Persona.
 * Contiene información de contacto y datos relacionados con la adopción.
 */
public class Adoptante extends Persona {

    private int idPersona;

    private int idAdoptante;

    private String telefono;

    private String email;

    private String direccion;

    private int idAnimal;

    private String observaciones;

    /**
     * Constructor vacío necesario para frameworks o deserialización.
     */
    public Adoptante() {}

    /**
     * Constructor con parámetros para inicializar los campos principales.
     *
     * @param idAdoptante   ID del adoptante
     * @param nombre        Nombre del adoptante (heredado de Persona)
     * @param telefono      Teléfono de contacto
     * @param email         Correo electrónico
     * @param direccion     Dirección del adoptante
     * @param idAnimal      ID del animal adoptado
     * @param observaciones Observaciones adicionales
     */
    public Adoptante(int idAdoptante, String nombre, String telefono, String email, String direccion, int idAnimal, String observaciones) {
        super(nombre);
        this.idAdoptante = idAdoptante;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.idAnimal = idAnimal;
        this.observaciones = observaciones;
    }

    /**
     * Obtiene el ID de la persona.
     * @return ID de la persona
     */
    public int getIdPersona() {
        return idPersona;
    }

    /**
     * Establece el ID de la persona.
     * @param idPersona ID de la persona
     */
    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Obtiene la dirección del adoptante.
     * @return Dirección del adoptante
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del adoptante.
     * @param direccion Dirección del adoptante
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
     * Obtiene el teléfono del adoptante.
     * @return Teléfono de contacto
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del adoptante.
     * @param telefono Teléfono de contacto
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el email del adoptante.
     * @return Correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el email del adoptante.
     * @param email Correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el ID del animal adoptado.
     * @return ID del animal (clave foránea)
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
     * Obtiene las observaciones adicionales.
     * @return Observaciones
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Establece las observaciones adicionales.
     * @param observaciones Observaciones
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Método sobrescrito que muestra los datos del adoptante.
     */
    @Override
    public void mostrarDatos() {
        System.out.println("ID Adoptante: " + idAdoptante);
        System.out.println("Nombre: " + nombre);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Email: " + email);
        System.out.println("ID Animal (FK): " + idAnimal);
        System.out.println("Observaciones: " + observaciones);
    }
}
