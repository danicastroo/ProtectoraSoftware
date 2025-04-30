package github.danicastroo.model.entity;

public enum EstadoAnimal {
    EN_ADOPCION("En adopción"),
    ADOPTADO("Adoptado"),
    EN_TRATAMIENTO("En tratamiento"),
    EN_REHABILITACION("En rehabilitación");

    private final String estado;

    EstadoAnimal(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return estado;
    }

    /**
     * Convierte un texto en el enum correspondiente.
     * @param estadoTexto Texto del estado, por ejemplo "En adopción".
     * @return EstadoAnimal correspondiente o null si no coincide.
     */
    public static EstadoAnimal fromString(String estadoTexto) {
        for (EstadoAnimal estado : EstadoAnimal.values()) {
            if (estado.estado.equalsIgnoreCase(estadoTexto)) {
                return estado;
            }
        }
        return null;
    }
}