DROP DATABASE IF EXISTS concurso;
CREATE DATABASE concurso;
USE concurso;

CREATE TABLE jugador(
	codigo INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(25) NOT NULL UNIQUE,
    puntuacion_total INT UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE partida(
	id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL
);

CREATE TABLE partida_jugador(
	idPartida INT UNSIGNED,
    codigoJugador INT UNSIGNED,
    puntuacion INT UNSIGNED NOT NULL,
    CONSTRAINT pk_partida_jugador PRIMARY KEY(idPartida, codigoJugador),
    CONSTRAINT fk_partida FOREIGN KEY(idPartida) REFERENCES partida(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_jugador FOREIGN KEY(codigoJugador) REFERENCES jugador(codigo) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE pregunta(
	idPregunta INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    enunciado VARCHAR(300) NOT NULL,
    respuesta_correcta VARCHAR(200) NOT NULL,
    respuesta_incorrecta_1 VARCHAR(200) NOT NULL,
    respuesta_incorrecta_2 VARCHAR(200) NOT NULL,
    respuesta_incorrecta_3 VARCHAR(200) NOT NULL
);