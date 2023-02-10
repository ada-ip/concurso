
package practicaconcurso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Esta clase representa una pregunta de inglés del juego Concurso de Números y Letras seleccionada de forma aleatoria.
 * La pregunta será una pregunta escrita en inglés seleccionada de forma aleatoria de la tabla pregunta de la base
 * de datos del juego.
 * La pregunta tendrá cuatro posibles respuestas y una respuesta correcta.
 * La clase tiene un método para mostrar las respuestas ordenadas de forma aleatoria.
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 17-05-2022
 */
public class PreguntaIngles extends Pregunta {

    /************** Atributos de la clase **************/
    
    private String[] respuestas;        // Guarda las cuatro posibles respuestas a la enunciado ordenadas aleatoriamente


    /************** Constructor de la clase **************/
    /**
     * Crea un objeto PreguntaIngles con un enunciado que se selecciona aleatoriamente de la tabla pregunta de la base de datos 
     * concurso, una respuesta correcta que será la indicada como respuesta correcta a dicha pregunta en la tabla enunciado, y 
     * cuatro posibles respuestas que serán todas las respuestas a dicha pregunta almacenadas en la tabla pregunta ordenadas aleatoriamente.
     */
    public PreguntaIngles() {
        
        // Se selecciona una pregunta de la base de datos
        String[] preguntaElegida = seleccionarPregunta();
        
        // Se inicializan los atributos sólo si no ha habido ningún problema al elegir una pregunta al azar
        if(preguntaElegida[0] != null) {
            this.enunciado = preguntaElegida[0];
            this.respuestaCorrecta = preguntaElegida[1];
            
            // Se introducen las respuestas en un array auxiliar para poder ordenarlas aleatoriamente después
            String[] posiblesRespuestas = new String[4];
            for (int i = 0; i < posiblesRespuestas.length; i++) {
                posiblesRespuestas[i] = preguntaElegida[i + 1];
            }
            this.respuestas = ordenarRespuestas(posiblesRespuestas);
        }
    }
    
    
    /************** Métodos de la clase **************/
    
