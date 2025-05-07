package github.danicastroo.model.singleton;

import github.danicastroo.model.entity.Trabajador;

public class UserSession {
    private static Trabajador user=null;
    public static void login(String email, String password) {
        user = new Trabajador(null, 1, null, null, email, password);
    }
    public static void logout() {
        user = null;
    }
    public static boolean isLogged() {
        return user==null?false:true;
    }
    public static Trabajador getUser() {
        return user;
    }
}
