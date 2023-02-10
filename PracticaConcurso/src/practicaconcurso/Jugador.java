
package practicaconcurso;

/**
 * Esta clase representa un jugador del juego Concurso Números y Letras con un nombre identificativo, sus puntos
 * en la partida que esté jugando y su respuesta a la última pregunta que ha respondido.
 * La clase incluye métodos para actualizar los puntos del jugador cuando acierte una pregunta, mostrar su puntuación y
 * actualizar la respuesta a su última respuesta.
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 16-05-2022
 */
public class Jugador {
    
    /************** Atributos de la clase **************/
    
    private String nombre;              // El nombre del jugador que tiene que ser único en el sistema
    private int puntosPartida;          // La puntuación del jugador durante la partida
    private String respuesta;           // La respuesta del jugador a la última pregunta a la que ha contestado

    
    /************** Constructor de la clase **************/
    /**
     * Crea un objeto de la clase Jugador con un nombre dado y con una puntuación de 0 puntos
     * @param nombre que es el nombre del jugador
     */
    public Jugador(String nombre) {
        this.nombre = nombre;
        puntosPartida = 0;
    }

    
    /************** Setters/Getters de la clase **************/
    /**
     * Devuelve el nombre de un jugador
     * @return el nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la puntuación de un jugador
     * @return los puntos del jugador
     */
    public int getPuntosPartida() {
        return puntosPartida;
    }

    /**
     * Devuelve la respuesta del jugador
     * @return la respuesta del jugador
     */
    public String getRespuesta() {
        return respuesta;
    }
    
    
    /************** Métodos de la clase **************/

    /**
     * Le suma un punto a la puntuación del jugador
     */
    public void sumarPunto() {
        puntosPartida++;
    }

    /**
     * Muestra por pantalla los puntos del jugador
     */
    public void mostrarPuntos() {
        System.out.println(nombre + ": " + puntosPartida);
    }

    /**
     * Guarda la respuesta del jugador a una pregunta en el atributo respuesta
     * @param respuesta que es la respuesta del jugador a una pregunta
     */
    public void contestarPregunta(String respuesta) {
        this.respuesta = respuesta;
    }
}
