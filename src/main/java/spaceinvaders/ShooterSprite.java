package spaceinvaders;

import javafx.scene.paint.Color;

import static spaceinvaders.ContentBuilder.NODOS;

public class ShooterSprite extends Sprite {
    ShooterSprite(int w, int h, int x, int y, String tipo, Color color) {
        //Este contructor no pide dirección, pues quienes disparan se crean con una dirección por defecto
        super(w, h, x, y, tipo, 's', color);
        if (tipo.equals("jugador")) {
            direccion = 'w';
            GC.setFill(color);
            GC.fillPolygon(
                    new double[]{w/2.0, w, w/2.0, 0},
                    new double[]{0, h/2.0, h, h/2.0},
                    4);
            GC.setFill(Color.BLUE);
            GC.fillPolygon(
                    new double[]{w/2.0, 7*w/8.0, w/2.0, w/8.0},
                    new double[]{h/4.0, 5*h/8.0, h, 5*h/8.0},
                    4);
        } else{
            GC.setFill(color);
            GC.fillRect(0, 0, w, h);
        }
    }

    void disparar() {
        Sprite proyectil = new Sprite(
                direccion == 'w' || direccion == 's' ? 5 : 20,
                direccion == 'w' || direccion == 's' ? 20 : 5,
                (int) getTranslateX() + 20,
                (int) getTranslateY(),
                "proyectil" + TIPO,
                direccion,
                Color.BLACK);
        NODOS.add(proyectil);
    }

    void dirigir(Sprite proyectil) {
        switch (proyectil.direccion) {
            // La dirección depende de la direccion del proyectil que tenía cuando se disparó)
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
                break;
        }
    }
}
