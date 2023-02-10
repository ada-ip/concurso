package practicaconcurso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Esta clase representa una partida del Concurso de Números y Letras con una lista de jugadores de entre 
 * 1 y 6 jugadores, un número de rondas que puede ser de 3 (partida rápida), 5 (partida corta), 
 * 10 (partida normal) o 20 (partida larga), y una lista de preguntas elegidas al azar que pueden ser
 * numéricas (adivinar el resultado de una expresión matemática), de letras (adivinar una palabra)
 * o de inglés.
 * La clase tiene métodos para inicializar los jugadores, asignar un turno aleatorio a los jugadores, inicializar
 * el tipo de juego (número de rondas), generar preguntas aleatorias para la partida, jugar una ronda, mostrar
 * las puntuaciones de todos los jugadores, mostrar el ganador, y para añadir y eliminar jugadores y preguntas
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 19-05-2022
 */
public class Partida {

    /************** Atributos de la clase **************/
    
    private int numRondas;                     // El número de rondas de la partida
    private int[] turnos;                      // Qué turno tiene cada jugador
    private ArrayList<Jugador> jugadores;      // Lista de jugadores de la partida
    private ArrayList<Pregunta> preguntas;     // Lista de preguntas de la partida

    
    /************** Constructor de la clase **************/
    /**
     * Crea un objeto Partida con un determinado número de rondas elegido por el usuario, una lista con los jugadores
     * que van a participar en la partida, el turno que va a tener cada jugador, y la lista de preguntas que se
     * van a realizar a los jugadores.
     */
    public Partida() {
        inicializarTipoJuego();
        inicializarJugadores();
        asignarTurnos();
        generarPreguntas();
    }
    
    
    /************** Setters/getters de la clase **************/

    /**
     * Devuelve el número de rondas de la partida
     * @return el número de rondas
     */
    public int getNumRondas() {
        return numRondas;
    }

    /**
     * Devuelve los turnos de los jugadores de la partida
     * @return los turnos de los jugadores
     */
    public int[] getTurnos() {
        return turnos;
    }
    
    
    /************** Métodos de la clase **************/
    
