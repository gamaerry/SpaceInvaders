package spaceinvaders;

import javafx.scene.paint.Color;
import static spaceinvaders.ContentBuilder.*;

public class ShooterSprite extends Sprite {
    ShooterSprite(int w, int h, int x, int y, String tipo, Color color) {
        //Este contructor no pide dirección, pues quienes disparan se crean con una dirección por defecto
        //'w' (hacia arriba) para el JUGADOR, 's' (hacia abajo) para el enemigo
        super(w, h, x, y, tipo, 's', color);
        if (tipo.equals("jugador")) {
            super.setDireccion('w');
            GC.setFill(COLOR_APUNTADOR);
            GC.fillPolygon(
                    new double[]{w/2.0, w, w/2.0, 0},
                    new double[]{0, h/2.0, h, h/2.0},
                    4);
            GC.setFill(color);
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
                getDireccion() == 'w' || getDireccion() == 's' ? 5 : 20,
                getDireccion() == 'w' || getDireccion() == 's' ? 20 : 5,
                (int) (getTranslateX()+getWidth()/2),
                (int) (getTranslateY()+getHeight()/2),
                "proyectil" + TIPO,
                getDireccion(),
                COLOR_PROYECTIL);
        NODOS.add(proyectil);
    }

    void dirigir(Sprite proyectil) {
        switch (proyectil.getDireccion()) {
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
    @Override
    void setDireccion(char d){
        super.setDireccion(d);
        if(TIPO.equals("jugador")){
            GC.clearRect(0, 0, LADO_JUGADOR, LADO_JUGADOR);
            GC.setFill(COLOR_APUNTADOR);
            GC.fillPolygon(
                    new double[]{LADO_JUGADOR/2.0, LADO_JUGADOR,     LADO_JUGADOR/2.0, 0},
                    new double[]{0,                LADO_JUGADOR/2.0, LADO_JUGADOR,     LADO_JUGADOR/2.0},
                    4);
            double xa,xb,xc,xd,ya,yb,yc,yd;
            switch(d){
                case 'a':
                    xa=3*LADO_JUGADOR/4.0; xb=LADO_JUGADOR; xc=3*LADO_JUGADOR/4.0; xd=LADO_JUGADOR/8.0;
                    ya=LADO_JUGADOR/8.0; yb=LADO_JUGADOR/2.0; yc=7*LADO_JUGADOR/8.0; yd=LADO_JUGADOR/2.0;
                    break;
                case 'd':
                    xa=LADO_JUGADOR/4.0; xb=0; xc=LADO_JUGADOR/4.0; xd=7*LADO_JUGADOR/8.0;
                    ya=LADO_JUGADOR/8.0; yb=LADO_JUGADOR/2.0; yc=7*LADO_JUGADOR/8.0; yd=LADO_JUGADOR/2.0;
                    break;
                case 's':
                    xa=LADO_JUGADOR/2.0; xb=7*LADO_JUGADOR/8.0; xc=LADO_JUGADOR/2.0; xd=LADO_JUGADOR/8.0;
                    ya=0; yb=3*LADO_JUGADOR/8.0; yc=3*LADO_JUGADOR/4.0; yd=3*LADO_JUGADOR/8.0;
                    break;
                default:
                    xa=LADO_JUGADOR/2.0; xb=7*LADO_JUGADOR/8.0; xc=LADO_JUGADOR/2.0; xd=LADO_JUGADOR/8.0;
                    ya=LADO_JUGADOR/4.0; yb=5*LADO_JUGADOR/8.0; yc=LADO_JUGADOR; yd=5*LADO_JUGADOR/8.0;
            }
            GC.setFill(COLOR_JUGADOR);
            GC.fillPolygon(new double[]{xa,xb,xc,xd}, new double[]{ya,yb,yc,yd}, 4);
        }
    }
}
