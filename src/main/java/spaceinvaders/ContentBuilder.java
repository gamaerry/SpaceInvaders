package spaceinvaders;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
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
            COLOR_ENEMIGO_A = Color.RED,
            COLOR_APUNTADOR = Color.BLACK,
            COLOR_PROYECTIL = Color.BLACK,
            COLOR_PAREDES = Color.BLACK;

    /**
     * Variables enteras para las dimensiones
     */
    static int widthInterno = 560,
            heightInterno = 680,
            borde = 20;

    /**
     * Variables para el jugador
     */
    static int ladoJugador = 55,
            velJugador = 80,
            velProyectilJugador = 90;

    /**
     * Constantes enteras para los enemigos
     */
    static final int LADO_ENEMIGO_A = 40,
            VEL_ENEMIGO_A = 350,
            VEL_PROYECTIL_ENEMIGO_A = 70;

    /**
     * Objeto auxiliar para los enemigos
     */
    static ShooterSprite nodoTmp;

    /**
     * Objeto auxiliar para los valores aleatorios
     */
    static double random;

    /**
     * booleanos que mantienen el estado de los botones pulsados
     */
    static boolean left = false, up = false, down = false, right = false;

    /**
     * Manejador del evento de soltar los botones
     */
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
            ladoJugador, widthInterno / 2 - 20, heightInterno - 50, "jugador", COLOR_JUGADOR);

    /**
     * Las paredes (o márgenes) son del tipo Sprite
     */
    final static Sprite PAREDES = new Sprite(
            widthInterno, heightInterno, borde, borde, "paredes", 'w', COLOR_PAREDES);

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
        establecerDimensiones();
        NODOS.addAll(PAREDES, JUGADOR);
        siguienteNivel();
        final AnimationTimer TEMPORIZADOR = new AnimationTimer() {
            @Override
            public void handle(long now) {
                actualizar();
            }
        };
        TEMPORIZADOR.start();
        return RAIZ;
    }

    /**
     * Método que establece las dimenciones de la raíz
     */
    private static void establecerDimensiones() {
        RAIZ.setPrefSize(widthInterno + 40, heightInterno + 40);
        RAIZ.widthProperty().addListener((observable, oldValue, newValue) -> {
            widthInterno = newValue.intValue() - 40;
            PAREDES.setWidth(widthInterno);
        });
        RAIZ.heightProperty().addListener((observable, oldValue, newValue) -> {
            heightInterno = newValue.intValue() - 40;
            PAREDES.setHeight(heightInterno);
        });
    }

    /**
     * Este método crea a los enemigos y los coloca cada 100 pixeles
     * empezando por el 90 (recordar que el width es de 600 pixeles)
     */
    static void siguienteNivel() {
        for (int i = 0; i < 5; i++) {
            ShooterSprite enemigo = new ShooterSprite(
                    LADO_ENEMIGO_A,
                    90 + i * 100,
                    150,
                    "enemigo",
                    COLOR_ENEMIGO_A);
            NODOS.add(enemigo);
        }
    }

    /**
     * Este método actualiza el juego con el tiempo
     */
    static void actualizar() {
        if (left) JUGADOR.moverIzquierda(velJugador);
        if (up) JUGADOR.moverArriba(velJugador);
        if (down) JUGADOR.moverAbajo(velJugador);
        if (right) JUGADOR.moverDerecha(velJugador);
        //Se limita dos veces al jugador (en seguida y en recorrerSprites())
        limitarShooter(JUGADOR.getTranslateX(), JUGADOR.getTranslateY(), velJugador, ladoJugador, JUGADOR);
        recorrerSprites();
        NODOS.removeIf(nodo -> !nodo.isVisible());
        if (pasos++ > 200) pasos = 0;
    }

    /**
     * Recorre y actua sobre cada sprite de la RAIZ con el tiempo
     */
    private static void recorrerSprites() {
        NODOS_U.stream().map(n -> (Sprite) n).forEach(nodo -> {
            if (nodo instanceof ShooterSprite) { //Si es un tirador entonces
                if (nodo != JUGADOR) {// Si es el enemigo
                    nodoTmp = (ShooterSprite) nodo;
                    random = Math.random();
                    limitarShooter(nodoTmp.getTranslateX(), nodoTmp.getTranslateY(), VEL_ENEMIGO_A, LADO_ENEMIGO_A, nodoTmp);
                    if (nodoTmp.limites.intersects(JUGADOR.limites)) //Si intersecta con el jugador
                        JUGADOR.setVisible(false);//muere el jugador
                    if (pasos > 200 && random < 0.4) { //Si es tiempo de disparar y le toca por azar
                        nodoTmp.disparar(); // dispara
                        if (random < 0.1) nodoTmp.moverIzquierda(VEL_ENEMIGO_A);
                        else if (random < 0.2) nodoTmp.moverArriba(VEL_ENEMIGO_A);
                        else if (random < 0.3) nodoTmp.moverAbajo(VEL_ENEMIGO_A);
                        else nodoTmp.moverDerecha(VEL_ENEMIGO_A);
                    }
                }else //Si es el jugador (su limitación debe de superar los eventos de los booleanos left, rigth, up y down):
                    limitarShooter(JUGADOR.getTranslateX(), JUGADOR.getTranslateY(), velJugador, ladoJugador, JUGADOR);
            } else if (nodo.TIPO.matches("proyectil.*")) { //Si no es un tirador, entonces es un proyectil
                if (proyectilFueraDelLimite(nodo.getTranslateX(), nodo.getTranslateY()))
                    nodo.setVisible(false);
                if (nodo.TIPO.equals("proyectilenemigo")) { //Si es el proyectil del enemigo
                    nodo.moverAbajo(VEL_PROYECTIL_ENEMIGO_A); //Entonces se mueve hacia abajo
                    if (nodo.getBoundsInParent().intersects(JUGADOR.limites)) { //Si toca al jugador
                        JUGADOR.setVisible(false);//muere el jugador
                        nodo.setVisible(false); // y el proyectil
                    }
                } else { //Si no es el proyectil enemigo, es el del jugador
                    JUGADOR.dirigir(nodo); // Entonces se mueve hacia donde se disparó
                    NODOS_U.forEach(nodoInterno -> { // Y para cada nodo...
                        if (nodoInterno != JUGADOR && nodoInterno instanceof ShooterSprite) //Si el nodo es el enemigo entonces
                            if (nodo.getBoundsInParent().intersects(((ShooterSprite) nodoInterno).limites)) { //si la bala y el enemigo intersectan
                                nodo.setVisible(false); // el proyectil muere
                                nodoInterno.setVisible(false); // el enemigo muere
                            }
                    });
                }
            }
        });
    }

    /**
     * Método que mantiene al ShooterSprite dentro del area de juego
     *
     * @param x posición horizontal del ShooterSprite
     * @param y posición vertical del ShoterSprite
     * @param velShooter velocidad del ShoterSprite
     * @param ladoShooter longitud del ShoterSprite
     * @param shooter ShoterSprite a limitar
     */
    private static void limitarShooter(double x, double y, int velShooter, int ladoShooter, ShooterSprite shooter) {
        if (x < borde || x > widthInterno + borde - ladoShooter) //si esta fuera de los límites horizontales
            if (x < borde)
                shooter.moverDerecha(velShooter);
            else
                shooter.moverIzquierda(velShooter);
        else if (y < borde || y > heightInterno + borde - ladoShooter) //si esta fuera de los límites verticales
            if (y < borde)
                shooter.moverAbajo(velShooter);
            else
                shooter.moverArriba(velShooter);
    }

    /**
     * Método auxiliar que ayuda a limitar los proyectiles dentro del area del juego
     *
     * @param x posición horizontal del proyectil
     * @param y posición vertical del proyectil
     * @return Regresa si está fuera del límite o no
     */
    private static boolean proyectilFueraDelLimite(double x, double y) {
        //Esto se puede simplificar porque borde==proyectil.length
        return x < borde || x > widthInterno || y < borde || y > heightInterno;
    }
}
