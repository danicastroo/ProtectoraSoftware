package github.danicastroo.model.singleton;

import github.danicastroo.model.entity.Trabajador;

public class UserSession {
    /**
     * Trabajador actualmente autenticado en la sesión.
     */
    private static Trabajador user = null;

    /**
     * Inicia sesión asignando el trabajador autenticado.
     *
     * @param trabajador El trabajador autenticado.
     */
    public static void login(Trabajador trabajador) {
        user = trabajador;
    }

    /**
     * Cierra la sesión del usuario, eliminando al trabajador de la memoria.
     */
    public static void logout() {
        user = null;
    }

    /**
     * Verifica si hay un trabajador actualmente logueado.
     *
     * @return {@code true} si hay sesión iniciada, {@code false} si no.
     */
    public static boolean isLogged() {
        return user != null;
    }

    /**
     * Obtiene el trabajador que ha iniciado sesión.
     *
     * @return El trabajador logueado, o {@code null} si no hay ninguno.
     */
    public static Trabajador getUser() {
        return user;
    }
}
