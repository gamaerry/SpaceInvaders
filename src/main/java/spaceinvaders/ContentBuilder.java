package spaceinvaders;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Clase que crea y hace funcionar todo el conenido
 */
public class ContentBuilder {
    static ShooterSprite nodoTmp;
    static double random;
    static boolean left = false, up = false, down = false, right = false;
    static final EventHandler<? super KeyEvent> CONTROLES_SOLTAR = event -> {
        switch (event.getCode()) {
            case LEFT:
                left = false;
                break;
            case UP:
                up = false;
                break;
            case DOWN:
                down = false;
                break;
            case RIGHT:
                right = false;
                break;
        }
    };
    /**
     * Pasos que se darán mediante el método actualizar
     * del objeto de tipo AnimationTimer
     */
    static int pasos = 0;

    /**
     * Colores del juego
     */
    static final Color
            COLOR_JUGADOR = Color.BLUE,
            COLOR_ENEMIGO = Color.RED,
            COLOR_APUNTADOR = Color.BLACK,
            COLOR_PROYECTIL = Color.BLACK,
            COLOR_FONDO = Color.WHITE;

    /**
     * Simples constantes enteras para las dimensiones
     */
    static final int
            WIDTH = 600,
            HEIGHT = 720,
            LADO_JUGADOR = 55,
            LADO_ENEMIGO = 40;

    /**
     * Raíz de tipo Pane en donde se coloca todo
     * que será regresado al método main
     */
    final static Pane RAIZ = new Pane();

    /**
     * Lista de nodos, en realidad todos los nodos son Canvas
     * (un tipo particular de Node)
     */
    final static ObservableList<Node> NODOS = RAIZ.getChildren();

    /**
     * Lista inmodificable de nodos
     */
    final static ObservableList<Node> NODOS_U = RAIZ.getChildrenUnmodifiable();

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
            COLOR_JUGADOR);

    /**
     * El jugador no necesita ser actualizado con el tiempo
     * a diferencia de los proyectiles y los enemigos,
     * en cambio se define el siguiente manejador de eventos
     * para la escena en el método App.start().
     */
    final static EventHandler<KeyEvent> CONTROLES = event -> {
        switch (event.getCode()) {
            case LEFT:
                left = true;
                break;
            case UP:
                up = true;
                break;
            case DOWN:
                down = true;
                break;
            case RIGHT:
                right = true;
                break;
            case SPACE:
                JUGADOR.disparar();
                break;
            case W:
                JUGADOR.setDireccion('w');
                break;
            case S:
                JUGADOR.setDireccion('s');
                break;
            case A:
                JUGADOR.setDireccion('a');
                break;
            case D:
                JUGADOR.setDireccion('d');
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
                    COLOR_ENEMIGO);
            NODOS.add(enemigo);
        }
    }

    /**
     * Este método actualiza el juego con el tiempo
     */
    static void actualizar() {
        if (left) JUGADOR.moverIzquierda(100);
        if (up) JUGADOR.moverArriba(100);
        if (down) JUGADOR.moverAbajo(100);
        if (right) JUGADOR.moverDerecha(100);
        NODOS_U.stream().map(n -> (Sprite) n).forEach(nodo -> {
            if (nodo instanceof ShooterSprite) { //Si es un tirador entonces
                if (nodo != JUGADOR) {// Si es el enemigo
                    nodoTmp = (ShooterSprite) nodo;
                    random = Math.random();
                    if (nodoTmp.limites.intersects(JUGADOR.limites)) //Si intersecta con el jugador
                        JUGADOR.setVisible(false);//muere el jugador
                    if (pasos > 200 && random < 0.4) { //Si es tiempo de disparar y le toca por azar
                        nodoTmp.disparar(); // dispara
                        if (random < 0.1) {
                            nodoTmp.moverIzquierda(400);
                        } else if (random < 0.2) {
                            nodoTmp.moverArriba(400);
                        } else if (random < 0.3) {
                            nodoTmp.moverAbajo(400);
                        } else {
                            nodoTmp.moverDerecha(400);
                        }
                    }
                }
            } else { //Si no es un tirador, entonces es un proyectil
                if (nodo.TIPO.equals("proyectilenemigo")) { //Si es el proyectil del enemigo
                    nodo.moverAbajo(80); //Entonces se mueve hacia abajo
                    if (nodo.getBoundsInParent().intersects(JUGADOR.limites)) { //Si toca al jugador
                        JUGADOR.setVisible(false);//muere el jugador
                        nodo.setVisible(false); // y el proyectil
                    }
                } else { //Si no es el proyectil enemigo, es el del jugador
                    JUGADOR.dirigir(nodo); // Entonces se mueve hacia donde se disparó
                    NODOS_U.forEach(nodoInterno -> { // Y para cada nodo...
                        if (nodoInterno instanceof ShooterSprite && nodoInterno != JUGADOR) { //Si el nodo es el enemigo entonces
                            if (nodo.getBoundsInParent().intersects(((ShooterSprite) nodoInterno).limites)) { //si la bala y el enemigo intersectan
                                nodo.setVisible(false); // el proyectil muere
                                nodoInterno.setVisible(false); // el enemigo muere
                            }
                        }
                    });
                }
            }
        });
        NODOS.removeIf(nodo -> {
            if (!JUGADOR.isVisible()) pantallaFinal();
            return !nodo.isVisible();
        });
        if (pasos++ > 200) pasos = 0;
    }

    private static void pantallaFinal() {
    }
}
