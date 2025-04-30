package github.danicastroo.view;

public enum Scenes {
    WELCOME("view/pantallaInicial.fxml"),
    ROOT("view/layout.fxml"),
    INICIO_SESION("view/iniciosesion.fxml");

    private String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
