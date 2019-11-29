package juego;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


/**
 * Juego Cuatro en Línea
 * 
 * Reglas: - Juego para 2 jugadores obligatoriamente.
 * 		   - Cada jugador utilizara fichas de un color(rojo o amarillo) que le quedara para toda la partida.
 *         - Una vez que un jugador tire, le tocara al otro y asi sucesivamente.
 *         - Ganara el que alinee cuatro fichas del mismo color horizontal, vertical, o diagonalmente.
 *         - Si el tablero se llena quedara en empate.
 */
public class CuatroEnLinea {
	
	private Casillero[][] tablero;
	private String nombreRojo;
	private String nombreAmarillo;
	private Casillero jugadorActual;
	private int ultimaFila;
	private int ultimaColumna;
	private Casillero casilleroAComparar;

	/**
	 * pre : 'filas' y 'columnas' son mayores o iguales a 4.
	 * post: empieza el juego entre el jugador que tiene fichas rojas, identificado como 
	 * 		 'jugadorRojo' y el 
	 * jugador que tiene fichas amarillas, identificado como
	 * 		 'jugadorAmarillo'. 
	 * 		 Todo el tablero está vacío.
	 * 
	 * @param filas : cantidad de filas que tiene el tablero.
	 * @param columnas : cantidad de columnas que tiene el tablero.
	 * @param jugadorRojo : nombre del jugador con fichas rojas.
	 * @param jugadorAmarillo : nombre del jugador con fichas amarillas.
	 */
	public CuatroEnLinea(int filas, int columnas, String jugadorRojo, String jugadorAmarillo) {
		if(filas >= 4) {
			if(columnas >= 4) {
				tablero = new Casillero[filas][columnas];
				for(int fila = 0; fila < tablero.length; fila++) {
					for(int columna = 0; columna < tablero[fila].length; columna++) {
						tablero[fila][columna] = Casillero.VACIO;
					}
				}
				asignarPrimerJugador();
				nombreRojo = jugadorRojo;
				nombreAmarillo = jugadorAmarillo;
			} else {
				throw new Error("La cantidad de columnas a crear debe ser mayor o igual a 4.");
			}
		} else {
			throw new Error("La cantidad de filas a crear debe ser mayor o igual a 4.");
		}
	}
	
	/**
	 * post: asigna aleatoriamente cual sera el jugador que tire la primer ficha de la partida, donde 1 es el jugador que usara las fichas rojas
	 *       y 2 sera el jugador que usara las fichas amarillas.		 
	 */
	private void asignarPrimerJugador() {
		int numeroGanador = (int)(Math.random() * 2) + 1;
		jugadorActual = (numeroGanador == 1)? Casillero.ROJO : Casillero.AMARILLO;
	}
	
	/**
	 * post: cambia el jugador actual, que es el que establece el color de la ficha en cada turno.
	 *  	 Si es rojo el ultimo jugador, pasa a ser amarillo, y viceversa. 		 
	 */
	private void cambioDeJugador() {
		jugadorActual = (jugadorActual == Casillero.ROJO)? Casillero.AMARILLO : Casillero.ROJO;
	}
	
	/**
	 * post: devuelve la ficha que va a ser tirada en el siguiente turno.		 
	 */
	public Casillero obtenerJugadorActual() {
		return jugadorActual;
	}
	
	/**
	 * post: devuelve la cantidad máxima de fichas que se pueden apilar en el tablero.
	 */
	public int contarFilas() {
		return tablero.length;
	}

	/**
	 * post: devuelve la cantidad máxima de fichas que se pueden alinear en el tablero.
	 */
	public int contarColumnas() {
		return tablero[0].length;
	}

	/**
	 * pre : fila está en el intervalo [1, contarFilas()],
	 * 		 columnas está en el intervalo [1, contarColumnas()].
	 * post: indica qué ocupa el casillero en la posición dada por fila y columna.
	 * 
	 * @param fila
	 * @param columna
	 */
	public Casillero obtenerCasillero(int fila, int columna) {
		if((fila >= 1) && (fila <= contarFilas())){ 
			if((columna >= 1) && (columna <= contarColumnas())) {
				return tablero[fila - 1][columna - 1];
			} else {
				throw new Error("La columna solicitada no pertenece al intervalo [1, contarFilas()]");
			}
		} else {
			throw new Error("La fila solicitada no pertenece al intervalo [1, contarColumnas()]");
		}
	}
	
