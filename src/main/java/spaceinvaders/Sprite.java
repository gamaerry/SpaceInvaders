package spaceinvaders;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sprite extends Rectangle {
    final String TIPO;
    char direccion;

    Sprite(int x, int y, int w, int h, Color color, String tipo, char direccion) {
        super(w, h, color);
        setTranslateX(x);
        setTranslateY(y);
        TIPO = tipo;
        this.direccion = direccion;
    }

    void moverIzquierda() {
        setTranslateX(getTranslateX() - 5);
    }

    void moverDerecha() {
        setTranslateX(getTranslateX() + 5);
    }

    void moverAbajo() {
        setTranslateY(getTranslateY() + 5);
    }

    void moverArriba() {
        setTranslateY(getTranslateY() - 5);
    }
}
