package spaceinvaders;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
public class Sprite extends Rectangle {
    char direccion='s';
    boolean muerto=false;
    final String TIPO;
    Sprite(int x, int y, int w, int h, String tipo, Color color){
        super(w, h, color);
        TIPO=tipo;
        setTranslateX(x);
        setTranslateY(y);
        if(tipo.equals("jugador")) direccion='w';
        if(tipo.equals("disparojugador")) direccion='w';
    }
    void moverIzquierda(){
        setTranslateX(getTranslateX()-5);
    }
    void moverDerecha(){
        setTranslateX(getTranslateX()+5);
    }
    void moverAbajo(){setTranslateY(getTranslateY()+5);}
    void moverArriba(){
        setTranslateY(getTranslateY()-5);
    }

}
