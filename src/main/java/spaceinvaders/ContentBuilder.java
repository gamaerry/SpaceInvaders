package spaceinvaders;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class ContentBuilder {
    /**
     * Pasos que se darán mediante el método actualizar
     * del objeto de tipo AnimationTimer
     */
    static int pasos = 0;

    /**
     * Simples constantes enteras para las dimensiones
     */
    static final int WIDTH = 600, HEIGHT = 720, LADO_JUGADOR = 40, LADO_ENEMIGO = 40;

    /**
     * Raiz de tipo Pane en donde se coloca todo
     * que será regresado al método main
     */
    final static Pane RAIZ = new Pane();

    /**
     * En realidad todos los nodos son Rectangle
     * (un tipo particular de Shape)
     */
    final static ObservableList<Node> NODOS_U = RAIZ.getChildrenUnmodifiable();
    final static ObservableList<Node> NODOS = RAIZ.getChildren();

    /**
     * El jugador es del tipo ShooterSprite
     * un tipo particular de Sprite
     */
    final static ShooterSprite JUGADOR = new ShooterSprite(
            LADO_JUGADOR,
            LADO_JUGADOR,
            WIDTH / 2 - 20,
            HEIGHT - 50,
            "jugador",
            Color.BLACK);

    /**
     * El jugador no necesita ser actualizado con el tiempo
     * a diferencia de los proyectiles y los enemigos,
     * en cambio se define el siguiente manejador de eventos
     * para la escena en el método main.
     */
    final static EventHandler<KeyEvent> CONTROLES = event -> {
        switch (event.getCode()) {
            case LEFT:
                JUGADOR.moverIzquierda();
                break;
            case UP:
                JUGADOR.moverArriba();
                break;
            case DOWN:
                JUGADOR.moverAbajo();
                break;
            case RIGHT:
                JUGADOR.moverDerecha();
                break;
            case SPACE:
                JUGADOR.disparar();
                break;
            case W:
                JUGADOR.direccion = 'w';
                break;
            case S:
                JUGADOR.direccion = 's';
                break;
            case A:
                JUGADOR.direccion = 'a';
                break;
            case D:
                JUGADOR.direccion = 'd';
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
        RAIZ.setPrefSize(WIDTH, HEIGHT);
        NODOS.add(JUGADOR);
        siguienteNivel();
        temporizador.start();
        return RAIZ;
    }

    /**
     * Este método crea a los enemigos y los coloca cada 100 pixeles
     * empezando por el 90 (recordar que el width es de 600 pixeles)
     */
    static void siguienteNivel() {
        for (int i = 0; i < 5; i++) {
            ShooterSprite enemigo = new ShooterSprite(
                    LADO_ENEMIGO,
                    LADO_ENEMIGO,
                    90 + i * 100,
                    150,
                    "enemigo",
                    Color.RED);
            NODOS.add(enemigo);
        }
    }

    /**
     * Este método actualiza el juego con el tiempo
     */
    static void actualizar() {
        pasos++;
        NODOS_U.stream().map(n->(Sprite)n).forEach(nodo -> {
            if (nodo instanceof ShooterSprite) { //Si es un tirador entonces
                if (nodo != JUGADOR && pasos > 200 && Math.random() < 0.3) //Si es el enemigo y es tiempo de disparar y le toca por azar
                    ((ShooterSprite)nodo).disparar(); // dispara
            } else { //Si no es un tirador, entonces es un proyectil
                if (nodo.TIPO.equals("proyectilenemigo")) { //Si es el proyectil del enemigo
                    nodo.moverAbajo(); //Entonces se mueve hacia abajo
                    if (nodo.getBoundsInParent().intersects(JUGADOR.getBoundsInParent())) { //Si toca al jugador
                        JUGADOR.setVisible(false);//muere el jugador
                        nodo.setVisible(false); // y el proyectil
                    }
                } else { //Si no es el proyectil enemigo, es el del jugador
                    JUGADOR.dirigir(nodo); // Entonces se mueve hacia donde se disparó
                    NODOS_U.forEach(nodoInterno -> { // Y para cada nodo...
                        if (nodoInterno instanceof ShooterSprite && nodoInterno != JUGADOR) { //Si el nodo es el enemigo entonces
                            if (nodo.getBoundsInParent().intersects(nodoInterno.getBoundsInParent())) { //si la bala y el enemigo intersectan
                                nodo.setVisible(false); // el enemigo muere
                                nodoInterno.setVisible(false); // el proyectil muere
                            }
                        }
                    });
                }
            }
        });
        NODOS.removeIf(nodo -> !nodo.isVisible());
        if (pasos > 200) pasos = 0;
    }
}
