package spaceinvaders;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;

import static spaceinvaders.LevelBuilder.COLOR_APUNTADOR;
import static spaceinvaders.LevelBuilder.COLOR_PROYECTIL;
import static spaceinvaders.LevelBuilder.NODOS;
import static spaceinvaders.LevelBuilder.PROYECTILES;


/**
 * Clase que hereda de Sprite que adiciona los métodos disparar() y dirigir(),
 * y sobreescribe el método setDirección()
 */
public class ShooterSprite extends Sprite {
    Bounds limites = getBoundsInParent();
    final int VELOCIDAD_DISPARO;
    final int L;

    /**
     * Una vez que se llama al constructor de Sprite,
     * se verifica si se trata de un jugador o de un enemigo.
     * Este constructor no pide dirección, pues quienes disparan se crean
     * con una dirección por defecto 'w' (hacia arriba) para el JUGADOR,
     * 's' (hacia abajo) para el enemigo
     *
     * @param l                length del ShooterSprite
     * @param x                Posición en el eje x
     * @param y                Posición en el eje y
     * @param color            Color del Sprite
     * @param velocidad        Velocidad a la que se mueve
     * @param velocidadDisparo Velocidad de sus disparos
     */
    ShooterSprite(int l, int x, int y, int enemigo, Color color, int velocidad, char direccion, int velocidadDisparo) {
        super(l, l, x, y, enemigo, color, velocidad, direccion);
        VELOCIDAD_DISPARO = velocidadDisparo;
        L =l;
        if (enemigo == 0) { //Si es el jugador
            //Se crea la figura mas grande del jugador (apuntador):
            GC.setFill(COLOR_APUNTADOR);
            GC.fillPolygon(new double[]{l / 2.0, l, l / 2.0, 0},
                    new double[]{0, l / 2.0, l, l / 2.0}, 4);
            //Se crea la figura central del jugador (con COLOR_JUGADOR):
            GC.setFill(color);
            GC.fillPolygon(new double[]{l / 2.0, 7 * l / 8.0, l / 2.0, l / 8.0},
                    new double[]{l / 8.0, 3 * l / 4.0, l, 3 * l / 4.0}, 4);
        } else {//Si no es el jugador, es el enemigo
            //Se crea la figura mas grande del enemigo (apuntador):
            GC.setFill(COLOR_APUNTADOR);
            double ladoCuadrado = Math.sqrt(l * l / 2.0),
                    esquinaCuadrado = (l - ladoCuadrado) / 2.0;
            GC.fillRect(esquinaCuadrado, esquinaCuadrado, ladoCuadrado, ladoCuadrado); //Se dibuja un cuadrado
            GC.fillPolygon(new double[]{l / 2.0, l, l / 2.0, 0}, new double[]{0, l / 2.0, l, l / 2.0}, 4);//Se dibuja el rombo
            //Se crea la figura central del enemigo (con COLOR_ENEMIGO):
            GC.setFill(color);
            GC.fillPolygon(new double[]{3 * l / 4.0, 3 * l / 4.0, l / 2.0, l / 4.0, l / 4.0},
                    new double[]{0, l / 2.0, 7 * l / 8.0, l / 2.0, 0}, 5);
        }
    }

    /**
     * Método que crea el proyectil del ShooterSprite
     */
    void disparar() {
        //Se crea un proyectil que depende de la dirección del SooterSprite
        //para dibujar su ancho y su alto
        Sprite proyectil = new Sprite(
                5,
                20,
                limites.getCenterX(),//centro vertical
                limites.getCenterY(),//centro horizontal
                ENEMIGO * -1,
                COLOR_PROYECTIL,
                VELOCIDAD_DISPARO,
                direccion);
        NODOS.add(proyectil);
        PROYECTILES.add(proyectil);
    }

    /**
     * Método que sobreescribe al existente en la clase padre que agrega la funcionalidad
     * de modificar el Canvas del jugador en función de su dirección
     *
     * @param direccion Dirección del ShooterSprite que se configura como 'a','s','d' o 'w'
     */
    void setDireccion(char direccion) {
        this.direccion=direccion;
        if (ENEMIGO==0) {
            //Primero se borra el contenido del Canvas
            //usando el objeto GC situado en la clase Sprite
            GC.clearRect(0, 0, L, L);
            //Se crea la figura mas grande del jugador (apuntador):
            GC.setFill(COLOR_APUNTADOR);
            GC.fillPolygon(
                    new double[]{L / 2.0, L, L / 2.0, 0},
                    new double[]{0, L / 2.0, L, L / 2.0},
                    4);
            //Se crea la forma que completará al jugador:
            double[] puntosEjeX, puntosEjeY; //Aquí serán almacenados los puntos que cambian en función de la dirección
            switch (direccion) {
                case 'a': //Esta posición fué un feliz error que le terminó gustando al autor del código
                    puntosEjeX = new double[]{
                            3 * L / 4.0,
                            L,
                            3 * L / 4.0,
                            L / 8.0};
                    puntosEjeY = new double[]{
                            L / 8.0,
                            L / 2.0,
                            7 * L / 8.0,
                            L / 2.0,};
                    break;
                case 'd': //La posición para 'd' es un reflejo exacto del caso 'a'
                    puntosEjeX = new double[]{
                            L / 4.0,
                            0,
                            L / 4.0,
                            7 * L / 8.0};
                    puntosEjeY = new double[]{
                            L / 8.0,
                            L / 2.0,
                            7 * L / 8.0,
                            L / 2.0};
                    break;
                case 's': //La pocisión para abajo es un reflejo exacto de la que es hacia arriba
                    puntosEjeX = new double[]{
                            L / 2.0,
                            7 * L / 8.0,
                            L / 2.0,
                            L / 8.0};
                    puntosEjeY = new double[]{
                            0,
                            L / 4.0,
                            7 * L / 8.0,
                            L / 4.0};
                    break;
                default: //Este es el caso que queda, 'w'
                    puntosEjeX = new double[]{
                            L / 2.0,
                            7 * L / 8.0,
                            L / 2.0,
                            L / 8.0};
                    puntosEjeY = new double[]{
                            L / 8.0,
                            3 * L / 4.0,
                            L,
                            3 * L / 4.0};
            }
            GC.setFill(COLOR);
            GC.fillPolygon(puntosEjeX, puntosEjeY, 4);
        }
    }

    /**
     * Mueve al Sprite 0.05a pixeles a la izquierda
     */
    @Override
    void moverIzquierda() {
        super.moverIzquierda();
        limites = getBoundsInParent();
    }

    /**
     * Mueve al Sprite 0.05a pixeles a la derecha
     */
    @Override
    void moverDerecha() {
        super.moverDerecha();
        limites = getBoundsInParent();
    }

    /**
     * Mueve al Sprite 0.05a pixeles a la abajo
     */
    @Override
    void moverAbajo() {
        super.moverAbajo();
        limites = getBoundsInParent();
    }

    /**
     * Mueve al Sprite 0.05a pixeles a la arriba
     */
    @Override
    void moverArriba() {
        super.moverArriba();
        limites = getBoundsInParent();
    }
}
