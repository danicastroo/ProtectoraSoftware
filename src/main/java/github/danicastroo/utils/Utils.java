package github.danicastroo.utils;

import github.danicastroo.App;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Utils {

    /**
     * Encripta una cadena usando SHA-256.
     *
     * @param s Cadena de texto a encriptar.
     * @return Cadena hexadecimal resultante del hash SHA-256 o null si hay error.
     */
    public static String encryptSHA256(String s) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA256");

            md.update(s.getBytes());    // Se actualiza el MessageDigest con los bytes de la cadena de entrada

            StringBuilder sb = new StringBuilder();
            for (byte aByte : md.digest()) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Crea y muestra un diálogo de alerta personalizado.
     *
     * @param title  Título del diálogo.
     * @param header Texto del encabezado.
     * @param text   Contenido principal del mensaje.
     * @param type   Tipo de alerta (INFORMATION, WARNING, ERROR, etc.).
     * @param stage  Ventana propietaria del diálogo; si es null, se usa la ventana principal.
     * @return El objeto Alert creado y mostrado.
     */
    public static Alert Alert(String title, String header, String text, Alert.AlertType type, Stage stage) {
        Alert alertDialog = new Alert(type);
        alertDialog.setTitle(title);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(text);

        if (stage != null) {
            alertDialog.initOwner(stage);
        } else if (App.stage != null) {
            alertDialog.initOwner(App.stage);
        }

        alertDialog.show();

        if (App.stage != null) {
            App.stage.toFront();
        }

        return alertDialog;
    }

    /**
     * Muestra un diálogo de alerta informativo con un mensaje dado.
     *
     * @param message Mensaje a mostrar en la alerta.
     */
    public static void ShowAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * Solicita al usuario una cadena de texto por consola.
     *
     * @param sentence Mensaje que se mostrará para pedir la cadena.
     * @return Cadena ingresada por el usuario.
     */
    public static String pideString(String sentence) {
        String cadena = "";
        System.out.print(sentence);
        cadena = sc.next();
        return cadena;
    }

    /**
     * Solicita al usuario un número entero con validación de errores.
     *
     * @param msn Mensaje que se mostrará para pedir el número.
     * @return Número entero ingresado por el usuario.
     */
    public static int pideEntero(String msn) {
        int n = 0;
        boolean error = false;
        do {
            try {
                System.out.print(msn);
                n = sc.nextInt();
                error = false;
            } catch (InputMismatchException e) {
                System.out.println(">>> ⚠\uFE0F AVISO: El valor que has ingresado no es válido. Inténtalo de nuevo.\n");
                error = true;
                sc.next();
            }
        } while (error);
        return n;
    }

    public static class EmailValidator {
        /**
         * Valida si una cadena tiene formato de correo electrónico válido.
         *
         * @param email Cadena con el email a validar.
         * @return {@code true} si el email es válido, {@code false} en caso contrario.
         */
        public static boolean isValid(String email) {
            return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        }
    }

    /**
     * Scanner reutilizable para entrada por consola.
     */
    public static Scanner sc = new Scanner(System.in);
}
