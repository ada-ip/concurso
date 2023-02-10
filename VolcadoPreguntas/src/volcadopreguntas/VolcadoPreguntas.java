/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package volcadopreguntas;

import java.sql.Statement;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Este programa vuelca la información del archivo "ingles.txt" en la tabla pregunta de la base de datos concurso.
 * El archivo "ingles.txt" incluye varias preguntas con 4 posibles respuestas cada una almacenadas de la siguiente manera:
 * Cada pregunta ocupa 5 líneas:
 *   - La primera línea contiene la pregunta.
 *   - La segunda línea contiene la respuesta correcta.
 *   - Las otras 3 líneas contienen 3 opciones incorrectas.
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 12-05-2022
 */
public class VolcadoPreguntas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /* Variables auxiliares para ir pasando los datos guardados en el archivo ingles.txt a sus correspondientes campos de la
        tabla pregunta de la base de datos */
        String enunciado, respuestaCorrecta, respuestaIncorrecta1, respuestaIncorrecta2, respuestaIncorrecta3;
        
        try {
            // Se crea un objeto archivo con el archivo ingles.txt y se crea un objeto Scanner para leer cada línea del archivo
            File archivoIngles = new File("ingles.txt");
            Scanner lectorArchivo = new Scanner(archivoIngles);
            
            // Se instancia el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Se crea una conexión con la base de datos del concurso
            String url = "jdbc:mysql://localhost:3306/concurso";
            Connection con = DriverManager.getConnection(url, "root", "1234");
            
            // Se crea un objeto Statement para poder realizar la insercción de los datos que se lean del archivo
            Statement stm = con.createStatement();
            
            while(lectorArchivo.hasNext()) {
                enunciado = lectorArchivo.nextLine();
                respuestaCorrecta = lectorArchivo.nextLine();
                respuestaIncorrecta1 = lectorArchivo.nextLine();
                respuestaIncorrecta2 = lectorArchivo.nextLine();
                respuestaIncorrecta3 = lectorArchivo.nextLine();
                
                String queryInsert = "INSERT INTO pregunta(enunciado, respuesta_correcta, respuesta_incorrecta_1, "
                        + "respuesta_incorrecta_2, respuesta_incorrecta_3) VALUES('" + enunciado + "', '" + respuestaCorrecta + "', "
                        + "'" + respuestaIncorrecta1 + "', '" + respuestaIncorrecta2 + "', '" + respuestaIncorrecta3 +"')";
                
                stm.execute(queryInsert);
            }
            
            lectorArchivo.close();
            stm.close();
            con.close(); 
            
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
}
