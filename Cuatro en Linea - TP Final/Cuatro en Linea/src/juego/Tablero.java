package juego;


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Representaci�n gr�fica del Tablero del Juego Cuatro en L�nea.
 * 
 */
public class Tablero {

	private static final int ALTO_FILA = 80;
	private static final int ANCHO_COLUMNA = 80;
	private static final int ALTURA_BOTON = 40;
	private static final double RADIO = Math.min(ALTO_FILA - 1, ANCHO_COLUMNA - 1) / 2;
	
	
	private CuatroEnLinea juego;
	private GridPane grilla;
	private Stage escenario;
	
	/**
	 * post: asocia el Tablero a 'nuevoJuego' y lo inicializa a partir de su estado. 
	 * 
	 * @param nuevoJuego
	 */
	public Tablero(CuatroEnLinea nuevoJuego) {
		
		juego = nuevoJuego;
		escenario = new Stage();
		grilla = new GridPane();
	}
	
	/**
	 * post: muestra el Tablero en pantalla.
	 */
	public void mostrar() {
		
		dibujarBotones();
		
		double ancho = juego.contarColumnas() * ANCHO_COLUMNA;
		double alto = (juego.contarFilas() * ALTO_FILA) + ALTURA_BOTON;
		
		Scene escena = new Scene(grilla, ancho, alto);

		escenario.setScene(escena);
		escenario.getIcons().add(new Image("file:img/logo.png"));
		escenario.setResizable(false);
		escenario.setTitle("Cuatro en L�nea - Jugando");
		
		dibujar();
		
		escenario.show();
	}
	
	/**
	 * post: agrega los botones para soltar una ficha en cada columna del Tablero.
	 */
	private void dibujarBotones() {
		
		for (int columna = 1; columna <= juego.contarColumnas(); columna++) {

			Button botonSoltarFicha = new Button("Soltar");
			botonSoltarFicha.setMinHeight(ALTURA_BOTON);

			botonSoltarFicha.setOnAction(new SoltarFicha(this, juego, columna));
			botonSoltarFicha.setMinWidth(ANCHO_COLUMNA);
			botonSoltarFicha.setStyle("fx-font: italic 10pt Arial; -fx-background-radius: 9999; -fx-text-fill: #b4c5d1;"
			+"-fx-background-color:#27396e");
			grilla.add(botonSoltarFicha, columna - 1, 0);
		}
		grilla.setStyle("-fx-background-color:#32498c");
	}
	
	/**
	 * post: actualiza el Tablero a partir del estado del juego asociado.
	 */
	public void dibujar() {

		for (int fila = 1; fila <= juego.contarFilas(); fila++) {

			for (int columna = 1; columna <= juego.contarColumnas(); columna++) {

				Casillero casillero = juego.obtenerCasillero(fila, columna);
				
				Circle dibujoCasillero = dibujarCasillero(casillero);
				
				grilla.add(dibujoCasillero, columna - 1, fila);
			}
		}
		
		
	}

	/**
	 * post: dibuja y devuelve el casillero dado.
	 * 
	 * @param casillero
	 * @return representaci�n gr�fica del Casillero.
	 */
	private Circle dibujarCasillero(Casillero casillero) {
		
		Circle dibujoCasillero = new Circle(RADIO, obtenerPintura(casillero));
		
		dibujoCasillero.setStroke(new Color(0.5, 0.5, 0.5, 1.0));
		dibujoCasillero.setScaleX(0.95);
		dibujoCasillero.setScaleY(0.95);
		return dibujoCasillero;
	}

	/**
	 * post: determina la pintura a utilizar para 'casillero'.

	 * @param casillero
	 * @return pintura a utilizar para identificar el Casillero.
	 */
	private Paint obtenerPintura(Casillero casillero) {

		Paint pintura;

		switch (casillero) {
		
			case AMARILLO:
				pintura = Color.YELLOW;
				break;
				
			case ROJO:
				pintura = Color.RED;
				break;
				
			default:
				pintura = Color.CORNFLOWERBLUE;
		}

		return pintura;
	}

	/**
	 * pre : el juego asociado termin�.
	 * post: muestra un mensaje indicando el resultado del juego.
	 */
	public void mostrarResultado() {

		Stage dialogo = new Stage();
		
		BorderPane panelGanador = new BorderPane();
		panelGanador.setPadding(new Insets(10.0));
		panelGanador.setStyle("-fx-font: bold italic 15pt Arial; -fx-background-radius: 0;"
		+"-fx-background-color: #5b82b3");
		dialogo.getIcons().add(new Image("file:img/logo.png"));
		Text textoResultado;
		Font fuente = new Font(40.0);
		dialogo.setTitle("Ganador: Jugador " + juego.obtenerGanador());
		
		if (juego.hayGanador()) {
		
			textoResultado = new Text("Gan� el jugador " + juego.obtenerGanador());
			textoResultado.setStyle("-fx-font: bold 15pt Arial; -fx-background-radius: 0;");
			textoResultado.setFill(Color.ALICEBLUE);			
			
		} else {
			
			textoResultado = new Text("Empate");
			textoResultado.setStyle("-fx-font: bold 15pt Arial; -fx-background-radius: 0;");
			textoResultado.setFill(Color.ALICEBLUE);	
		}
		
		textoResultado.setFont(fuente);
		panelGanador.setCenter(textoResultado);
		
		Scene escenaGanador = new Scene(panelGanador);
		
		dialogo.setScene(escenaGanador);
		dialogo.initOwner(escenario);
		dialogo.initModality(Modality.WINDOW_MODAL);
		dialogo.setResizable(false);
		
		dialogo.showAndWait();
	}

}
