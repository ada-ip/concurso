
package practicaconcurso;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Esta clase representa una pregunta matemática del juego Concurso de Números y Letras generada de forma aleatoria.
 * El enunciado de la pregunta será una expresión matemática aleatoria con de 4 a 8 números enteros con sumas, restas y multiplicaciones.
 * Cada número entero de la expresión matemática también se genera aleatoriamente y tiene que tener un valor entre 2 y 12, ambos incluidos.
 * 
 * @author Ada Iglesias Pasamontes
 * 
 * date: 16-05-2022
 */
public class PreguntaNumeros extends Pregunta {

    /************** Atributos de la clase **************/
    
    private String expresionMatematica;          // Expresión matemática con de 4 a 8 números enteros generada aleatoriamente

    
    /************** Constructor de la clase **************/
    /**
     * Crea un objeto PreguntaNumeros con una expresión matemática generada aleatoriamente, una respuesta correcta
     * que será el resultado de dicha expresión matemática, y un enunciado que incluirá dicha expresión matemática.
     */
    public PreguntaNumeros() {
        this.expresionMatematica = generarExpresionMatematica();
        this.respuestaCorrecta = Integer.toString(evaluarExpresion(this.expresionMatematica));
        this.enunciado = "¿Cuál es el resultado de esta expresión matemática?: " + this.expresionMatematica;
    }

    
    /************** Métodos de la clase **************/

    /**
     * Genera una expresión matemática aleatoria con de 4 a 8 números enteros y con sumas, restas y multiplicaciones.
     * Cada número entero de la expresión matemática también se genera aleatoriamente y tiene que tener un valor entre 2 y 12, ambos incluidos.
     * 
     * @return un String con la expresión matemática aleatoria generada
     */
    private String generarExpresionMatematica() {
        // String que almacena la expresión matemática aleatoria
        String expresion = "";
        
        // Variable que almacena el número de operandos que va a tener la expresión que tiene que ser entre 4 y 8
        int numEnteros = 4 + (int)(Math.random()*(8 + 1 - 4));
        
        // Variable que almacena el número de operadores que va a tener la expresión
        int numOperadores = numEnteros - 1;
        
        /* Variable auxiliar para almacenar el valor aleatorio que se va a ir añadiendo uno a uno al String expresion
        hasta completar la expresión matemática */
        int valor;
        
        
        for(int i = 0; i < numEnteros + numOperadores; i++) {
            
            /* Los caracteres que estén en una posición par del String (teniendo en cuenta que el primer caracter es el 
            caracter 0) tienen que ser un número entero, es decir, un operando. Y los caracteres que estén en una posición
            impar del String tienen que ser los operadores (o + o - o *) */
            if(i % 2 == 0) {
               
                // Los operandos tienen que tener un valor entre 2 y 12 
                valor = 2 + (int)(Math.random()*(12 + 1 - 2));
               
                // Se añade el nuevo número entero al String expresion;
                expresion += Integer.toString(valor);
            }
            else {
               
                /* Los operadores se codifican para poder aleatorizarlos. La suma es un -1, la resta un -2 y la 
                multiplicación un -3 */
                valor = -1 * (1 + (int)(Math.random()*(3 + 1 - 1)));
               
                // Se añade o un + o un - o un * al String expresion
                switch (valor) {
                    case -1:
                        expresion += "+";
                        break;
                    case -2:
                        expresion += "-";
                        break;
                    case -3:
                        expresion += "*";
                        break;
                }
            }
        }
       
        return expresion;
    }

    /**
     * Evalúa una expresión matemática en forma de String y devuelve el resultado de dicha expresión
     * 
     * @param expresion que la expresión matemática en tipo String
     * @return el resultado de la expresión matemática
     */
    private int evaluarExpresion(String expresion) {
        int valor = 0;
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("javascript");
            Object result = engine.eval(expresion);
            valor = Integer.decode(result.toString());
            
        } catch (Exception e) {
            e.getMessage();
        }
        return valor;
    }
}
