
package practicaconcurso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * Esta clase permite crear objetos para gestionar las tablas partida y partida_jugador de la base de datos del Concurso de Números y Letras.
 * La clase tiene métodos para añadir una nueva partida a la base de datos y mostrar el histórico de partidas.
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 18-05-2022
 */
public class GestionPartidas {

    /************** Atributos de la clase **************/

    private Connection con;
    private Statement stm;
    
    /************** Constructor de la clase **************/
    /**
     * Crea un objeto GestionPartidas con una conexión abierta con la base de datos del concurso
     */
    public GestionPartidas() {
        try {
            // Se instancia el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
      
            // Se crea una conexión con la base de datos del concurso
            String url = "jdbc:mysql://localhost:3306/concurso";
            this.con = DriverManager.getConnection(url, "root", "1234");
            
            // Se crea un objeto Statement para poder hacer queries sobre la base de datos
            this.stm = con.createStatement();
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    /************** Métodos de la clase **************/
    
    /**
     * Registra una partida recién terminada en la tabla partida y en la tabla partida_jugador de la base de datos
     * @param partida que es la partida que se quiere registrar en la base de datos
     */
    public void addPartida(Partida partida) {
        
        try {
            // Se crea un nuevo registro en la tabla partida con un id dado por la base de datos y la fecha y tiempo actual
            String insertPartida = "INSERT INTO partida(fecha) VALUES(NOW())";
            stm.execute(insertPartida);
            
            
            /* Se crea un nuevo registro en la tabla partida_jugador por cada jugador de la partida incluyendo 
            su puntuación final */
            
            // Se obtiene el código que la base de datos le ha dado a la partida
            String queryIdPartida = "SELECT id FROM partida ORDER BY fecha DESC LIMIT 1";
            ResultSet rsIdPartida = stm.executeQuery(queryIdPartida);
            
            /* Se realiza el registro de los jugadores en la tabla partida_jugador siempre y cuando la consulta del 
            código de la partida haya devuelto un código */
            if(rsIdPartida.next()) {
                
                // Se guarda el código de la partida
                int codigoPartida = rsIdPartida.getInt("id");
                
                /* Por cada jugador de la partida se obtiene su código en la base de datos y se realiza una inserción en la
                tabla partida_jugador con sus puntos correspondientes. */
                ArrayList<Jugador> jugadores = partida.getJugadores();

                for (int i = 0; i < jugadores.size(); i++) {
                    
                    String nombreJugador = jugadores.get(i).getNombre();
                    int puntosJugador = jugadores.get(i).getPuntosPartida();
                    
                    // Se crea un objeto GestionJugadores para obtener el código de cada jugador y poder modificar su puntuación
                    GestionJugadores gestorPuntos = new GestionJugadores();
                    int codigoJugador = gestorPuntos.getCodigoJugador(nombreJugador);
                
                    // Se inserta cada jugador en la tabla partida_jugador
                    String insertJugador = "INSERT INTO partida_jugador VALUES(" + codigoPartida + ", " 
                                        + codigoJugador + ", " + puntosJugador +")";
                    stm.execute(insertJugador);
                        
                    /* Y se modifica su puntuación total en la tabla jugador sumándole los puntos que ha
                    obtenido durante la partida */
                    gestorPuntos.modificarPuntuacion(nombreJugador, puntosJugador);
                    gestorPuntos.cerrarConexion();
                     
                }
            } 
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Muestra por pantalla todas las partidas que están registradas en la base de datos en orden cronológico
     */
    public void mostrarHistoricoPartidas() {
        
        try {
            // Se obtienen los identificadores de todas las partidas registradas en la base de datos
            String queryPartidas = "SELECT id FROM partida";
            ResultSet rsPartidas = stm.executeQuery(queryPartidas);
            
            System.out.println("***************** HISTÓRICO DE PARTIDAS *****************\n");
            
            // Se recorre cada partida para mostrar los jugadores y sus puntuaciones de cada una
            int contador = 1;
            while(rsPartidas.next()) {
                
                // Se obtienen todos los jugadores que participaron en una partida en concreta
                int codigoPartida = rsPartidas.getInt("id");
                Statement stmJugadores = con.createStatement();
                String queryJugadores = "SELECT jugador.nombre AS nombre, pj.puntuacion AS puntuacion FROM jugador INNER JOIN "
                        + "partida_jugador AS pj ON jugador.codigo = pj.codigoJugador WHERE pj.idPartida = " + codigoPartida;
                ResultSet rsJugadores = stmJugadores.executeQuery(queryJugadores);
                
                // Se muestran el nombre y la puntuación en la partida de cada jugador
                System.out.print(contador + ":");
                while(rsJugadores.next()) {
                    System.out.print(" " + rsJugadores.getString("nombre") + " " + rsJugadores.getInt("puntuacion"));
                }
                System.out.println("");
                contador++;
                
                stmJugadores.close();
            }
            
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    /**
     * Cierra la conexión con la base de datos
     */
    public void cerrarConexion(){
        try {
            this.stm.close();
            this.con.close();
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
