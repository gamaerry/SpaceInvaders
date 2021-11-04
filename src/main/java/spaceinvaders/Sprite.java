package spaceinvaders;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Sprite extends Canvas {
    final GraphicsContext GC = getGraphicsContext2D();
    final String TIPO;
    char direccion;

    Sprite(int w, int h, int x, int y, String tipo, char direccion, Color color) {
        super(w, h);
        setTranslateX(x);
        setTranslateY(y);
        TIPO = tipo;
        this.direccion = direccion;

        if (tipo.matches("proyectil.*")) {
            System.out.println("MATCH!");
            GC.setFill(color);
            GC.fillRect(0, 0, w, h);
            //GC.fillRoundRect(0, 0, w, h,10,10);
        }
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
