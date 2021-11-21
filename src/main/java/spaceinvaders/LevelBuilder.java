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

import java.util.HashSet;

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
    static final int LADO_JUGADOR = 50;
    static int velJugador = 70,
            velProyectilJugador = 80;

    /**
     * Constantes enteras para los enemigos
     */
    static final int LADO_ENEMIGO_A = 40,
            VEL_ENEMIGO_A = 300,
            VEL_PROYECTIL_ENEMIGO_A = 60;

    /**
     * booleanos que mantienen el estado de los botones pulsados
     */
    static boolean left = false, up = false, down = false, right = false;

    /**
     * Pasos que se darán mediante el método actualizar
     * del objeto de tipo AnimationTimer
     */
    static int pasos = 0;

    /**
     * Objeto auxiliar para los valores aleatorios
     */
    static double random = 0;

    /**
     * Raíz de tipo Pane en donde se coloca el nivel
     * que será regresado a la clase MainContentBuilder
     */
    final static Pane RAIZ = new Pane();

    /**
     * Lista de nodos, en realidad todos los nodos son Canvas
     * (un tipo particular de Node)
     */
    final static ObservableList<Node> NODOS = RAIZ.getChildren();

    final static Canvas CAMPO = new Canvas(widthInterno, heightInterno);

    /**
     * Lista de enemigos
     */
    final static HashSet<ShooterSprite> ENEMIGOS = new HashSet<>();

    /**
     * Lista de proyectiles
     */
    final static HashSet<Sprite> PROYECTILES = new HashSet<>();

    /**
     * El jugador es del tipo ShooterSprite
     * un tipo particular de Sprite
     */
    final static ShooterSprite JUGADOR = new ShooterSprite(
            LADO_JUGADOR,
            widthInterno / 2 - 20,
            heightInterno - 50,
            0,
            COLOR_JUGADOR,
            velJugador,
            'w',
            velProyectilJugador);

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
            case ESCAPE:

        }
    };

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
     * Este método contiene el flujo principal del juego,
     * llamando los métodos necesarios para su funcionamiento
     *
     * @return Regresa el Parent "Panel" en donde se coloca todo
     */
    static Parent levelContent(int nivel) {
        RAIZ.setStyle("-fx-background-color: darkslategray");
        establecerDimensiones();
        establecerCampo();
        NODOS.add(JUGADOR);
        establecerEnemigos(nivel);
        final AnimationTimer TEMPORIZADOR = new AnimationTimer() {
            @Override
            public void handle(long now) {
                actualizarJugador();
                actualizarSprites();// se encargará de desactivar nodos
                NODOS.removeIf(Node::isDisable);// borra de raiz (Pane) si estan desactivados:
                if (pasos++ > 200) pasos = 0;
            }
        };
        TEMPORIZADOR.start();
        return RAIZ;
    }

    private static void establecerCampo() {
        final GraphicsContext GC = CAMPO.getGraphicsContext2D();
        GC.setFill(COLOR_CAMPO);
        GC.fillRoundRect(0, 0, widthInterno, heightInterno, 20, 20);
        CAMPO.widthProperty().addListener((observable, oldValue, newValue) -> {
            GC.clearRect(0, 0, oldValue.intValue(), CAMPO.getHeight());
            GC.fillRoundRect(0, 0, newValue.intValue(), CAMPO.getHeight(), 20, 20);
        });//dibujar de nuevo el canvas en función de su ancho
        CAMPO.heightProperty().addListener((observable, oldValue, newValue) -> {
            GC.clearRect(0, 0, CAMPO.getWidth(), oldValue.intValue());
            GC.fillRoundRect(0, 0, CAMPO.getWidth(), newValue.intValue(), 20, 20);
        });//dibujar de nuevo el canvas en función de su altura
        NODOS.add(CAMPO);
    }

    /**
     * Método que establece las dimenciones de la raíz
     */
    private static void establecerDimensiones() {
        RAIZ.setPrefSize(widthInterno + 2 * borde, heightInterno + 2 * borde);
        RAIZ.widthProperty().addListener((observable, oldValue, newValue) -> {
            widthInterno = newValue.intValue() - 2 * borde;
            CAMPO.setWidth(widthInterno);
        });
        RAIZ.heightProperty().addListener((observable, oldValue, newValue) -> {
            heightInterno = newValue.intValue() - 2 * borde;
            CAMPO.setHeight(heightInterno);
        });
    }

    /**
     * Este método crea a los enemigos y los coloca cada 100 pixeles
     * empezando por el 90 (recordar que el width es de 600 pixeles)
     */
    static void establecerEnemigos(int nivel) {
        switch (nivel) {
            case 1:
                for (int i = 0; i < 20; i++) {
                    ShooterSprite enemigo = new ShooterSprite(
                            LADO_ENEMIGO_A,
                            90 + i % 5 * 100,
                            i < 5 ? 100 : (i < 10 ? 150 : (i < 15 ? 200 : 250)),
                            1,
                            COLOR_ENEMIGO_A,
                            VEL_ENEMIGO_A,
                            's',
                            VEL_PROYECTIL_ENEMIGO_A);
                    NODOS.add(enemigo);
                    ENEMIGOS.add(enemigo);
                }
                break;
        }
    }

    /**
     * Este método actualiza al jugador con el tiempo
     */
    static void actualizarJugador() {
        if (left) JUGADOR.moverIzquierda();
        if (up) JUGADOR.moverArriba();
        if (down) JUGADOR.moverAbajo();
        if (right) JUGADOR.moverDerecha();
        //if (space && pasos % 15 == 0) jugador.disparar();
        limitarShooter(LADO_JUGADOR, JUGADOR);
    }

    /**
     * Recorre y actua sobre cada sprite de la RAIZ con el tiempo
     */
    private static void actualizarSprites() {
        //ACTUALIZA ENEMIGOS:
        ENEMIGOS.forEach(enemigo -> {
            random = Math.random();
            limitarShooter(LADO_ENEMIGO_A, enemigo);
            if (enemigo.limites.intersects(JUGADOR.limites)) {
                enemigo.setDisable(true);
                JUGADOR.setDisable(true);
            }
            if (pasos > 200 && random < 0.4) { //Si es tiempo de disparar y le toca por azar
                enemigo.disparar(); // dispara
                if (random < 0.1) enemigo.moverIzquierda();
                else if (random < 0.2) enemigo.moverArriba();
                else if (random < 0.3) enemigo.moverAbajo();
                else enemigo.moverDerecha();
            }
        });
        ENEMIGOS.removeIf(Node::isDisable); //borra de enemigos si esta desactivado

        //ACTUALIZA PROYECTILES:
        PROYECTILES.forEach(proyectil->{
            dirigirProyectil(proyectil);
            if(proyectil.ENEMIGO==0)
                ENEMIGOS.forEach(enemigo -> matarSiIntersectan(proyectil, enemigo));
            else
                matarSiIntersectan(proyectil, JUGADOR);
            if (proyectilFueraDelLimite(proyectil.getTranslateX(), proyectil.getTranslateY()))
                proyectil.setDisable(true);
        });
        PROYECTILES.removeIf(Node::isDisable);
    }

    /**
     * Método que mantiene al ShooterSprite dentro del area de juego
     *
     * @param ladoShooter longitud del ShoterSprite
     * @param shooter     ShoterSprite a limitar
     */
    private static void limitarShooter(int ladoShooter, ShooterSprite shooter) {
        if (shooter.getTranslateX() < borde) //Si se sale por la izquierda no necesita revisar si se sale por la derecha
            shooter.moverDerecha();
        else if (shooter.getTranslateX() > widthInterno + borde - ladoShooter)
            shooter.moverIzquierda();
        if (shooter.getTranslateY() < borde) //Si se sale por arriba no necesita revisar si se sale por abajo
            shooter.moverAbajo();
        else if (shooter.getTranslateY() > heightInterno + borde - ladoShooter)
            shooter.moverArriba();
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

    private static void matarSiIntersectan(Sprite proyectil, ShooterSprite shooter) {
        if (proyectil.getBoundsInParent().intersects(shooter.limites)) { //si la bala y el enemigo intersectan
            shooter.setDisable(true); // el shooter muere
            proyectil.setDisable(true); // el proyectil muere
        }
    }

    private static void dirigirProyectil(Sprite proyectil) {
        switch (proyectil.direccion) {
            case 'a':
                proyectil.moverIzquierda();
                break;
            case 's':
                proyectil.moverAbajo();
                break;
            case 'd':
                proyectil.moverDerecha();
                break;
            default:
                proyectil.moverArriba();
        }
    }
}
