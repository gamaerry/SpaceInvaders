package spaceinvaders;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Clase que crea y hace funcionar todo el conenido
 */
public class ContentBuilder {
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
    static int width = 560;
    static int height = 680;
    static final int LADO_JUGADOR = 55;
    static final int LADO_ENEMIGO = 40;
    /**
     * Variables enteras para las velocidades
     */
    static int VEL_JUGADOR = 80,
            VEL_ENEMIGO = 350,
            VEL_PROYECTIL_ENEMIGO = 70,
            VEL_PROYECTIL_JUGADOR = 90;
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
     * Raíz de tipo Pane en donde se coloca todo
     * que será regresado al método main
     */
    final static BorderPane RAIZ = new BorderPane();

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
            LADO_JUGADOR, width / 2 - 20, height - 50, "jugador", COLOR_JUGADOR);

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
        Sprite paredes = new Sprite(width, height,20,20,"paredes",'w', Color.BLACK);
        AnimationTimer temporizador = new AnimationTimer() {
            @Override
            public void handle(long now) {
                actualizar();
            }
        };
        RAIZ.setPrefSize(width+40, height+40);
        RAIZ.widthProperty().addListener((observable, oldValue, newValue) -> {
            width =newValue.intValue();
            paredes.setWidth(newValue.intValue()-40);

        });
        RAIZ.heightProperty().addListener((observable, oldValue, newValue) -> {
            height =newValue.intValue();
            //paredes.setHeight(newValue.intValue()-40);
            System.out.println("Cambio de "+oldValue+" a "+newValue);
        });
        NODOS.add(paredes);
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
        if (left) JUGADOR.moverIzquierda(VEL_JUGADOR);
        if (up) JUGADOR.moverArriba(VEL_JUGADOR);
        if (down) JUGADOR.moverAbajo(VEL_JUGADOR);
        if (right) JUGADOR.moverDerecha(VEL_JUGADOR);
        NODOS_U.stream().map(n -> (Sprite) n).forEach(nodo -> {
            if (nodo instanceof ShooterSprite) { //Si es un tirador entonces
                if (nodo != JUGADOR) {// Si es el enemigo
                    nodoTmp = (ShooterSprite) nodo;
                    random = Math.random();
                    if (nodoTmp.limites.intersects(JUGADOR.limites)) //Si intersecta con el jugador
                        JUGADOR.setVisible(false);//muere el jugador
                    if (pasos > 200 && random < 0.4) { //Si es tiempo de disparar y le toca por azar
                        nodoTmp.disparar(); // dispara
                        if (random < 0.1) { nodoTmp.moverIzquierda(VEL_ENEMIGO);
                        } else if (random < 0.2) { nodoTmp.moverArriba(VEL_ENEMIGO);
                        } else if (random < 0.3) { nodoTmp.moverAbajo(VEL_ENEMIGO);
                        } else { nodoTmp.moverDerecha(VEL_ENEMIGO);}
                    }
                }
            } else if(nodo.TIPO.matches("proyectil.*")){ //Si no es un tirador, entonces es un proyectil
                if (fueraDelLimite(nodo.getTranslateX(), nodo.getTranslateY())) {
                    nodo.setVisible(false);
                    System.out.println("tipo invisible: "+nodo.TIPO);
                }
                if (nodo.TIPO.equals("proyectilenemigo")) { //Si es el proyectil del enemigo
                    nodo.moverAbajo(VEL_PROYECTIL_ENEMIGO); //Entonces se mueve hacia abajo
                    if (nodo.getBoundsInParent().intersects(JUGADOR.limites)) { //Si toca al jugador
                        JUGADOR.setVisible(false);//muere el jugador
                        nodo.setVisible(false); // y el proyectil
                    }
                } else { //Si no es el proyectil enemigo, es el del jugador
                    JUGADOR.dirigir(nodo); // Entonces se mueve hacia donde se disparó
                    NODOS_U.forEach(nodoInterno -> { // Y para cada nodo...
                        if (nodoInterno != JUGADOR && nodoInterno instanceof ShooterSprite) { //Si el nodo es el enemigo entonces
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

    private static boolean fueraDelLimite(double x, double y) {
        return x < 20 || x > width-40 || y < 20 || y > height-40;
    }

    private static void pantallaFinal() {
    }
}
