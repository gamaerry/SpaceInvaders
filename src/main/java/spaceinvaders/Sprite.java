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
     * Color principal del Sprite
     */
    final Color COLOR;

    /**
     * Cadena de caracteres que indica qué tipo de Sprite es el objeto
     */
    final String TIPO;

    /**
     * Primitivo de tipo char que indica la dirección de cada objeto en el programa
     * que puede ser 'a', 's', 'd' o 'w'
     * (para cada ShotSprite determina el rumbo que tomará cuando sea disparado,
     * para cada ShooterSprite determina el rumbo de todos sus proyectiles en este estado)
     */
    char direccion;

    Sprite(int w, int h, int x, int y, String tipo, char direccion, Color color) {
        super(w, h);
        setTranslateX(x);
        setTranslateY(y);
        COLOR = color;
        TIPO = tipo;
        this.direccion = direccion;
        if (tipo.matches("campo")) {
            GC.setFill(COLOR);
            GC.fillRoundRect(0, 0, w, h, 20, 20);
            widthProperty().addListener((observable, oldValue, newValue) -> {
                GC.clearRect(0, 0, oldValue.intValue(), getHeight());
                GC.fillRoundRect(0, 0, newValue.intValue(), getHeight(), 20, 20);
            });//dibujar de nuevo el canvas en función de su ancho
            heightProperty().addListener((observable, oldValue, newValue) -> {
                GC.clearRect(0, 0, getWidth(), oldValue.intValue());
                GC.fillRoundRect(0, 0, getWidth(), newValue.intValue(), 20, 20);
            });//dibujar de nuevo el canvas en función de su altura
        }
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
