package spaceinvaders;

import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import static spaceinvaders.LevelBuilder.COLOR_PROYECTIL;

/**
 * Clase que sirve como base a cada objeto animado dentro del programa
 * que extiende de Canvas, que permite el uso de figuras geométricas
 * (los proyectiles, por ejemplo, son objetos Sprite)
 */
public class Sprite extends Canvas {
    final static Rotate ROTAR_90= new Rotate(90);
    /**
     * Objeto que sirve para manipular el objeto de tipo Canvas
     */
    final GraphicsContext GC = getGraphicsContext2D();
    final ObservableList<Transform> TR=getTransforms();
    final double VELOCIDAD;
    final int ENEMIGO_ID;
    final boolean ES_ENEMIGO;

    /**
     * Color principal del Sprite
     */
    final Color COLOR;

    /**
     * Primitivo de tipo char que indica la dirección de cada objeto en el programa
     * que puede ser 'a', 's', 'd' o 'w'
     * (para cada ShotSprite determina el rumbo que tomará cuando sea disparado,
     * para cada ShooterSprite determina el rumbo de todos sus proyectiles en este estado)
     */
    char direccion;

    Sprite(int w, int h, double x, double y, int enemigoId, Color color, int velocidad, char direccion) {
        super(w, h);
        setTranslateX(x);
        setTranslateY(y);
        ENEMIGO_ID = enemigoId;
        ES_ENEMIGO = ENEMIGO_ID > 0;
        COLOR = color;
        VELOCIDAD = velocidad;
        this.direccion = direccion;
    }

    Sprite(int w, int h, double x, double y, int enemigoId, int velocidad, char direccion) { //este builder no usa el field color
        this(w, h, x, y, enemigoId, COLOR_PROYECTIL, velocidad, direccion);
        GC.fillRoundRect(0, 0, w, h, 10, 10);
        if (direccion == 'a' || direccion == 'd') TR.add(ROTAR_90);
    }

    /**
     * Mueve al Sprite 0.05*VELOCIDAD pixeles a la izquierda
     */
    void moverIzquierda() {
        setTranslateX(getTranslateX() - 0.05 * VELOCIDAD);
    }

    /**
     * Mueve al Sprite 0.05*VELOCIDAD pixeles a la derecha
     */
    void moverDerecha() {
        setTranslateX(getTranslateX() + 0.05 * VELOCIDAD);
    }

    /**
     * Mueve al Sprite 0.05*VELOCIDAD pixeles a la abajo
     */
    void moverAbajo() {
        setTranslateY(getTranslateY() + 0.05 * VELOCIDAD);
    }

    /**
     * Mueve al Sprite 0.05*VELOCIDAD pixeles a la arriba
     */
    void moverArriba() {
        setTranslateY(getTranslateY() - 0.05 * VELOCIDAD);
    }
}
