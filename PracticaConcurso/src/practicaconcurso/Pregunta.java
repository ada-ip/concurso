
package practicaconcurso;

/**
 * Esta clase abstracta representa una pregunta del juego Concurso de Números y Letras con un enunciado y una respuesta correcta.
 * La clase tiene métodos para mostrar el enunciado, mostrar la respuesta correcta y comprobar si una respuesta de 
 * un jugador es correcta o no.
 * El enunciado y la respuesta correcta de la pregunta se generarán según se indique en sus clases derivadas.
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 16-05-2022
 */
public class Pregunta {

    /************** Atributos de la clase **************/
    
    protected String enunciado;                 // El enunciado completo de la pregunta ya preparado para mostrarse por pantalla
    protected String respuestaCorrecta;         // La respuesta correcta a la pregunta

    
    /************** Getters/setters de la clase **************/
    
    /**
     * Devuelve el enunciado de la pregunta
     * @return el enunciado de la pregunta
     */
    public String getEnunciado() {
        return enunciado;
    }

    
    /************** Métodos de la clase **************/

    /**
     * Muestra el enunciado de la pregunta por pantalla
     */
    public void mostrarPregunta() {
        System.out.println(getEnunciado());
        
        // Se muestra la respuesta correcta para poder comprobar que el programa funciona correctamente
        System.out.println("respuesta: " + respuestaCorrecta);
    }

    /**
     * Comprueba si la respuesta de un jugador a la pregunta se corresponde con la respuesta correcta
     * @param jugador que es el jugador que ha contestado a la pregunta
     * @return true si el jugador ha contestado bien y false si ha contestado mal
     */
    public boolean comprobarRespuesta(Jugador jugador) {
        boolean haAcertado = false;
        
        if(respuestaCorrecta.equals(jugador.getRespuesta())) {
            haAcertado = true;
        }
        
        return haAcertado;
    }

    /**
     * Muestra la respuesta correcta a la pregunta
     */
    public void mostrarRespuestaCorrecta() {
        System.out.println("La respuesta correcta es: " + respuestaCorrecta);
    }
}
