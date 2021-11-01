package spaceinvaders;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class ContentBuilder {
    static double tiempo = 0;
    /**
     * Raiz de tipo Pane en donde se coloca todo
     * que será regresado al método main
     */
    final static Pane raiz = new Pane();
    /**
     * En realidad todos los nodos son Retangle
     * (un tipo particular de Shape)
     */
    final static ObservableList<Node> nodos = raiz.getChildren();
    /**
     * El jugador es del tipo ShooterSprite
     * un tipo particular de Sprite
     */
    final static ShooterSprite jugador = new ShooterSprite(
            300 - 20,
            720 - 50,
            40,
            40,
            "jugador",
            Color.BLUE);
    /**
     * El jugador no necesita ser actualizado con el tiempo
     * a diferencia de los disparos y los enemigos,
     * en cambio se define el siguiente manejador de eventos
     * para la escena en el método main.
     */
    final static EventHandler<KeyEvent> controles = event -> {
        switch (event.getCode()) {
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
                jugador.disparar();
                break;
            case W:
                jugador.direccion = 'w';
                break;
            case S:
                jugador.direccion = 's';
                break;
            case A:
                jugador.direccion = 'a';
                break;
            case D:
                jugador.direccion = 'd';
                break;
        }
    };

    /**
     * Este método contiene el flujo principal del juego,
     * llamando los métodos necesarios para su funcionamiento
     *
     * @return Regresa el Parent "Panel" en donde se coloca todo
     */
    static Parent mainContent() {
        AnimationTimer temporizador = new AnimationTimer() {
            @Override
            public void handle(long now) {
                actualizar();
            }
        };
        raiz.setPrefSize(600, 720);
        nodos.add(jugador);
        temporizador.start();
        siguienteNivel();
        return raiz;
    }

    /**
     * Este método crea a los enemigos y los coloca cada 100 pixeles
     * empezando por el 90 (recordar que el width es de 600 pixeles)
     */
    static void siguienteNivel() {
        for (int i = 0; i < 5; i++) {
            ShooterSprite enemigo = new ShooterSprite(90 + i * 100, 150, 30, 30, "enemigo", Color.RED);
            nodos.add(enemigo);
        }
    }

    /**
     * Este método actualiza el juego con el tiempo
     */
    static void actualizar() {
        tiempo += 0.016;
        sprites().forEach(s -> {
            switch (s.TIPO) {
                case "disparoenemigo":
                    s.moverAbajo();
                    if (s.getBoundsInParent().intersects(jugador.getBoundsInParent()))
                        jugador.muerto = s.muerto = true;
                    break;
                case "disparojugador":
                    switch (jugador.direccion) {
                        case 'w':
                            s.moverArriba();
                            break;
                        case 's':
                            s.moverAbajo();
                            break;
                        case 'd':
                            s.moverDerecha();
                            break;
                        case 'a':
                            s.moverIzquierda();
                            break;
                    }
                    sprites().stream().filter(sprite -> sprite.TIPO.equals("enemigo")).forEach(enemigo -> {
                        if (s.getBoundsInParent().intersects(enemigo.getBoundsInParent()))
                            enemigo.muerto = s.muerto = true;
                    });
                    break;
                case "enemigo":
                    if (tiempo > 2 && Math.random() < 0.3) ((ShooterSprite) s).disparar();
                    break;
            }
        });
        nodos.removeIf(sprite -> ((Sprite) sprite).muerto);
        if (tiempo > 2) tiempo = 0;
    }

    /**
     * Método de apoyo que regresa la lista de Sprites de la raiz
     *
     * @return Regresa el objeto List<Sprites> de la raiz
     */
    static List<Sprite> sprites() {
        return nodos //ObservableList<Node>
                .stream() //Stream<Node>
                .map(n -> (Sprite) n) //Stream<Sprite>
                .collect(Collectors.toList()); //List<Sprite>
    }
}
