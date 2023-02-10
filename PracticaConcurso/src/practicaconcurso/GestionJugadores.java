
package practicaconcurso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Esta clase permite crear objetos para gestionar la tabla jugador de la base de datos del Concurso de Números y Letras.
 * La clase tiene métodos para mostrar todos los jugadores registrados, mostrar el ranking de jugadores, añadir un
 * jugador al sistema, eliminar un jugador del sistema, comprobar si un jugador está registrado en el sistema,
 * modificar la puntuación total de un jugador y obtener el código de un jugador registrado.
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 18-05-2022
 */
public class GestionJugadores {
    
    /************** Atributos de la clase **************/
    
    private Connection con;
    private Statement stm;
    
    
    /************** Constructor de la clase **************/
    /**
     * Crea un objeto GestionJugadores con una conexión abierta con la base de datos del concurso
     */
    public GestionJugadores() {
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
     * Muestra la lista de jugadores registrados en la base de datos del juego Concurso Números y Letras
     */
    public void mostrarJugadores() {
        try {   
            /* Se realiza una consulta sobre la tabla jugador de la base de datos seleccionando todos los jugadores */
            String queryNombresJugadores = "SELECT nombre FROM jugador";
            ResultSet rsNombresJugadores = stm.executeQuery(queryNombresJugadores);
            
            // Se muestran los nombres de todos los jugadores de la base de datos por pantalla
            System.out.println("***************** JUGADORES REGISTRADOS *****************\n");
            int contador = 1;
            while(rsNombresJugadores.next()) {
                System.out.println(contador + "- " + rsNombresJugadores.getString("nombre"));
                contador++;
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Muestra la lista de jugadores registrados en la base de datos del juego Concurso Números y Letras
     * ordenados de mayor puntuación a menor puntuación
     */
    public void mostrarRanking() {
        try {   
            /* Se realiza una consulta sobre la tabla jugador de la base de datos seleccionando todos los jugadores
            y ordenándolos por puntuación de forma descendente */
            String queryJugadores = "SELECT nombre, puntuacion_total FROM jugador ORDER BY puntuacion_total DESC";
            ResultSet rsJugadores = stm.executeQuery(queryJugadores);
            
            // Se muestran los nombres y puntos de todos los jugadores por pantalla
            System.out.println("***************** RANKING DE JUGADORES *****************\n");
            int contador = 1;
            while(rsJugadores.next()) {
                System.out.println(contador + "- " + rsJugadores.getString("nombre") + ": " + rsJugadores.getInt("puntuacion_total"));
                contador++;
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Añade un jugador nuevo a la tabla jugador de la base de datos del concurso siempre y cuando el jugador 
     * no esté ya registrado y el nombre de jugador proporcionado no contenga espacios ni tenga más de 25 caracteres
     * @param nombreJugador que es el nombre del jugador nuevo
     * @return true si el jugador se registra correctamente en la base de datos y false si ocurre algún error
     */
    public boolean registrarJugador(String nombreJugador) {
        
        boolean jugadorRegistrado = false;
        
        // El jugador no puede tener el mismo nombre que un jugador ya registrado en la base de datos ni puede contener espacios
        if(comprobarJugador(nombreJugador)) { 
            System.out.println("Error: el jugador ya está registrado");       
        }
        else if(nombreJugador.contains(" ") || nombreJugador.length() > 25 || nombreJugador.isEmpty()) {
            System.out.println("Error: el nombre de jugador no es válido. No puede tener espacios ni más de 25 caracteres");
        }
        else {
            try {
                // Se realiza la insercción de los datos (sólo hace falta el nombre del jugador) en la tabla jugador
                String sqlInsertJugador = "INSERT INTO jugador(nombre) VALUES('" + nombreJugador + "')";
                stm.execute(sqlInsertJugador);
                
                System.out.println("¡Nuevo jugador registrado!");
                
                jugadorRegistrado = true;

            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return jugadorRegistrado;
    }

    /**
     * Permite eliminar un jugador registrado en la base de datos a partir del nombre del jugador
     * @param nombreJugador que es el nombre del jugador que se quiere eliminar
     */
    public void eliminarJugador(String nombreJugador) {
        // Si el jugador no está registrado en la base de datos se muestra un mensaje de error, y si lo está se elimina de la base de datos
        if(!comprobarJugador(nombreJugador)) {
            System.out.println("Error: el jugador que se quiere eliminar no está registrado");
        }
        else {
            try {
                // Se obtiene el código del jugador para no tener problemas al realizar el borrado del registro
                int codigoJugador = getCodigoJugador(nombreJugador);
                
                // Se realiza el borrado del registro del jugador de la base de datos con su código de jugador
                String sqlDeleteJugador = "DELETE FROM jugador WHERE codigo = " + codigoJugador;
                stm.execute(sqlDeleteJugador);
                
                System.out.println("El jugador " + nombreJugador + " se ha eliminado correctamente");

            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Comprueba si un jugador está registrado en la base de datos concurso.
     * Comprueba si el nombre del jugador ya existe en el sistema
     * @param nombreJugador que es el nombre del jugador que puede o no estar registrado
     * @return true si el jugador está registrado y false si no lo está
     */
    public boolean comprobarJugador(String nombreJugador) {
        
        boolean jugadorRegistrado = false;
        
        try {
            /* Se realiza una consulta buscando por el nombre del jugador dado y si la consulta devuelve algún
            dato el jugador estará registrado en la base de datos, si no está registrado el ResultSet se quedará vacío */
            String queryJugador = "SELECT codigo FROM jugador WHERE nombre = '" + nombreJugador +"'";
            ResultSet rs = stm.executeQuery(queryJugador);
            if(rs.next()){
                jugadorRegistrado = true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return jugadorRegistrado;
    }

    
    /**
     * Modifica la puntuación total histórica de un jugador registrado en la base de datos
     * @param nombreJugador que es el nombre del jugador registrado
     * @param puntos que son los puntos que se le quieren añadir al jugador
     */
    public void modificarPuntuacion(String nombreJugador, int puntos) {
        
        // Si el jugador no está registrado en la base de datos se muestra un mensaje de error, y si lo está se modifica su puntuación
        if(!comprobarJugador(nombreJugador)) {
            System.out.println("Error: el jugador no está registrado");
        }
        else {
            try {
                // Se realiza la actualización de la puntuación total del jugador de la tabla jugador
                String sqlUpdatePuntuacion = "UPDATE jugador SET puntuacion_total = puntuacion_total + " + puntos 
                        + " WHERE nombre = '" + nombreJugador + "'";
                stm.execute(sqlUpdatePuntuacion);

            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    
    /**
     * Permite obtener el código asignado a un jugador registrado en la base de datos del concurso
     * @param nombreJugador que es el nombre del jugador registrado
     * @return el codigo del jugador en la base de datos
     */
    public int getCodigoJugador(String nombreJugador) {
        
        int codigoJugador = 0;
        
        /* Si el jugador no está registrado en la base de datos se muestra un mensaje de error, y si lo está 
        se obtiene su código de jugador */
        if(!comprobarJugador(nombreJugador)) {
            System.out.println("Error: el jugador no está registrado");
        }
        else {
            try {
                // Se obtiene el código del jugador a partir de su nombre
                String queryCodigoJugador = "SELECT codigo FROM jugador WHERE nombre = '" + nombreJugador + "'";
                ResultSet rsCodigo = stm.executeQuery(queryCodigoJugador);
                
                // Se guarda el codigo en la variable auxiliar codigoJugador
                rsCodigo.next();
                codigoJugador = rsCodigo.getInt("codigo");

            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        return codigoJugador;
    }
    
    /**
     * Cierra la conexión con la base de datos
     */
    public void cerrarConexion() {
        try {
            this.stm.close();
            this.con.close();
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
