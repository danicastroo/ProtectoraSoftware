package github.danicastroo.view;

import javax.management.MBeanAttributeInfo;

public enum Scenes {
    WELCOME("view/pantallaInicial.fxml"),
    ROOT("view/layout.fxml"),
    INICIO_SESION("view/iniciosesion.fxml"),
    REGISTRO("view/registrar.fxml"),
    MAIN("view/main.fxml");
    private String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
