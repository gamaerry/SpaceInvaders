package spaceinvaders;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Clase que sirve como base a cada objeto animado dentro del programa
 * que extiende de Canvas, que permite el uso de figuras geométricas
 * (los proyectiles, por ejemplo, son objetos Sprite)
 */
public class Sprite extends Canvas {
    /**
     * Objeto que sirve para manipular el objeto de tipo Canvas
     */
    final GraphicsContext GC = getGraphicsContext2D();

    /**
     * Cadena de caracteres que indica qué tipo de Sprite es el objeto
     */
    final String TIPO;

    /**
     * Primitivo de tipo char que indica la dirección de cada objeto en el programa
     * que puede ser 'a', 's', 'd' o 'w'
     * (para cada proyectil determina el rumbo que tomará cuando sea disparado,
     * para cada ShooterSprite determina el rumbo de todos sus proyectiles en este estado)
     */
    private char direccion;

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
     */
    Sprite(int w, int h, int x, int y, String tipo, char direccion, Color color) {
        super(w, h);
        setTranslateX(x);
        setTranslateY(y);
        TIPO = tipo;
        this.direccion = direccion;
        if (tipo.matches("proyectil.*")) {
            GC.setFill(color);
            //GC.fillRect(0, 0, w, h);
            GC.fillRoundRect(0, 0, w, h, 10, 10);
        }
    }

    /**
     * Mueve al Sprite 2 pixeles a la izquierda
     */
    void moverIzquierda(int a) {
        setTranslateX(getTranslateX() - 0.04*a);
    }

    /**
     * Mueve al Sprite 2 pixeles a la derecha
     */
    void moverDerecha(int a) {
        setTranslateX(getTranslateX() + 0.04*a);
    }

    /**
     * Mueve al Sprite 2 pixeles a la abajo
     */
    void moverAbajo(int a) {
        setTranslateY(getTranslateY() + 0.04*a);
    }

    /**
     * Mueve al Sprite 2 pixeles a la arriba
     */
    void moverArriba(int a) {
        setTranslateY(getTranslateY() - 0.04*a);
    }

    /**
     * Método setter de la propiedad direccion
     *
     * @param s Dirección del Sprite
     */
    void setDireccion(char s) {
        direccion = s;
    }

    /**
     * Método getter de la propiedad direccion
     *
     * @return Valor de la propiedad direccion
     */
    char getDireccion() {
        return direccion;
    }
}
