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
            GC.setFill(COLOR_ENEMIGO);
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
                proyectil.moverIzquierda(VEL_PROYECTIL_JUGADOR);
                //(De este modo se moverán 4 pixeles a la izquierda)
                break;
            case 's':
                proyectil.moverAbajo(VEL_PROYECTIL_JUGADOR);
                break;
            case 'd':
                proyectil.moverDerecha(VEL_PROYECTIL_JUGADOR);
                break;
            default:
                proyectil.moverArriba(VEL_PROYECTIL_JUGADOR);
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
            GC.clearRect(0, 0, LADO_JUGADOR, LADO_JUGADOR);
            //Se crea la figura mas grande del jugador (apuntador):
            GC.setFill(COLOR_APUNTADOR);
            GC.fillPolygon(
                    new double[]{LADO_JUGADOR / 2.0, LADO_JUGADOR, LADO_JUGADOR / 2.0, 0},
                    new double[]{0, LADO_JUGADOR / 2.0, LADO_JUGADOR, LADO_JUGADOR / 2.0},
                    4);
            //Se crea la forma que completará al jugador:
            double[] puntosEjeX, puntosEjeY; //Aquí serán almacenados los puntos que cambian en función de la dirección
            switch (direccion) {
                case 'a': //Esta posición fué un feliz error que le terminó gustando al autor del código
                    puntosEjeX = new double[]{
                            3 * LADO_JUGADOR / 4.0,
                            LADO_JUGADOR,
                            3 * LADO_JUGADOR / 4.0,
                            LADO_JUGADOR / 8.0};
                    puntosEjeY = new double[]{
                            LADO_JUGADOR / 8.0,
                            LADO_JUGADOR / 2.0,
                            7 * LADO_JUGADOR / 8.0,
                            LADO_JUGADOR / 2.0,};
                    break;
                case 'd': //La posición para 'd' es un reflejo exacto del caso 'a'
                    puntosEjeX = new double[]{
                            LADO_JUGADOR / 4.0,
                            0,
                            LADO_JUGADOR / 4.0,
                            7 * LADO_JUGADOR / 8.0};
                    puntosEjeY = new double[]{
                            LADO_JUGADOR / 8.0,
                            LADO_JUGADOR / 2.0,
                            7 * LADO_JUGADOR / 8.0,
                            LADO_JUGADOR / 2.0};
                    break;
                case 's': //La pocisión para abajo es un reflejo exacto de la que es hacia arriba
                    puntosEjeX = new double[]{
                            LADO_JUGADOR / 2.0,
                            7 * LADO_JUGADOR / 8.0,
                            LADO_JUGADOR / 2.0,
                            LADO_JUGADOR / 8.0};
                    puntosEjeY = new double[]{
                            0,
                            LADO_JUGADOR / 4.0,
                            7 * LADO_JUGADOR / 8.0,
                            LADO_JUGADOR / 4.0};
                    break;
                default: //Este es el caso que queda, 'w'
                    puntosEjeX = new double[]{
                            LADO_JUGADOR / 2.0,
                            7 * LADO_JUGADOR / 8.0,
                            LADO_JUGADOR / 2.0,
                            LADO_JUGADOR / 8.0};
                    puntosEjeY = new double[]{
                            LADO_JUGADOR / 8.0,
                            3 * LADO_JUGADOR / 4.0,
                            LADO_JUGADOR,
                            3 * LADO_JUGADOR / 4.0};
            }
            GC.setFill(COLOR_JUGADOR);
            GC.fillPolygon(puntosEjeX, puntosEjeY, 4);
        }
    }

    @Override
    void moverIzquierda(int a) {
        super.moverIzquierda(a);
        limites = getBoundsInParent();
    }

    @Override
    void moverDerecha(int a) {
        super.moverDerecha(a);
        limites = getBoundsInParent();
    }

    @Override
    void moverArriba(int a) {
        super.moverArriba(a);
        limites = getBoundsInParent();
    }

    @Override
    void moverAbajo(int a) {
        super.moverAbajo(a);
        limites = getBoundsInParent();
    }
}
