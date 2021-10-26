package spaceinvaders;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Pane raiz=new Pane();
    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        raiz.setPrefSize(600,800);
        return raiz;
    }

    public static void main(String[] args) {
        launch();
    }
}