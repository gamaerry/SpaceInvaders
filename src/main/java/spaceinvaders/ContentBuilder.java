package spaceinvaders;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ContentBuilder {

    private final static Pane raiz=new Pane();
    private final static Sprite jugador=new Sprite(300-20,720-50,40,40,"jugador", Color.BLUE);
    
    static final EventHandler<KeyEvent> controles= event -> {
        switch(event.getCode()){
            case A:
                jugador.moverIzquierda();
                break;
            case D:
                jugador.moverDerecha();
                break;
            case SPACE:
                disparar();
                break;
        }
    };

    private static void disparar() {
    }

    static Parent createContent() {
        raiz.setPrefSize(600,720);
        raiz.getChildren().add(jugador);
        return raiz;
    }
}
