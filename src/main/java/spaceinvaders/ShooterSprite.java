package spaceinvaders;

import javafx.scene.paint.Color;

import static spaceinvaders.ContentBuilder.*;

/**
 * Clase que hereda de Sprite que adiciona los métodos disparar() y dirigir(),
 * y sobreescribe el método setDirección()
 */
public class ShooterSprite extends Sprite {
    /**
     * Una vez que se llama al contructor de Sprite,
     * se verifica si se trata de un jugador o de un enemigo.
     * Este contructor no pide dirección, pues quienes disparan se crean
     * con una dirección por defecto 'w' (hacia arriba) para el JUGADOR,
     * 's' (hacia abajo) para el enemigo
     *
     * @param w     Width
     * @param h     Height
     * @param x     Posición en el eje x
     * @param y     Posición en el eje y
     * @param tipo  Qué tipo de Sprite es
     * @param color Color del Sprite
     */
    ShooterSprite(int w, int h, int x, int y, String tipo, Color color) {
        super(w, h, x, y, tipo, 's', color);
        if (tipo.equals("jugador")) { //Si es el jugador
            super.setDireccion('w'); //Se cambia la dirección de 's' a 'w'
            //Se crea la figura mas grande del jugador (apuntador):
            GC.setFill(COLOR_APUNTADOR);
            GC.fillPolygon(
                    new double[]{w / 2.0, w, w / 2.0, 0},
                    new double[]{0, h / 2.0, h, h / 2.0},
                    4);
            //Se crea la figura central del jugador (con COLOR_JUGADOR):
            GC.setFill(color);
            GC.fillPolygon(
                    new double[]{w / 2.0, 7 * w / 8.0, w / 2.0, w / 8.0},
                    new double[]{h / 4.0, 5 * h / 8.0, h, 5 * h / 8.0},
                    4);
        } else {//Si no es el jugador, es el enemigo
            GC.setFill(color); //Se usa el COLOR_ENEMIGO
            GC.fillRect(0, 0, w, h); //Se dibuja un cuadrado
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
                            3 * LADO_JUGADOR / 8.0,
                            3 * LADO_JUGADOR / 4.0,
                            3 * LADO_JUGADOR / 8.0};
                    break;
                default: //Este es el caso que queda, 'w'
                    puntosEjeX = new double[]{
                            LADO_JUGADOR / 2.0,
                            7 * LADO_JUGADOR / 8.0,
                            LADO_JUGADOR / 2.0,
                            LADO_JUGADOR / 8.0};
                    puntosEjeY = new double[]{
                            LADO_JUGADOR / 4.0,
                            5 * LADO_JUGADOR / 8.0,
                            LADO_JUGADOR,
                            5 * LADO_JUGADOR / 8.0};
            }
            GC.setFill(COLOR_JUGADOR);
            GC.fillPolygon(puntosEjeX, puntosEjeY, 4);
        }
    }
}
