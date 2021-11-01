package spaceinvaders;
import javafx.scene.paint.Color;
import static spaceinvaders.ContentBuilder.raiz;

public class ShooterSprite extends Sprite{
    char direccion='s';
    ShooterSprite(int x, int y, int w, int h, String tipo, Color color) {
        super(x, y, w, h, tipo, color);
        if(tipo.equals("jugador")) direccion='w';
    }
    void disparar() {
        Sprite disparo=new Sprite(
                (int) getTranslateX()+20,
                (int) getTranslateY(),
                direccion=='w'|| direccion=='s'?5:20,
                direccion=='w'|| direccion=='s'?20:5,
                "disparo"+TIPO,
                Color.BLACK);
        raiz.getChildren().add(disparo);
    }
}
