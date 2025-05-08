package github.danicastroo.view;

import javax.management.MBeanAttributeInfo;

public enum Scenes {
    WELCOME("/github/danicastroo/view/pantallaInicial.fxml"),
    ROOT("/github/danicastroo/view/layout.fxml"),
    INICIO_SESION("/github/danicastroo/view/iniciosesion.fxml"),
    REGISTRO("/github/danicastroo/view/registrar.fxml"),
    MAIN("/github/danicastroo/view/main.fxml"),
    MODULO_INICIO("/github/danicastroo/view/ModuloInicio.fxml"),
    MODULO_ANIMALES("/github/danicastroo/view/ModuloAnimales.fxml");
    private String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
