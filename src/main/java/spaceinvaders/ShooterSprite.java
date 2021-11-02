package spaceinvaders;

import javafx.scene.paint.Color;

import static spaceinvaders.ContentBuilder.NODOS;

public class ShooterSprite extends Sprite {
    ShooterSprite(int x, int y, int w, int h, Color color, String tipo) {
        super(x, y, w, h, color, tipo, 's');
        if (tipo.equals("jugador")) direccion = 'w';
    }

    void disparar() {
        Sprite disparo = new Sprite(
                (int) getTranslateX() + 20,
                (int) getTranslateY(),
                direccion == 'w' || direccion == 's' ? 5 : 20,
                direccion == 'w' || direccion == 's' ? 20 : 5,
                Color.BLACK,
                "disparo" + TIPO,
                direccion);
        NODOS.add(disparo);
    }

    void dirigir(Sprite disparo) {
        switch (disparo.direccion) { //La dirección está en función del proyectil
            // depende de la direccion que tenía cuando se disparó
            case 'a':
                disparo.moverIzquierda();
                break;
            case 's':
                disparo.moverAbajo();
                break;
            case 'd':
                disparo.moverDerecha();
                break;
            default:
                disparo.moverArriba();
                break;
        }
    }
}
