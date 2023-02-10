/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicaconcurso;

import java.util.Scanner;


/**
 * Este programa permite jugar a una versión simplificada de un concurso de preguntas y
 * respuestas en el que varios jugadores responden preguntas por turnos, acumulan puntos y
 * al final gana el que más puntos tiene.
 * El programa también mantiene un registro de los jugadores, el histórico de partidas jugadas 
 * y un ranking de los mejores jugadores. 
 * Toda la interacción con los usuarios será mediante entrada y salida estándar (teclado y pantalla).
 * Al iniciar el programa se muestra un menú principal con varias opciones con las que cualquier
 * jugador puede iniciar una partida, acceder a cierta información almacenada por el sistema o acceder
 * a un menú de jugadores donde puede registrarse o borrarse del sistema.
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 23-05-2022
 */
public class ConcursoNumerosYLetras {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Se crea un objeto Scanner para leer lo que introduzca el usuario por teclado
        Scanner teclado = new Scanner(System.in);
        // Variable que controla qué opción del menú elige el usuario
        int eleccionUsuario = 0;
        
        // Menú principal del concurso
        do {
            // Se muestra el menú por pantalla
            System.out.println("***************** CONCURSO NÚMEROS Y LETRAS *****************");
            System.out.println("1. Jugar Partida.");
            System.out.println("2. Ranking.");
            System.out.println("3. Histórico.");
            System.out.println("4. Menú de Jugadores.");
            System.out.println("5. Salir.");
        
            // Se lee la opción elegida por el usuario
            if(!teclado.hasNextInt()) {
                System.out.println("\nOpción no válida. Elija un número del menú.\n");
                teclado.nextLine();
            }
            else {
                eleccionUsuario = teclado.nextInt();
                teclado.nextLine();
                
                switch (eleccionUsuario) {
                    // El usuario elige jugar a una partida
                    case 1:
                        System.out.println("");
                        // Se crea un objeto Partida y se juegan el número de rondas elegido por el usuario
                        Partida partida = new Partida();
                        for(int i = 0; i < partida.getNumRondas(); i++) {
                            partida.jugarRonda(i);
                            System.out.println("");
                            partida.mostrarPuntuaciones();
                        }
                        System.out.println("");
                        // Al acabar la partida se muestra el ganador
                        partida.mostrarGanador();
                        
                        /* Se crea un objeto GestionPartidas para almacenar todos los datos de la partida y sus
                        jugadores en la base de datos */
                        GestionPartidas gestorPartida = new GestionPartidas();
                        gestorPartida.addPartida(partida);
                        gestorPartida.cerrarConexion();
                        break;
                    
                    // El usuario elige ver el ranking de jugadores 
                    case 2:
                        // Se crea un objeto GestionJugadores para poder obtener el ranking de la base de datos
                        GestionJugadores gestorRanking = new GestionJugadores();
                        gestorRanking.mostrarRanking();
                        gestorRanking.cerrarConexion();
                        break;
                        
                    // El usuario elige ver el histórico de partidas
                    case 3: 
                        // Se crea un objeto GestionPartidas para poder obtener el histórico de la base de datos
                        GestionPartidas gestorHistorico = new GestionPartidas();
                        gestorHistorico.mostrarHistoricoPartidas();
                        gestorHistorico.cerrarConexion();
                        break;
                        
                    // El usuario elige entrar en el menú de jugadores
                    case 4:
                        menuJugador();
                        break;
                    
                    // El usuario elige salir de la partida 
                    case 5:
                        break;
                        
                    default:
                        System.out.println("Opción no válida. Elija un número del menú.");
                }
            }
        }
        while(eleccionUsuario != 5);
    }
    
    
    /**
     * Ejecuta el submenú de gestion de jugadores del concurso
     */
    public static void menuJugador() {
        // Variables para almacenar la opción que elija el usuario del menú y el nombre del jugador que quiera gestionar 
        int opcionElegida = 0;
        String nombreJugador;
        
        // Se crea un objeto Scanner para leer lo que el usuario introduzca por teclado
        Scanner teclado = new Scanner(System.in);
        
        // Se crea un objeto GestionJugadores para poder utilizar los métodos de esa clase relacionados con el menú del programa
        GestionJugadores gestorJugadores = new GestionJugadores();
        
        do {
            // Se muestra el menú por pantalla
            System.out.println("¿Qué quiere hacer? (Introducir el número de la opción)");
            System.out.println("1. Ver jugadores");
            System.out.println("2. Añadir jugador");
            System.out.println("3. Eliminar jugador");
            System.out.println("4. Salir");
            
            // Se realiza la opción correspondiente a la elección del usuario
            if(!teclado.hasNextInt()) {
                System.out.println("Opción no válida");
                teclado.nextLine();
            }
            else {
                opcionElegida = teclado.nextInt();
                teclado.nextLine();
                
                switch(opcionElegida) {
                    // Se muestra un listado de todos los jugadores registrados
                    case 1:
                        System.out.println("");
                        gestorJugadores.mostrarJugadores();
                        break;
                        
                    /* Se le pide al usuario el nombre del jugador y se registra dicho jugador en la base de datos concurso 
                    siempre y cuando no esté ya registrado */
                    case 2:
                        System.out.print("\nIntroduzca el nombre del jugador a registrar: ");
                        nombreJugador = teclado.nextLine();
                        gestorJugadores.registrarJugador(nombreJugador);
                        break;
                    
                    /* Se le pide al usuario el nombre del jugador y se elimina dicho jugador de la base de datos concurso 
                    siempre y cuando no esté ya registrado*/
                    case 3:
                        System.out.print("\nIntroduzca el nombre del jugador a borrar: ");
                        nombreJugador = teclado.nextLine();
                        gestorJugadores.eliminarJugador(nombreJugador);
                        break;
                        
                    // Opción para salir del programa
                    case 4:
                        break;
                    
                    default:
                        System.out.println("Opción no válida"); 
                }
            }
            
            System.out.println("");
        }
        // El programa sólo acaba cuando el usuario elige la opción 4
        while(opcionElegida != 4);
        
        gestorJugadores.cerrarConexion();
    }
}
