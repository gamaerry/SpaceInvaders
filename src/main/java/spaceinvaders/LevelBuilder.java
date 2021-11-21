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
     * Lista de enemigos
     */
    static ArrayList<ShooterSprite> enemigos = new ArrayList<>();

    /**
     * Lista de proyectiles huerfanos (proyectiles sin ShooterSprite)
     */
    static ArrayList<ShotSprite> huerfanos=new ArrayList<>();

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
            widthInterno, heightInterno, borde, borde, "campo", 'd', COLOR_CAMPO);

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
            case ESCAPE:

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
                actualizarJugador();
                //actualizarSprites() se encargará de desactivar nodos:
                actualizarSprites();
                //NODOS.removeIf() borra de raiz (Pane) si estan desactivados:
                NODOS.removeIf(Node::isDisable);
                if (pasos++ > 200) pasos = 0;
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
     * Este método actualiza al jugador con el tiempo
     */
    static void actualizarJugador(){
        if (left) jugador.moverIzquierda();
        if (up) jugador.moverArriba();
        if (down) jugador.moverAbajo();
        if (right) jugador.moverDerecha();
        //if (space && pasos % 15 == 0) jugador.disparar();
        limitarShooter(LADO_JUGADOR, jugador);
    }

    /**
     * Recorre y actua sobre cada sprite de la RAIZ con el tiempo
     */
    private static void actualizarSprites() {
        //ACTUALIZA enemigos y enemigo.PROYECTILES:
        enemigos.forEach(enemigo -> {
            random = Math.random();
            limitarShooter(LADO_ENEMIGO_A, enemigo);
            if(enemigo.limites.intersects(jugador.limites)) {
                huerfanos.addAll(enemigo.PROYECTILES);
                enemigo.setDisable(true);
                jugador.setDisable(true);
            }
            if (pasos > 200 && random < 0.4) { //Si es tiempo de disparar y le toca por azar
                enemigo.disparar(); // dispara
                if (random < 0.1) enemigo.moverIzquierda();
                else if (random < 0.2) enemigo.moverArriba();
                else if (random < 0.3) enemigo.moverAbajo();
                else enemigo.moverDerecha();
            }
            enemigo.PROYECTILES.forEach(proyectil -> {
                if (proyectilFueraDelLimite(proyectil.getTranslateX(), proyectil.getTranslateY()))
                    proyectil.setDisable(true);
                enemigo.dirigir(proyectil); //Entonces se mueve hacia abajo
                matarSiIntersectan(proyectil, jugador);
            });
            enemigo.PROYECTILES.removeIf(Node::isDisable); //borra de enemigo.PROYECTILES si no es visible
            //Si matan a uno, la lista huerfanos se encarga de dirigir al proyectil sin shooter
        });
        enemigos.removeIf(Node::isDisable); //borra de enemigos si esta desactivado

        //ACTUALIZA jugador.PROYECTILES:
        jugador.PROYECTILES.forEach(proyectil -> {
            if (proyectilFueraDelLimite(proyectil.getTranslateX(), proyectil.getTranslateY())) {
                proyectil.setDisable(true);
            }
            jugador.dirigir(proyectil); // Entonces se mueve hacia donde se disparó
            enemigos.forEach(enemigo -> matarSiIntersectan(proyectil, enemigo));
        });
        jugador.PROYECTILES.removeIf(Node::isDisable); //borra de jugador.PROYECTILES si esta desactivado

        //ACTUALIZA huerfanos:
        huerfanos.forEach(proyectil->{
            if (proyectilFueraDelLimite(proyectil.getTranslateX(), proyectil.getTranslateY()))
                proyectil.setDisable(true);
            if (proyectil.getBoundsInParent().intersects(jugador.limites)) { //si la bala y el enemigo intersectan
                jugador.setDisable(true); // el enemigo muere
                proyectil.setDisable(true); // el proyectil muere
            }//huerfanos no puede usar matarSiIntersectan() pues este método modifica al mismo huerfanos
            dirigirHuerfano(proyectil);
        });
        huerfanos.removeIf(Node::isDisable);// borra de huerfanos si no es visible
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

    private static void matarSiIntersectan(ShotSprite proyectil, ShooterSprite shooter) {
        if (proyectil.getBoundsInParent().intersects(shooter.limites)) { //si la bala y el enemigo intersectan
            huerfanos.addAll(shooter.PROYECTILES); //proyectiles del shooter quedan huerfanos
            shooter.setDisable(true); // el shooter muere
            proyectil.setDisable(true); // el proyectil muere
        }
    }
    
    private static void dirigirHuerfano(ShotSprite proyectil){
        switch(proyectil.direccion){
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
