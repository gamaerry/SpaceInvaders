package spaceinvaders;

import javafx.scene.paint.Color;

public class ShotSprite extends Sprite {
    final int VELOCIDAD;

    /**
     * Es el contructor de todos los objetos animados en el programa. Si se trata
     * de un proyectil, se dibuja un rectángulo redondeado del color especificado.
     * Observar que se llama primero al constructor de la clase Canvas para especificar
     * sus dimensiones, y los demás parámetros se establecen de modo convencional
     *
     * @param w         Width
     * @param h         Height
     * @param x         Posición en el eje x
     * @param y         Posición en el eje y
     * @param tipo      Qué tipo de Sprite es
     * @param direccion Con qué dirección va a ser usado
     * @param color     Color del Sprite
     * @param velocidad Velocidad a la que se mueve
     */
    ShotSprite(int w, int h, int x, int y, String tipo, char direccion, Color color, int velocidad) {
        super(w, h, x, y, tipo, direccion, color);
        VELOCIDAD = velocidad;
        GC.fillRoundRect(0, 0, w, h, 10, 10);
    }

    /**
     * Mueve al Sprite 0.04*VELOCIDAD pixeles a la izquierda
     */
    void moverIzquierda() {
        setTranslateX(getTranslateX() - 0.05 * VELOCIDAD);
    }

    /**
     * Mueve al Sprite 0.04*VELOCIDAD pixeles a la derecha
     */
    void moverDerecha() {
        setTranslateX(getTranslateX() + 0.05 * VELOCIDAD);
    }

    /**
     * Mueve al Sprite 0.04*VELOCIDAD pixeles a la abajo
     */
    void moverAbajo() {
        setTranslateY(getTranslateY() + 0.05 * VELOCIDAD);
    }

    /**
     * Mueve al Sprite 0.04*VELOCIDAD pixeles a la arriba
     */
    void moverArriba() {
        setTranslateY(getTranslateY() - 0.05 * VELOCIDAD);
    }
}