    /**
     * Ordena las posibles respuestas a la pregunta de forma aleatoria
     * 
     * @return un array de Strings con las respuestas ordenadas
     */
    private String[] ordenarRespuestas(String[] respuestas) {
        
        // Se crea un array auxiliar para devolver las respuestas ordenadas
        String[] respuestasOrdenadas = new String[respuestas.length];
        
        /* Se eligen posiciones aleatorias del array y se van rellenando con las respuestas hasta
        que tenga todos sus elementos con valor */
        int posicionAleatoria;
        for (int i = 0; i < respuestasOrdenadas.length; i++) {
            do {
                posicionAleatoria = (int)(Math.random() * respuestasOrdenadas.length);
            }
            while(respuestasOrdenadas[posicionAleatoria] != null);
            
            respuestasOrdenadas[posicionAleatoria] = respuestas[i];
        }
        
        return respuestasOrdenadas;
    }
    
    
    /**
     * Muestra todas las posibles respuestas a la enunciado
     */
    private void mostrarRespuestas() {
        System.out.println("A: " + this.respuestas[0]);
        System.out.println("B: " + this.respuestas[1]);
        System.out.println("C: " + this.respuestas[2]);
        System.out.println("D: " + this.respuestas[3]);
    }
    
    
    /**
     * Comprueba si la respuesta de un jugador a la pregunta se corresponde con la respuesta correcta
     * @param jugador que es el jugador que ha contestado a la pregunta
     * @return true si el jugador ha contestado bien y false si ha contestado mal
     */
    @Override
    public boolean comprobarRespuesta(Jugador jugador) {
        boolean haAcertado = false;
        
        String respuesta = jugador.getRespuesta();
        
        // Si el texto que ha introducido el jugador es la respuesta correcta el jugador ha acertado
        if(respuestaCorrecta.equals(respuesta)) {
            haAcertado = true;
        }
        // Si el jugador en su lugar ha introducido sólo una A, B, C o D también puede haber acertado
        else {
            // Se obtiene si la respuesta correcta se ha mostrado como A, B, C o D
            int indiceRespuestaCorrecta = 0;
            for (int i = 0; i < respuestas.length; i++) {
                if (respuestaCorrecta.equals(respuestas[i])) {
                    indiceRespuestaCorrecta = i;
                }
            }
            
            // Se comprueba si el jugador ha acertado
            switch (indiceRespuestaCorrecta) {
                case 0:
                    if(respuesta.equals("A") || respuesta.equals("a")) {
                        haAcertado = true;
                    }
                    break;
                case 1:
                    if(respuesta.equals("B") || respuesta.equals("b")) {
                        haAcertado = true;
                    }
                    break;
                case 2:
                    if(respuesta.equals("C") || respuesta.equals("c")) {
                        haAcertado = true;
                    }
                    break;
                case 3:
                    if(respuesta.equals("D") || respuesta.equals("d")) {
                        haAcertado = true;
                    }
                    break;
            }
        }
        
        return haAcertado;
    }
    
    
    /**
     * Elige una pregunta al azar almacenada en la tabla pregunta de la base de datos del concurso y devuelve
     * los campos de enunciado, respuesta correcta, respuesta incorrecta 1, respuesta incorrecta 2 y
     * respuesta incorrecta 3
     * 
     * @return un array de Strings con todos los campos que se necesitan de la pregunta elegida al azar
     */
    private String[] seleccionarPregunta() {
        
        String[] preguntaElegida = new String[5];
        
        try {
            // Se instancia el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Se crea una conexión con la base de datos del concurso
            String url = "jdbc:mysql://localhost:3306/concurso";
            Connection con = DriverManager.getConnection(url, "root", "1234");
            
            // Se crea un objeto Statement para poder hacer una query sobre la base de datos
            Statement stm = con.createStatement();
            
            // Se calcula con una consulta resumen cuántas preguntas hay almacenadas en la base de datos
            String queryNumeroPreguntas = "SELECT COUNT(idPregunta) AS numeroPreguntas FROM pregunta";
            ResultSet rsNumeroPreguntas = stm.executeQuery(queryNumeroPreguntas);
            
            // Si el número de preguntas es 0 la tabla está vacía y no se puede devolver ninguna pregunta
            rsNumeroPreguntas.next();
            if(rsNumeroPreguntas.getInt("numeroPreguntas") == 0) {
                System.out.println("Error: No hay ninguna pregunta en la tabla pregunta");
            }
            /* Si el número de preguntas es mayor que 0 se elige un número al azar, y se selecciona la pregunta
            que tenga el id de ese número al azar.
            Puede ser que se hayan modificado los datos de la tabla y los id de las preguntas no coincidan con 
            el número de filas que hay y por lo tanto al buscar una pregunta por el id se obtenga un resultado vacío,
            en ese caso se vuelve a elegir otra pregunta al azar */
            else {
                ResultSet rsPreguntaElegida;
                do {
                    int filaPregunta = (int)(1 + Math.random() * (rsNumeroPreguntas.getInt("numeroPreguntas") - 1 + 1));
                
                    String queryPreguntaElegida = "SELECT enunciado, respuesta_correcta, respuesta_incorrecta_1, "
                                            + "respuesta_incorrecta_2, respuesta_incorrecta_3 FROM pregunta "
                                            + "WHERE idPregunta = " + filaPregunta;
                
                    rsPreguntaElegida = stm.executeQuery(queryPreguntaElegida);
                }
                while(!rsPreguntaElegida.next());
                
                // Se almacena el resultado obtenido de la consulta en el array preguntaElegida
                preguntaElegida[0] = rsPreguntaElegida.getString("enunciado");
                preguntaElegida[1] = rsPreguntaElegida.getString("respuesta_correcta");
                preguntaElegida[2] = rsPreguntaElegida.getString("respuesta_incorrecta_1");
                preguntaElegida[3] = rsPreguntaElegida.getString("respuesta_incorrecta_2");
                preguntaElegida[4] = rsPreguntaElegida.getString("respuesta_incorrecta_3");
            }
            
            // Se cierran el Statement y la conexión
            stm.close();
            con.close();
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return preguntaElegida;
    }
    
    
    /**
     * Muestra el enunciado de la pregunta por pantalla junto con las posibles respuestas
     */
    @Override
    public void mostrarPregunta() {
        super.mostrarPregunta();
        mostrarRespuestas();
    }
}
