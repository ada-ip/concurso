# Concurso

Juego de preguntas y respuestas inspirado en el conocido concurso de televisión Saber y ganar.

## Descripción general

El programa permite jugar a una versión simplificada de un concurso de preguntas y
respuestas en el que varios jugadores responden preguntas por turnos, acumulan puntos y
al final gana el que más puntos tiene. El programa también mantiene un registro de los
jugadores, el histórico de partidas jugadas y un ranking de los mejores jugadores. Toda la
interacción con los usuarios es mediante entrada y salida estándar (teclado y pantalla).

Al iniciar el programa se muestra el siguiente menú principal:
1. Jugar Partida -> Permite jugar una nueva partida.
2. Ranking -> Muestra el ranking de los mejores jugadores.
3. Histórico -> Muestra el histórico de partidas.
4. Jugadores -> Permite acceder a un submenú de gestión de jugadores.
5. Salir -> Termina el programa.
3. JUGAR PARTIDA

Una partida se compone de jugadores y preguntas. Los jugadores pueden ser entre 1 y 6. El
juego puede tener un número variable de rondas: 3 (partida rápida), 5 (partida corta), 10
(partida normal) o 20 (partida larga). En cada ronda se le hace una pregunta a cada jugador y
se obtienen puntos por contestar correctamente. 

## Dinámica de la partida

Al principio de la partida los N jugadores se ordenan de forma aleatoria (1º, 2º, 3º, etc.) y su
marcador se pone a cero puntos. Luego se juegan las R rondas de la partida. En cada ronda
se le hace una pregunta distinta al azar a cada jugador y contestan. Es decir, se le pregunta al
jugador 1, contesta, se le pregunta al jugador 2, contesta, etc. No hay rebotes de preguntas
ni acertar permite contestar otra pregunta ni nada similar. 
Cada pregunta acertada da un punto y los fallos dan cero puntos (no restan). Cuando se falla
una pregunta debe mostrarse cual era la respuesta correcta. 

Cuando se han jugado todas las rondas la partida termina, se muestran las puntuaciones
finales y quién ha sido el ganador, se registra la partida en el histórico y se vuelve al menú
principal.

## Preguntas

Las preguntas de una partida pueden ser de tres tipos: Números, Letras e Inglés.

* Pregunta de Números: El jugador debe calcular el resultado de una expresión
matemática generada al azar utilizando de 4 a 8 enteros, cada uno con valor entre 2 y
12, combinando sumas, restas y multiplicaciones (no hay divisiones). 
* Pregunta de Letras: Dada una palabra elegida al azar del archivo diccionario.txt, se
muestra parcialmente dicha palabra (ocultando algunas letras) y el jugador deberá
intentar adivinarla escribiendo la palabra completa correctamente. La palabra tendrá
L letras, siendo obligatorio L > 3 (más de 3 letras), y se ocultarán L / 3 letras elegidas
al azar (una letra por cada 3 letras que tenga la palabra). 
* Pregunta de Inglés: Se muestra una pregunta aleatoria almacenada en la base de datos, mostrando 
las cuatro posibles respuestas en orden aleatorio (A, B, C o D) y el jugador elegirá cuál cree que es la correcta.
