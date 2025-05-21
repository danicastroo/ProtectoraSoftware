package github.danicastroo.view;

import github.danicastroo.App;

import java.io.IOException;

public abstract class Controller {
    App app;

    /**
     * Establece la instancia principal de la aplicación.
     *
     * @param app Instancia de la clase App.
     */
    public void setApp(App app){
        this.app = app;
    }

    /**
     * Método que se ejecuta al abrir la vista/controlador.
     *
     * @param input Objeto de entrada para inicializar la vista (puede ser null).
     * @throws IOException Excepción en caso de errores de entrada/salida.
     */
    public abstract void onOpen(Object input) throws IOException;

    /**
     * Método que se ejecuta al cerrar la vista/controlador.
     *
     * @param output Objeto de salida para enviar datos al cerrar (puede ser null).
     */
    public abstract void onClose(Object output);
}
