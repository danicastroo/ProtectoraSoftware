package github.danicastroo.model.entity;

public class Trabajador extends Persona {
    private int idTrabajador;
    private String estado;
    private String email;
    private String password;

    // Constructor vacío
    public Trabajador() {}

    public Trabajador(String nombre, int idTrabajador, String estado, String email, String password) {
        super(nombre);
        this.idTrabajador = idTrabajador;
        this.estado = estado;
        this.email = email;
        this.password = password;
    }

    public Trabajador(String nombre, String gmail, String password) {
    }

    // Getters y setters específicos
    public int getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(int idTrabajador) { this.idTrabajador = idTrabajador; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    //Sobreescritura del método mostrarDatos
    @Override
    public void mostrarDatos() {
        System.out.println(">> ID Trabajador: " + idTrabajador);
        System.out.println(" Nombre: " + nombre);
        System.out.println(" Estado: " + estado);
        System.out.println(" Email: " + email);
    }
}
