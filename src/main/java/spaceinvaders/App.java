package spaceinvaders;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage escenario){
        Scene escena=new Scene(ContentBuilder.mainContent());
        escena.setOnKeyPressed(ContentBuilder.controles);
        escenario.setScene(escena);
        escenario.show();
    }
    public static void main(String[] args) {launch();}
}