package github.danicastroo.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Utils {
    /**
     * Función de Scanner.
     * Permite reutilizar el mismo objeto Scanner en todo el programa.
     */
    public static Scanner sc = new Scanner(System.in);

    /**
     * Método que pide una cadena por teclado.
     * @param sentence : pide un sout que quiere que aparezca por pantalla.
     * @return : devuelve una cadena.
     */
    public static String pideString (String sentence){
        String cadena = "";
        System.out.print(sentence);
        cadena = sc.next();
        return cadena;
    }

    /**
     * Pide un número entero, con control de errores.
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
}
