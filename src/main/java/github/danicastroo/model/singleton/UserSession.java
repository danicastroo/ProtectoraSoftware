package github.danicastroo.model.singleton;

import github.danicastroo.model.entity.Trabajador;

public class UserSession {
    private static Trabajador user = null;

    public static void login(Trabajador trabajador) {
        user = trabajador; // Asigna el trabajador autenticado
    }

    public static void logout() {
        user = null;
    }

    public static boolean isLogged() {
        return user != null;
    }

    public static Trabajador getUser() {
        return user;
    }
}