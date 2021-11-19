package spaceinvaders;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Clase que crea y hace funcionar el nivel especificado
 */
public class LevelBuilder {
    /**
     * Colores del juego
     */
    static final Color
            COLOR_JUGADOR = Color.BLUE,
            COLOR_ENEMIGO_A = Color.RED,
            COLOR_APUNTADOR = Color.BLACK,
            COLOR_PROYECTIL = Color.BLACK,
            COLOR_CAMPO = Color.SLATEGRAY;

    /**
     * Variables enteras para las dimensiones
     */
    static int widthInterno = 560,
            heightInterno = 680,
            borde = 20;

    /**
     * Variables para el jugador
     */
    static final int LADO_JUGADOR = 55;
    static int velJugador = 80,
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
     * Lista de enemigos
     */
    static ArrayList<ShooterSprite> enemigos = new ArrayList<>();

    /**
     * El jugador es del tipo ShooterSprite
     * un tipo particular de Sprite
     */
    static ShooterSprite jugador = new ShooterSprite(
            LADO_JUGADOR,
            widthInterno / 2 - 20,
            heightInterno - 50,
            "jugador",
            'w',
            COLOR_JUGADOR,
            velJugador,
            velProyectilJugador);

    /**
     * El campo de fuego (o area de movimiento) es del tipo Sprite
     */
    final static Sprite CAMPO_DE_FUEGO = new Sprite(
            widthInterno, heightInterno, borde, borde, "campo", 'w', COLOR_CAMPO);

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
                jugador.disparar();
                break;
            case W:
                jugador.setDireccion('w');
                break;
            case S:
                jugador.setDireccion('s');
                break;
            case A:
                jugador.setDireccion('a');
                break;
            case D:
                jugador.setDireccion('d');
                break;
        }
    };

    /**
     * Este método contiene el flujo principal del juego,
     * llamando los métodos necesarios para su funcionamiento
     *
     * @return Regresa el Parent "Panel" en donde se coloca todo
     */
    static Parent levelContent(int nivel) {
        RAIZ.setStyle("-fx-background-color: darkslategray");
        establecerDimensiones();
        NODOS.addAll(CAMPO_DE_FUEGO, jugador);
        establecerEnemigos(nivel);
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
        RAIZ.setPrefSize(widthInterno + 2 * borde, heightInterno + 2 * borde);
        RAIZ.widthProperty().addListener((observable, oldValue, newValue) -> {
            widthInterno = newValue.intValue() - 2 * borde;
            CAMPO_DE_FUEGO.setWidth(widthInterno);
        });
        RAIZ.heightProperty().addListener((observable, oldValue, newValue) -> {
            heightInterno = newValue.intValue() - 2 * borde;
            CAMPO_DE_FUEGO.setHeight(heightInterno);
        });
    }

    /**
     * Este método crea a los enemigos y los coloca cada 100 pixeles
     * empezando por el 90 (recordar que el width es de 600 pixeles)
     */
    static void establecerEnemigos(int nivel) {
        switch (nivel) {
            case 0:
                for (int i = 0; i < 20; i++) {
                    ShooterSprite enemigo = new ShooterSprite(
                            LADO_ENEMIGO_A,
                            90 + i % 5 * 100,
                            i < 5 ? 100 : (i < 10 ? 150 : (i < 15 ? 200 : 250)),
                            "enemigoA",
                            's',
                            COLOR_ENEMIGO_A,
                            VEL_ENEMIGO_A,
                            VEL_PROYECTIL_ENEMIGO_A);
                    NODOS.add(enemigo);
                    enemigos.add(enemigo);
                }
                break;
        }
    }

    /**
     * Este método actualiza el juego con el tiempo
     */
    static void actualizar() {
        if (left) jugador.moverIzquierda();
        if (up) jugador.moverArriba();
        if (down) jugador.moverAbajo();
        if (right) jugador.moverDerecha();
        //Se limita dos veces al jugador (en seguida y en recorrerSprites())
        limitarShooter(jugador.getTranslateX(), jugador.getTranslateY(), LADO_JUGADOR, jugador);
        recorrerSprites();
        NODOS.removeIf(nodo -> !nodo.isVisible());
        enemigos.removeIf(enemigo -> !enemigo.isVisible());
        if (pasos++ > 200) pasos = 0;
    }

    /**
     * Recorre y actua sobre cada sprite de la RAIZ con el tiempo
     */
    private static void recorrerSprites() {
        enemigos.forEach(enemigo -> {
            random = Math.random();
            limitarShooter(enemigo.getTranslateX(), enemigo.getTranslateY(), LADO_ENEMIGO_A, enemigo);
            if (enemigo.limites.intersects(jugador.limites)) //Si intersecta con el jugador
                jugador.setVisible(false);//muere el jugador
            if (pasos > 200 && random < 0.4) { //Si es tiempo de disparar y le toca por azar
                enemigo.disparar(); // dispara
                if (random < 0.1) enemigo.moverIzquierda();
                else if (random < 0.2) enemigo.moverArriba();
                else if (random < 0.3) enemigo.moverAbajo();
                else enemigo.moverDerecha();
            }
        });
        NODOS_U.stream().filter(nodo -> nodo instanceof ShotSprite).map(nodo -> (ShotSprite) nodo).forEach(proyectil -> {
            if (proyectilFueraDelLimite(proyectil.getTranslateX(), proyectil.getTranslateY()))
                proyectil.setVisible(false);
            if (proyectil.TIPO.matches(".+enemigoA")) { //Si es el proyectil del enemigo
                proyectil.moverAbajo(); //Entonces se mueve hacia abajo
                if (proyectil.getBoundsInParent().intersects(jugador.limites)) { //Si toca al jugador
                    jugador.setVisible(false);//muere el jugador
                    proyectil.setVisible(false); // muere el proyectil
                }
            } else { //Si no es el proyectil enemigo, es el del jugador
                jugador.dirigir(proyectil); // Entonces se mueve hacia donde se disparó
                enemigos.forEach(enemigo -> { // Y para cada nodo...//Si el nodo es el enemigo entonces
                    if (proyectil.getBoundsInParent().intersects(enemigo.limites)) { //si la bala y el enemigo intersectan
                        enemigo.setVisible(false); // el enemigo muere
                        proyectil.setVisible(false); // el proyectil muere
                    }
                });
            }
        });
    }

    /**
     * Método que mantiene al ShooterSprite dentro del area de juego
     *
     * @param x           posición horizontal del ShooterSprite
     * @param y           posición vertical del ShoterSprite
     * @param ladoShooter longitud del ShoterSprite
     * @param shooter     ShoterSprite a limitar
     */
    private static void limitarShooter(double x, double y, int ladoShooter, ShooterSprite shooter) {
        if (x < borde)
            shooter.moverDerecha(); //Si se sale por la izquierda no necesita revisar si se sale por la derecha
        else if (x > widthInterno + borde - ladoShooter) shooter.moverIzquierda();
        if (y < borde) shooter.moverAbajo();//Si se sale por arriba no necesita revisar si se sale por abajo
        else if (y > heightInterno + borde - ladoShooter) shooter.moverArriba();
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
