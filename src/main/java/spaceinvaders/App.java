package spaceinvaders;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal donde corre todo el programa
 */
public class App extends Application {
    /**
     * Método que llama al ContentBuilder para crear la escena
     * dentro del escenario y lo muestra
     *
     * @param escenario Objeto donde se establece la escena
     */
    @Override
    public void start(Stage escenario) {
        Scene escena = new Scene(ContentBuilder.mainContent());
        escena.setOnKeyPressed(ContentBuilder.CONTROLES);
        escenario.setScene(escena);
        escenario.show();
    }

    /**
     * Método main que es el primero que inicia Java
     *
     * @param args Argumentos que pueden o no usarse al inicio del programa
     */
    public static void main(String[] args) {
        launch();
    }
}