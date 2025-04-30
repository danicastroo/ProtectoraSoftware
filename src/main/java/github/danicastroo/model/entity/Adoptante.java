package github.danicastroo.model.entity;

public class Adoptante extends Persona {
    private int idAdoptante;
    private String telefono;
    private String email;
    private int idAnimal; // FK

    // Constructor vacío
    public Adoptante() {}

    // Constructor
    public Adoptante(int idAdoptante, String nombre, String telefono, String email, int idAnimal) {
        super(nombre); // Inicializa la clase base Persona
        this.idAdoptante = idAdoptante;
        this.telefono = telefono;
        this.email = email;
        this.idAnimal = idAnimal;
    }

    // Getters y setters específicos
    public int getIdAdoptante() { return idAdoptante; }

    public void setIdAdoptante(int idAdoptante) { this.idAdoptante = idAdoptante; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public int getIdAnimal() { return idAnimal; }

    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }

    //Sobreescritura del método mostrarDatos
    @Override
    public void mostrarDatos() {
        System.out.println("ID Adoptante: " + idAdoptante);
        System.out.println("Nombre: " + nombre);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Email: " + email);
        System.out.println("ID Animal (FK): " + idAnimal);
    }
}