    /**
     * Pregunta a los jugadores por el número de jugadores que van a participar en la partida y le pide a cada
     * uno su nombre de jugador que deberá estar registrado en la base de datos del concurso, y si no está registrado,
     * da la opción de registrarse o elegir otro nombre.
     * El número de jugadores permitido es de entre 1 y 6.
     * Los nombres de los jugadores tienen que ser únicos, no contener espacios y no tener más de 
     * 25 caracteres.
     */
    private void inicializarJugadores() {
        
        Scanner entrada = new Scanner(System.in);
        
        // Se le pide al usuario el número de jugadores
        int numJugadores = 0;
        do {
            System.out.print("\nIntroduce el número de jugadores (max 6): ");
            
            if(!entrada.hasNextInt()){
                System.out.print("Número de jugadores no válido. ");
                entrada.nextLine();
            }
            else {
                numJugadores = entrada.nextInt();
                entrada.nextLine();
                
                if(numJugadores < 1 || numJugadores > 6) {
                    System.out.print("Número de jugadores no válido. ");
                }
            }
        }
        while (numJugadores < 1 || numJugadores > 6); 
        
        
        /* Una vez se tiene el número de jugadores de la partida, se le pide a cada jugador su nombre.
        Se crea un obtejo GestionJugadores para poder comprobar que todos los jugadores están registrados
        en la base de datos y si no se registran */
        GestionJugadores gestorJugadores = new GestionJugadores();
        this.jugadores = new ArrayList<>();
        
        for(int i = 0; i < numJugadores; i++) {
            
            // Variable auxiliar para controlar que el jugador se añade correctamente a la partida
            boolean jugadorValido = false;
            do {
                System.out.print("Introduce el nombre del jugador " + (i+1) + ": ");
            
                String nombreJugador = entrada.nextLine();
                
                // Se comprueba si el jugador ya existe en la partida actual
                if(jugadorRepetido(nombreJugador)){
                    System.out.println("El jugador ya existe en la partida");
                }
                else {
                    /* Si el jugador está registrado en la base de datos se crea un objeto Jugador con su
                    nombre y se añade a la partida */
                    if(gestorJugadores.comprobarJugador(nombreJugador)) {
                        Jugador nuevoJugador = new Jugador(nombreJugador);
                        addJugador(nuevoJugador);
                        jugadorValido = true;
                    }
                    // Si no, se registra el jugador en la base de datos y después ya se añade a la partida
                    else {
                        // El jugador sólo se añade a la partida si se ha registrado correctamente
                        if (gestorJugadores.registrarJugador(nombreJugador)) {
                            Jugador nuevoJugador = new Jugador(nombreJugador);
                            addJugador(nuevoJugador);
                            jugadorValido = true;
                        }
                    }
                }

            }
            while(jugadorValido == false); 
        }
        
        gestorJugadores.cerrarConexion();
    }

    
    /**
     * Pregunta a los jugadores el tipo de juego (el número de partidas que quieren jugar) y lo almacena en
     * el atributo numRondas
     */
    private void inicializarTipoJuego() {
        
        Scanner entrada = new Scanner(System.in);
        this.numRondas = 0;
        
        /* Se le pregunta un número de rondas a los jugadores hasta que introduzcan un número válido 
        (3, 5, 10 o 20) */
        do{
            System.out.println("Introduce el número de rondas que quiere jugar: 3 (partida rápida), 5 (partida corta)," 
                            + " 10 (partida normal), 20 (partida larga)");
            
            if(!entrada.hasNextInt()){
                System.out.print("Error: número de rondas no válido. ");
                entrada.nextLine();
            }
            else {
                this.numRondas = entrada.nextInt();
                entrada.nextLine();
            
                if(!(numRondas == 3 || numRondas == 5 || numRondas == 10 || numRondas == 20)) {
                    System.out.print("Número de rondas no válido. ");
                }
            }
        }
        while(!(numRondas == 3 || numRondas == 5 || numRondas == 10 || numRondas == 20));
    }

    
    /**
     * Permite asignar a cada jugador de la partida un turno de forma aleatoria
     */
    private void asignarTurnos() {
        
        this.turnos = new int[getJugadores().size()];
        
        /* Vector auxiliar que se utiliza para evitar que se elija el turno de un mismo jugador más de una vez. 
        Cada elemento del vector representa si el jugador almacenado en la misma posición del ArrayList jugadores ya 
        tiene el turno asignado o no */
        boolean[] jugadoresYaElegidos = new boolean[getJugadores().size()];
        Arrays.fill(jugadoresYaElegidos, false);
        
        // Variable auxiliar que almacena el número del jugador que se va a colocar en el array de turnos.
        int jugadorAleatorio = 0;
        
        for (int i = 0; i < getTurnos().length; i++) {
            
            // Se elige el jugador que va a tener el turno i + 1
            do {
                jugadorAleatorio = (int)(Math.random()*getJugadores().size());
            }
            while(jugadoresYaElegidos[jugadorAleatorio] == true);
 
            // Se guarda el número de jugador en el array de turnos
            this.turnos[i] = jugadorAleatorio;
            
            // Se indica en el vector auxiliar que el jugador ya tiene turno
            jugadoresYaElegidos[jugadorAleatorio] = true;
        }
    }

    
    /**
     * Permite jugar una ronda completa del juego Concurso de Números y Letras.
     * Pregunta a cada jugador la pregunta que le toque responder de la lista de preguntas de la 
     * partida, comprueba si el jugador ha acertado o no, y le añade un punto a su marcador si ha acertado.
     * 
     * @param numRonda que es el número de ronda que se está jugando
     */
    public void jugarRonda(int numRonda) {
        
        // Se crea un objeto entrada para leer lo que introduzca el jugador por teclado
        Scanner entrada = new Scanner(System.in);
        
        for(int i = 0; i < getJugadores().size(); i++) {
            
            Jugador jugador = getJugadores().get(getTurnos()[i]);
            
            // Se obtiene la pregunta que le toca al jugador y se muestra por pantalla
            Pregunta pregunta = preguntas.get((numRonda * jugadores.size()) + i);
            /* Si es una pregunta de inglés y ha sucedido algún error a la hora de seleccionar una 
            pregunta de la base de datos, se muestra un mensaje de error y se pasa a la siguiente pregunta */
            if(pregunta.getEnunciado() == null) {
                System.out.println("Error: Pregunta no encontrada.");
            }
            else {
                System.out.println("\n" + jugador.getNombre() + " responda la siguiente pregunta:");
                pregunta.mostrarPregunta();
            
                // Se lee la respuesta del jugador
                String respuesta = entrada.nextLine();
                jugador.contestarPregunta(respuesta);
            
                // Se compara la respuesta del jugador con la respuesta correcta almacenada en el objeto Pregunta
                if(pregunta.comprobarRespuesta(jugador)) {  
                    System.out.println("¡Respuesta correcta!");
                    jugador.sumarPunto();
                }
                else {
                    System.out.print("!Respuesta incorrecta! ");
                    pregunta.mostrarRespuestaCorrecta();
                }
            }
        }
    }

    
    /**
     * Selecciona aleatoriamente el tipo de pregunta del que será cada pregunta de la partida, 
     * crea dichas preguntas y las almacena en la lista de preguntas de la partida.
     * Las preguntas pueden ser de tipo numérico, de letras o de inglés.
     */
    private void generarPreguntas() {
        this.preguntas = new ArrayList<>();
        
        // Se crean preguntas suficientes para todos los jugadores y todas las rondas
        for (int i = 0; i < jugadores.size() * numRondas; i++) {
            
            int tipoPregunta = (int)(Math.random()*3);
            
            switch (tipoPregunta) {
                case 0:
                    PreguntaNumeros preguntaNum = new PreguntaNumeros();
                    addPregunta(preguntaNum);
                    break;
                case 1:
                    PreguntaLetras preguntaLet = new PreguntaLetras();
                    addPregunta(preguntaLet);
                    break;
                case 2:
                    PreguntaIngles preguntaIng = new PreguntaIngles();
                    addPregunta(preguntaIng);
                    break;
            }
        }
    }

    
    /**
     * Muestra las puntuaciones actuales de los jugadores por pantalla siguiendo el orden de los turnos
     */
    public void mostrarPuntuaciones() {
        System.out.println("***************** PUNTUACIONES *****************");
        for(int i = 0; i < jugadores.size(); i++) {
            jugadores.get(turnos[i]).mostrarPuntos();
        }
    }

    
    /**
     * Muestra el ganador o ganadores de la partida por pantalla
     */
    public void mostrarGanador() {
        
        int puntuacionMax = 0;
        
        // Se comprueba cuál es la mayor puntuación de entre todos los jugadores
        for(int i = 0; i < jugadores.size(); i++){
            Jugador jugador = jugadores.get(i);
            if(jugador.getPuntosPartida() > puntuacionMax) {
                puntuacionMax = jugador.getPuntosPartida();
            }
        }
        
        // Se muestran por pantalla los nombres de los jugadores que tengan esa puntuación
        System.out.println("\nEl ganador o ganadores es...");
        for(int i = 0; i < jugadores.size(); i++) {     
            Jugador jugador = jugadores.get(turnos[i]);  
            if(jugador.getPuntosPartida() == puntuacionMax) {
                System.out.println("¡" + jugador.getNombre() + "!");
            }
        }
    }

    
    /**
     * Añadir un objeto Jugador al ArrayList de jugadores de la partida
     * @param nuevoJugador que es el jugador que se quiere añadir
     */
    public void addJugador(Jugador nuevoJugador) {
        jugadores.add(nuevoJugador);
    }

    
    /**
     * Devuelve los jugadores que participan en una partida
     * @return los jugadores de la partida
     */
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    
    /**
     * Elimina un objeto Jugador del ArrayList de jugadores de la partida según su índice
     * @param indice que es el índice del jugador que se quiere borrar
     */
    public void eliminarJugador(int indice) {
        if (indice < 0 || indice >= jugadores.size()) {
            System.out.println("Índice no válido");
        }
        else {
            jugadores.remove(indice);
        }
    }

    
    /**
     * Añadir un objeto Pregunta al ArrayList de preguntas de la partida
     * @param nuevaPregunta que es la pregunta que se quiere añadir
     */
    public void addPregunta(Pregunta nuevaPregunta) {
        preguntas.add(nuevaPregunta);
    }

    
    /**
     * Elimina un objeto Pregunta del ArrayList de preguntas de la partida según su índice
     * @param indice que es el índice de la pregunta que se quiere borrar
     */
    public void eliminarPregunta(int indice) {
        if (indice < 0 || indice >= preguntas.size()) {
            System.out.println("Índice no válido");
        }
        else {
            preguntas.remove(indice);
        }
    }
    
    
    /**
     * Comprueba si el último nombre introducido de un jugador es un nombre repetido en la partida o no
     * @param nombreJugador que es el nombre del nuevo jugador
     * @return true o false según si el jugador ya existe en la partida o no
     */
    private boolean jugadorRepetido(String nombreJugador) {
        
        boolean esRepetido = false;
        
        // Se comprueba el último nombre introducido con todos los nombres de los jugadores que se han añadido a la partida
        for(int i = 0; i < jugadores.size() && esRepetido == false; i++) {
            String nombre = jugadores.get(i).getNombre();
            if(nombre.equals(nombreJugador)) {
                esRepetido = true;
            }
        }
        
        return esRepetido;
    }
}
