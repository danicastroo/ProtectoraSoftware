package github.danicastroo.model.entity;

public class Adoptante extends Persona {
    private int idPersona;
    private int idAdoptante;
    private String telefono;
    private String email;
    private String direccion; // Nuevo campo
    private int idAnimal; // FK
    private String observaciones;

    public Adoptante() {}

    public Adoptante(int idAdoptante, String nombre, String telefono, String email, String direccion, int idAnimal, String observaciones) {
        super(nombre);
        this.idAdoptante = idAdoptante;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.idAnimal = idAnimal;
        this.observaciones = observaciones;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public String getDireccion() { return direccion; }

    public void setDireccion(String direccion) { this.direccion = direccion; }

    public int getIdAdoptante() { return idAdoptante; }

    public void setIdAdoptante(int idAdoptante) { this.idAdoptante = idAdoptante; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public int getIdAnimal() { return idAnimal; }

    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }

    public String getObservaciones() { return observaciones; }

    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

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