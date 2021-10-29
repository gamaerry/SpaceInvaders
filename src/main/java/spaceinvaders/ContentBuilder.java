package spaceinvaders;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.stream.Collectors;

public class ContentBuilder {
    static double tiempo=0;
    final static Pane raiz=new Pane();
    final static Sprite jugador=new Sprite(300-20,720-50,40,40,"jugador", Color.BLUE);
    static final EventHandler<KeyEvent> controles= event -> {
        switch(event.getCode()){
            case LEFT:
                jugador.moverIzquierda();
                break;
            case UP:
                jugador.moverArriba();
                break;
            case DOWN:
                jugador.moverAbajo();
                break;
            case RIGHT:
                jugador.moverDerecha();
                break;
            case SPACE:
                disparar(jugador);
                break;
        }
    };
    static Parent createContent() {
        AnimationTimer temporizador= new AnimationTimer() {
            @Override
            public void handle(long now) {
                actualizar();
            }
        };
        raiz.setPrefSize(600,720);
        raiz.getChildren().add(jugador);
        temporizador.start();
        siguienteNivel();
        return raiz;
    }
    static void siguienteNivel() {
        for (int i = 0; i < 5; i++) {
            Sprite enemigo= new Sprite(90+i*100,150, 30, 30 , "enemigo",Color.RED);
            raiz.getChildren().add(enemigo);
        }
    }
    static void actualizar() {
        tiempo+=0.010;
        sprites().forEach(s->{
            switch (s.TIPO){
                case "disparoenemigo":
                    s.moverAbajo();
                    if(s.getBoundsInParent().intersects(jugador.getBoundsInParent())){
                        jugador.muerto=true;
                        s.muerto=true;
                    }
                    break;
                case "disparojugador":
                    s.moverArriba();
                    sprites().stream().filter(sprite->sprite.TIPO.equals("enemigo")).forEach(enemigo->{
                        if(s.getBoundsInParent().intersects(enemigo.getBoundsInParent())){
                            enemigo.muerto=true;
                            s.muerto=true;
                        }
                    });
                    break;
                case "enemigo":
                   if(tiempo>2&&Math.random()<0.3) disparar(s);
                   break;


            }
        });
        raiz.getChildren().removeIf(sprite-> ((Sprite) sprite).muerto);
        if(tiempo>2){
            tiempo=0;
        }
    }
    static List<Sprite> sprites(){
        return raiz.getChildren()
                .stream()
                .map(n->(Sprite)n)
                .collect(Collectors.toList());

    }
    static void disparar(Sprite sujeto) {
        Sprite disparo=new Sprite(
                (int) sujeto.getTranslateX()+20,
                (int) sujeto.getTranslateY(),
                5,
                20,
                "disparo"+sujeto.TIPO,
                Color.BLACK);
        raiz.getChildren().add(disparo);
    }
}
