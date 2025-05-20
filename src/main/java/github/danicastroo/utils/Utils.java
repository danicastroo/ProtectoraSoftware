package github.danicastroo.utils;

import github.danicastroo.App;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Utils {

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
        return result;  // Se devuelve el resultado del hash en formato hexadecimal
    }

    public static Alert Alert(String title, String header, String text, Alert.AlertType type, Stage stage) {
        Alert alertDialog = new Alert(type);
        alertDialog.setTitle(title);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(text);

        // Asociar el cuadro de diálogo al Stage principal
        if (stage != null) {
            alertDialog.initOwner(stage);
        } else if (App.stage != null) {
            alertDialog.initOwner(App.stage);
        }



        // Mostrar el cuadro de diálogo sin bloquear
        alertDialog.show();

        // Asegurar que la ventana principal permanezca activa
        if (App.stage != null) {
            App.stage.toFront();
        }

        return alertDialog;
    }

    public static void ShowAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * Función de Scanner.
     * Permite reutilizar el mismo objeto Scanner en todo el programa.
     */
    public static Scanner sc = new Scanner(System.in);

    /**
     * Método que pide una cadena por teclado.
     *
     * @param sentence : pide un sout que quiere que aparezca por pantalla.
     * @return : devuelve una cadena.
     */
    public static String pideString(String sentence) {
        String cadena = "";
        System.out.print(sentence);
        cadena = sc.next();
        return cadena;
    }

    /**
     * Pide un número entero, con control de errores.
     *
     * @param msn : pide el mensaje que quiere que aparezca por pantalla.
     * @return : devuelve el número ingresado.
     */
    public static int pideEntero(String msn) {
        int n = 0;
        boolean error = false;
        do {
            try {
                System.out.print(msn);
                n = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println(">>> ⚠\uFE0F AVISO: El valor que has ingresado no es válido. Inténtalo de nuevo.\n");
                error = true;
                sc.next();
            }
        } while (error);
        return n;
    }

    public static class EmailValidator {
        public static boolean isValid(String email) {
            return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        }
    }
}
