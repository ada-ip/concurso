
package practicaconcurso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Esta clase representa una pregunta de letras del juego Concurso de Números y Letras seleccionada de forma aleatoria.
 * La pregunta será de adivinar una palabra elegida al azar. La palabra se elige al azar de un fichero llamado diccionario.txt 
 * y siempre tendrá más de 3 letras.
 * Para que el jugador tenga que adivinar la palabra se modifican x número de letras (el número de letras de 
 * la palabra dividido por 3) por asteriscos.
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 17-05-2022
 */
public class PreguntaLetras extends Pregunta {

    /************** Atributos de la clase **************/
    
    private String palabraConLetrasOcultas;        // La palabra elegida al azar con varias letras sustituidas por asteriscos

    
    /************** Constructor de la clase **************/
    /**
     * Crea un objeto PreguntaLetras con una respuesta correcta que será una palabra elegida al azar del fichero diccionario.txt, 
     * una palabra con letras ocultas que será la palabra elegida al azar con x letras sustituidas por asteriscos, y un 
     * enunciado que incluirá la palabra elegida con letras ocultas.
     */
    public PreguntaLetras() {
        this.respuestaCorrecta = elegirPalabra();
        this.palabraConLetrasOcultas = ocultarLetras(this.respuestaCorrecta);
        this.enunciado = "¿Cuál es la siguiente palabra?: " + this.palabraConLetrasOcultas;
    }


    /************** Métodos de la clase **************/
    
    /**
     * Selecciona una palabra al azar con más de tres letras del documento diccionario.txt
     * 
     * @return la palabra elegida
     */
    private String elegirPalabra() {
        // Se crea un objeto file con el archivo diccionario.txt
        File archDiccionario = new File("diccionario.txt");

        // Se calculan los bytes que ocupa el archivo
        long bytesArchivo = archDiccionario.length();
        
        String palabraElegida = "";

        try {
            // Se crea un objeto RandomAccessFile para poder elegir una palabra al azar
            RandomAccessFile lectorArchivo = new RandomAccessFile(archDiccionario, "r");
            
            // Se calcula una posición aleatoria del archivo y se lleva el puntero a esa posición
            long bytes = (long) (Math.random() * bytesArchivo);
            lectorArchivo.seek(bytes);
            
            /* Se lee el resto de la palabra en la que ha caído el puntero y se almacena la palabra siguiente,
            siempre y cuando la palabra tenga más de 3 letras
            */
            lectorArchivo.readLine();
            do {
                palabraElegida = lectorArchivo.readLine();
                
                /* Si se ha llegado al final del fichero se mueve el puntero al inicio del fichero y se elige la
                primera palabra del archivo */
                if (palabraElegida == null) {
                    lectorArchivo.seek(0);
                    palabraElegida = lectorArchivo.readLine();
                }
            }
            while (palabraElegida.length() <= 3);

            lectorArchivo.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return palabraElegida;
    }

    
    /**
     * Sustituye una letra de una palabra por un asterisco por cada 3 letras de la palabra
     * 
     * @param palabra que es la palabra cuyas letras se quieren sustituir
     * @return la palabra con 1 de cada 3 letras sustituidas por asteriscos
     */
    private String ocultarLetras(String palabra) {
        
        char[] letras = palabra.toCharArray();
        int posicion;
        
        // Se sustituye 1 de cada 3 letras por asteriscos
        for(int i = 0; i < letras.length / 3; i++) {
            do {
                /* Elige en la primera pasada una letra de entre la primera y la tercera, en la segunda una letra
                de entre la cuarta y la sexta etc */
                posicion = (int) (i * 3 + Math.random()*((i + 1) * 3 - 1 - i * 3 + 1));
            }
            while (letras[posicion] == '*');
            letras[posicion] = '*';
        }
        
        return String.valueOf(letras);
    }
}