	/**
	 * pre : el juego no terminó, columna está en el intervalo [1, contarColumnas()]
	 * 		 y aún queda un Casillero.VACIO en la columna indicada. 
	 * post: deja caer una ficha en la columna indicada, almacena el color del ultimo turno para comprobar si este gano
	 * 	     y cambia el color del siguiente jugador.
	 * 
	 * @param columna
	 */
	public void soltarFicha(int columna) {
		boolean continuaJugada = true;
		if(!termino() && (columna >= 1) && (columna <= contarColumnas())) {
			for(int fila = tablero.length - 1; (fila >= 0) && continuaJugada; fila--) {
				continuaJugada = tablero[fila][columna - 1] != Casillero.VACIO;
				if(!continuaJugada) {
					tablero[fila][columna - 1] = jugadorActual;
					continuaJugada = false;
					ultimaFila = fila;
					ultimaColumna = columna - 1;
					casilleroAComparar = jugadorActual;
					cambioDeJugador();
					String ruta = "img/Moneda 1.wav";
					Media media = new Media(new File(ruta).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(media);
					mediaPlayer.setAutoPlay(true);
				}
			}
		}
	}
	
	/**
	 * post: indica si el juego terminó porque uno de los jugadores
	 * 		 ganó o no existen casilleros vacíos.
	 */
	public boolean termino() {
		boolean finDeLaPartida = comprobarSiCasillerosEstanLlenos() || hayGanador();
		return finDeLaPartida;
	}

	/**
	 * post: indica si todos los casilleros se encuentran llenos.
	 *  	 	 
	 */
	private boolean comprobarSiCasillerosEstanLlenos() {
		boolean casillerosLlenos = true;
		for(int fila = 0; (fila < tablero.length) && casillerosLlenos; fila++) {
			for(int columna = 0; (columna < tablero[fila].length) && casillerosLlenos; columna++) {
				casillerosLlenos = tablero[fila][columna] != Casillero.VACIO;
			}
		}
		return casillerosLlenos;
	}
	
	/**
	 * post: indica si el juego terminó y tiene un ganador, comprobando las cuatro formas posibles de victoria.
	 */
	public boolean hayGanador() {
		boolean hayGanador = victoriaVertical() || victoriaHorizontal() || victoriaDiagonalIzquierda() || victoriaDiagonalDerecha();
		return hayGanador;
	}
	
	/**
	 * pre : el juego terminó.
	 * post: devuelve el nombre del jugador que ganó el juego.
	 */
	public String obtenerGanador() {
		if(termino()) {
			if(casilleroAComparar == Casillero.ROJO) {
				return nombreRojo;
			}else {
				return nombreAmarillo;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * post: comprueba si el ultimo jugador gano alineando cuatro fichas verticalmente.		 
	 */
	private boolean victoriaVertical() {
		boolean victoriaVertical = true;
		if(ultimaFila < (tablero.length - 3)) {
			for(int i = 0; (i < 4) && victoriaVertical; i++) {
				victoriaVertical = obtenerCasillero((ultimaFila + 1) + i, ultimaColumna + 1) == casilleroAComparar;
			}
		} else {
			victoriaVertical = false;
		}
		return victoriaVertical;
	}
	
	/**
	 * post: comprueba si el ultimo jugador gano alineando cuatro fichas horizontalmente, de cuatro formas posibles, e ingresando
	 *       las limitaciones y las diferencias a comprobar mediante parametros. 	 
	 */
	private boolean victoriaHorizontal() {
		boolean victoriaHorizontal = comprobarVictoriaHorizontal(3, -1, 0) || comprobarVictoriaHorizontal(2, 0, 1) || comprobarVictoriaHorizontal(1, 1, 2) || comprobarVictoriaHorizontal(0, 2, 3); 
		return victoriaHorizontal;
	}
	
	/**
	 * post: comprueba si el ultimo jugador gano alineando cuatro fichas diagonalmente a la izquierda, de cuatro formas posibles, , e ingresando
	 *       las limitaciones y las diferencias a comprobar mediante parametros. 	 
	 */
	private boolean victoriaDiagonalIzquierda() {
		boolean victoriaDiagonal = comprobarVictoriaDiagonalIzquierda(3, -1, 0, 2, 0) || comprobarVictoriaDiagonalIzquierda(2, 0, 1, 1, 1) || 
								   comprobarVictoriaDiagonalIzquierda(1, 1, 2, 0, 2)  || comprobarVictoriaDiagonalIzquierda(0, 2, 3, -1, 3);
		return victoriaDiagonal;
	}
	
	/**
	 * post: comprueba si el ultimo jugador gano alineando cuatro fichas diagonalmente a la derecha, de cuatro formas posibles, e ingresando
	 *       las limitaciones y las diferencias a comprobar mediante parametros. 
	 */
	private boolean victoriaDiagonalDerecha() {
		boolean victoriaDiagonal = comprobarVictoriaDiagonalDerecha(3, 0, 3, 0, 0) || comprobarVictoriaDiagonalDerecha(2, 0, 2, 0, 1) || 
				                   comprobarVictoriaDiagonalDerecha(1, 1, 1, 1, 2) || comprobarVictoriaDiagonalDerecha(0, 2, 0, 2, 3);
		return victoriaDiagonal;
	}
	
	/**
	 * post: mediante los parametros ingresados, limita la cantidad de columnas y la diferencia entre ficha y ficha.	
	 *       Devuelve verdadero si el color de la ultima ficha y el de los parametros ingresados forman cuatro fichas. En caso contrario devuelve falso.	  
	 */
	private boolean comprobarVictoriaHorizontal(int restriccionColumnaDerecha, int restriccionColumnaIzquierda, int diferencia){
		boolean victoria = true;
		if((ultimaColumna < tablero[0].length - restriccionColumnaDerecha) && (ultimaColumna > restriccionColumnaIzquierda)) {
			for(int i = 0; (i < 4) && victoria; i++) {
				victoria = obtenerCasillero((ultimaFila + 1), (ultimaColumna + 1) - diferencia + i) == casilleroAComparar;
			}
		}else {
			victoria = false;
		}	
		return victoria;
	}
	
	/**
	 * post: mediante los parametros ingresados, limita la cantidad de columnas, la cantidad de filas y la diferencia entre ficha y ficha.	
	 *       Devuelve verdadero si el color de la ultima ficha y el de los parametros ingresados forman cuatro fichas. En caso contrario devuelve falso.	 	 
	 */
	private boolean comprobarVictoriaDiagonalIzquierda(int restriccionFilaDerecha, int restriccionFilaIzquierda, int restriccionColumnaDerecha, int restriccionColumnaIzquierda, int diferencia){
		boolean victoria = true;
		if((ultimaFila < (tablero.length - restriccionFilaDerecha)) && (ultimaFila > restriccionFilaIzquierda) && (ultimaColumna < tablero[0].length - restriccionColumnaDerecha) && (ultimaColumna > restriccionColumnaIzquierda)) {
			for(int i = 0; (i < 4) && victoria; i++) {
				victoria = obtenerCasillero((ultimaFila + 1) - diferencia + i, (ultimaColumna + 1) + diferencia - i) == casilleroAComparar;
			}
		}else {
			victoria = false;
		}	
		return victoria;
	}
	
	/**
	 * post: mediante los parametros ingresados, limita la cantidad de columnas, la cantidad de filas y la diferencia entre ficha y ficha.
	 * 	     Devuelve verdadero si el color de la ultima ficha y el de los parametros ingresados forman cuatro fichas. En caso contrario devuelve falso.	 
	 */
	private boolean comprobarVictoriaDiagonalDerecha(int restriccionFilaDerecha, int restriccionFilaIzquierda, int restriccionColumnaDerecha, int restriccionColumnaIzquierda, int diferencia){
		boolean victoria = true;
		if((ultimaFila < (tablero.length - restriccionFilaDerecha)) && (ultimaFila > restriccionFilaIzquierda) && (ultimaColumna < tablero[0].length - restriccionColumnaDerecha) && (ultimaColumna > restriccionColumnaIzquierda)) {
			for(int i = 0; (i < 4) && victoria; i++) {
				victoria = obtenerCasillero((ultimaFila + 1) - diferencia + i, (ultimaColumna + 1) - diferencia + i) == casilleroAComparar;
			}
		}else {
			victoria = false;
		}	
		return victoria;
	}
	


}