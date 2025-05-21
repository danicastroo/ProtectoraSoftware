package github.danicastroo.model.entity;


public class Cuida {
    private int idCuida; // Clave primaria
    private int idAnimal; // FK hacia Animal
    private int idTrabajador; // FK hacia Trabajador
    private String observaciones;
    private String tipo;

    /**
     * Constructor por defecto.
     */
    public Cuida() {}

    /**
     * Constructor que inicializa los datos del cuidado.
     *
     * @param idAnimal        ID del animal cuidado.
     * @param idTrabajador    ID del trabajador que cuida al animal.
     * @param observaciones   Observaciones del cuidado.
     * @param tipo            Tipo de cuidado realizado.
     */
    public Cuida(int idAnimal, int idTrabajador, String observaciones, String tipo) {
        this.idAnimal = idAnimal;
        this.idTrabajador = idTrabajador;
        this.observaciones = observaciones;
        this.tipo = tipo;
    }

    /**
     * Obtiene el ID de la relación de cuidado.
     *
     * @return ID del cuidado.
     */
    public int getIdCuida() {
        return idCuida;
    }

    /**
     * Establece el ID de la relación de cuidado.
     *
     * @param idCuida ID del cuidado.
     */
    public void setIdCuida(int idCuida) {
        this.idCuida = idCuida;
    }

    /**
     * Obtiene el ID del animal cuidado.
     *
     * @return ID del animal.
     */
    public int getIdAnimal() {
        return idAnimal;
    }

    /**
     * Establece el ID del animal cuidado.
     *
     * @param idAnimal ID del animal.
     */
    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    /**
     * Obtiene el ID del trabajador que cuida al animal.
     *
     * @return ID del trabajador.
     */
    public int getIdTrabajador() {
        return idTrabajador;
    }

    /**
     * Establece el ID del trabajador que cuida al animal.
     *
     * @param idTrabajador ID del trabajador.
     */
    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    /**
     * Obtiene las observaciones del cuidado.
     *
     * @return Observaciones.
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Establece las observaciones del cuidado.
     *
     * @param observaciones Texto de observaciones.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Obtiene el tipo de cuidado realizado.
     *
     * @return Tipo de cuidado.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de cuidado realizado.
     *
     * @param tipo Tipo de cuidado.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve una representación en cadena del objeto Cuida.
     *
     * @return Cadena con los valores de los atributos.
     */
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
