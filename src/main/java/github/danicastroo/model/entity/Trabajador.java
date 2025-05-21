package github.danicastroo.model.entity;


public class Trabajador extends Persona {
    /**
     * Identificador único del trabajador.
     */
    private int idTrabajador;

    /**
     * Estado actual del trabajador (por ejemplo, activo o inactivo).
     */
    private String estado;

    /**
     * Correo electrónico del trabajador.
     */
    private String email;

    /**
     * Contraseña de acceso del trabajador.
     */
    private String password;

    /**
     * Constructor vacío.
     */
    public Trabajador() {}

    /**
     * Constructor que inicializa todos los atributos del trabajador.
     *
     * @param nombre Nombre del trabajador.
     * @param idTrabajador Identificador del trabajador.
     * @param estado Estado actual del trabajador.
     * @param email Correo electrónico del trabajador.
     * @param password Contraseña del trabajador.
     */
    public Trabajador(String nombre, int idTrabajador, String estado, String email, String password) {
        super(nombre);
        this.idTrabajador = idTrabajador;
        this.estado = estado;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor que inicializa solo nombre, email y contraseña.
     * Este constructor parece incompleto, ya que no asigna valores.
     *
     * @param nombre Nombre del trabajador.
     * @param gmail Correo electrónico (debería llamarse email).
     * @param password Contraseña del trabajador.
     */
    public Trabajador(String nombre, String gmail, String password) {
        // Este constructor está vacío y no inicializa atributos
    }

    /**
     * Obtiene el ID del trabajador.
     *
     * @return ID del trabajador.
     */
    public int getIdTrabajador() { return idTrabajador; }

    /**
     * Establece el ID del trabajador.
     *
     * @param idTrabajador ID a establecer.
     */
    public void setIdTrabajador(int idTrabajador) { this.idTrabajador = idTrabajador; }

    /**
     * Obtiene el estado del trabajador.
     *
     * @return Estado del trabajador.
     */
    public String getEstado() { return estado; }

    /**
     * Establece el estado del trabajador.
     *
     * @param estado Estado a establecer.
     */
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * Obtiene el email del trabajador.
     *
     * @return Email del trabajador.
     */
    public String getEmail() { return email; }

    /**
     * Establece el email del trabajador.
     *
     * @param email Email a establecer.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtiene la contraseña del trabajador.
     *
     * @return Contraseña del trabajador.
     */
    public String getPassword() { return password; }

    /**
     * Establece la contraseña del trabajador.
     *
     * @param password Contraseña a establecer.
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Muestra por consola los datos del trabajador.
     * Implementación del método abstracto definido en {@link Persona}.
     */
    @Override
    public void mostrarDatos() {
        System.out.println(">> ID Trabajador: " + idTrabajador);
        System.out.println(" Nombre: " + nombre);
        System.out.println(" Estado: " + estado);
        System.out.println(" Email: " + email);
    }
}
