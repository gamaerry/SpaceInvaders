package spaceinvaders;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;

import static spaceinvaders.ContentBuilder.*;

/**
 * Clase que hereda de Sprite que adiciona los métodos disparar() y dirigir(),
 * y sobreescribe el método setDirección()
 */
public class ShooterSprite extends Sprite {
    Bounds limites = getBoundsInParent();

    /**
     * Una vez que se llama al contructor de Sprite,
     * se verifica si se trata de un jugador o de un enemigo.
     * Este contructor no pide dirección, pues quienes disparan se crean
     * con una dirección por defecto 'w' (hacia arriba) para el JUGADOR,
     * 's' (hacia abajo) para el enemigo
     *
     * @param l     Width and height
     * @param x     Posición en el eje x
     * @param y     Posición en el eje y
     * @param tipo  Qué tipo de Sprite es
     * @param color Color del Sprite
     */
    ShooterSprite(int l, int x, int y, String tipo, Color color) {
        super(l, l, x, y, tipo, 's', color);
        if (tipo.equals("jugador")) { //Si es el jugador
            super.setDireccion('w'); //Se cambia la dirección de 's' a 'w'
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
            GC.setFill(COLOR_ENEMIGO_A);
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
                getDireccion() == 'w' || getDireccion() == 's' ? 5 : 20,
                getDireccion() == 'w' || getDireccion() == 's' ? 20 : 5,
                (int) (getTranslateX() + getWidth() / 2),//centro vertical
                (int) (getTranslateY() + getHeight() / 2),//centro horizontal
                "proyectil" + TIPO,
                getDireccion(),
                COLOR_PROYECTIL);
        NODOS.add(proyectil);
    }

    /**
     * Método que dirige el proyectil según su propia dirección
     *
     * @param proyectil Proyectil disparado por el ShooterSprite
     */
    void dirigir(Sprite proyectil) {
        switch (proyectil.getDireccion()) {
            // El rumbo depende de la direccion del proyectil que se le pasó cuando se creó/disparó)
            case 'a':
                proyectil.moverIzquierda(velProyectilJugador);
                //(De este modo se moverán 4 pixeles a la izquierda)
                break;
            case 's':
                proyectil.moverAbajo(velProyectilJugador);
                break;
            case 'd':
                proyectil.moverDerecha(velProyectilJugador);
                break;
            default:
                proyectil.moverArriba(velProyectilJugador);
                break;
        }
    }

    /**
     * Método que sobreescribe al existente en la clase padre que agrega la funcionalidad
     * de modificar el Canvas del jugador en función de su dirección
     *
     * @param direccion Dirección del ShooterSprite que se configura como 'a','s','d' o 'w'
     */
    @Override
    void setDireccion(char direccion) {
        super.setDireccion(direccion);
        if (TIPO.equals("jugador")) {
            //Primero se borra el contenido del Canvas
            //usando el objeto GC situado en la clase Sprite
            GC.clearRect(0, 0, ladoJugador, ladoJugador);
            //Se crea la figura mas grande del jugador (apuntador):
            GC.setFill(COLOR_APUNTADOR);
            GC.fillPolygon(
                    new double[]{ladoJugador / 2.0, ladoJugador, ladoJugador / 2.0, 0},
                    new double[]{0, ladoJugador / 2.0, ladoJugador, ladoJugador / 2.0},
                    4);
            //Se crea la forma que completará al jugador:
            double[] puntosEjeX, puntosEjeY; //Aquí serán almacenados los puntos que cambian en función de la dirección
            switch (direccion) {
                case 'a': //Esta posición fué un feliz error que le terminó gustando al autor del código
                    puntosEjeX = new double[]{
                            3 * ladoJugador / 4.0,
                            ladoJugador,
                            3 * ladoJugador / 4.0,
                            ladoJugador / 8.0};
                    puntosEjeY = new double[]{
                            ladoJugador / 8.0,
                            ladoJugador / 2.0,
                            7 * ladoJugador / 8.0,
                            ladoJugador / 2.0,};
                    break;
                case 'd': //La posición para 'd' es un reflejo exacto del caso 'a'
                    puntosEjeX = new double[]{
                            ladoJugador / 4.0,
                            0,
                            ladoJugador / 4.0,
                            7 * ladoJugador / 8.0};
                    puntosEjeY = new double[]{
                            ladoJugador / 8.0,
                            ladoJugador / 2.0,
                            7 * ladoJugador / 8.0,
                            ladoJugador / 2.0};
                    break;
                case 's': //La pocisión para abajo es un reflejo exacto de la que es hacia arriba
                    puntosEjeX = new double[]{
                            ladoJugador / 2.0,
                            7 * ladoJugador / 8.0,
                            ladoJugador / 2.0,
                            ladoJugador / 8.0};
                    puntosEjeY = new double[]{
                            0,
                            ladoJugador / 4.0,
                            7 * ladoJugador / 8.0,
                            ladoJugador / 4.0};
                    break;
                default: //Este es el caso que queda, 'w'
                    puntosEjeX = new double[]{
                            ladoJugador / 2.0,
                            7 * ladoJugador / 8.0,
                            ladoJugador / 2.0,
                            ladoJugador / 8.0};
                    puntosEjeY = new double[]{
                            ladoJugador / 8.0,
                            3 * ladoJugador / 4.0,
                            ladoJugador,
                            3 * ladoJugador / 4.0};
            }
            GC.setFill(COLOR_JUGADOR);
            GC.fillPolygon(puntosEjeX, puntosEjeY, 4);
        }
    }

    /**
     * Método que sobreescribe al del nivel superior que actualiza el campo límites del ShooterSprite
     *
     * @param a velocidad del movimiento del sprite
     */
    @Override
    void moverIzquierda(int a) {
        super.moverIzquierda(a);
        limites = getBoundsInParent();
    }

    /**
     * Método que sobreescribe al del nivel superior que actualiza el campo límites del ShooterSprite
     *
     * @param a velocidad del movimiento del sprite
     */
    @Override
    void moverDerecha(int a) {
        super.moverDerecha(a);
        limites = getBoundsInParent();
    }

    /**
     * Método que sobreescribe al del nivel superior que actualiza el campo límites del ShooterSprite
     *
     * @param a velocidad del movimiento del sprite
     */
    @Override
    void moverArriba(int a) {
        super.moverArriba(a);
        limites = getBoundsInParent();
    }

    /**
     * Método que sobreescribe al del nivel superior que actualiza el campo límites del ShooterSprite
     *
     * @param a velocidad del movimiento del sprite
     */
    @Override
    void moverAbajo(int a) {
        super.moverAbajo(a);
        limites = getBoundsInParent();
    }
}
