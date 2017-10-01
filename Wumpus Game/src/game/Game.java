package game;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import utils.Utilidades;

public class Game {
	//Opcion elegida
	private int numOpcion;
	
	//Parametros leidos desde fichero de propiedades
	private int celdas;	// Numero n para definir el tablero
	private int pozos;	// Numero de pozos
	private int flechas;// Numero de flechas
	
	//Posicion cazador
	private int posxCaz; //Posicion x Cazador
	private int posyCaz; //Posicion y Cazador
	
	private Celda[][] escenario; // fila columna y numero de estados
	
	
	public void menuPrincipal() {
		//Punto de entrada al juego
		System.out.println("Elige una opción:");
		System.out.println("1) Comenzar partida");
		System.out.println("2) Salir");

		
		while(!esNumeroValido(Utilidades.recuperarValorNumerico(), "MenuPrincipal")){
			System.out.println("Opción no válida");
		}
		
		// Comienza el juego
		if(numOpcion == 1){
			//Precargamos datos y generamos mapa aleatorio
			precargaDatos();
			generarMapa();
			
			//Bucle del juego
			while(escenario[posxCaz][posyCaz].getCazador().isVivo()){
				escenario[posxCaz][posyCaz].estadoCelda();			
				opcionesCazador();
				while(!esNumeroValido(Utilidades.recuperarValorNumerico(), "InGame")){
					System.out.println("Opción no válida");
				}
				
				if(numOpcion == 1){
					avanzarCazador(escenario[posxCaz][posyCaz].getCazador().getDireccion());
					calcularColisiones();
				}
				else if(numOpcion == 2){
					girarCazadorIzq(escenario[posxCaz][posyCaz].getCazador());
				}
				else if(numOpcion == 3){
					girarCazadorDer(escenario[posxCaz][posyCaz].getCazador());
				}
				else if(numOpcion == 4){
					lanzarFlechaCazador();
				}
				else if(numOpcion == 5){
					System.out.println("[INFO] Has escapado con el lingote!");
					System.out.println("Pulsa una tecla para continuar..");
					try {
						System.in.read();
					} catch (IOException e) {
						e.printStackTrace();
					}
					menuPrincipal();
				}								
			}
			
			 try {
				 System.out.println("Pulsa una tecla para continuar..");
				 System.in.read();
				 menuPrincipal();

			} catch (IOException e) {
				e.printStackTrace();
			}
						
		}
		else{
			System.out.println("Cerrando el juego");
			System.exit(0);
		}
	}
	
	private boolean esNumeroValido(int num, String menu){
		switch (menu) {
		case "MenuPrincipal":
			if(num >= 1 && num <= 2){
				numOpcion = num;
				return true;
			}
			else{				
				return false;
			}	
		case "InGame":
			if(num >= 1 && num <= 5){
				numOpcion = num;
				return true;
			}
			else{
				return false;
			}

		default:
			return false;
		}

	}

	private void precargaDatos(){
		try {
			System.out.println("Precargando datos..");
			
			Properties prop = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream resourceStream = loader.getResourceAsStream("parametros.properties");
			
			prop.load(resourceStream);
			
			celdas = Integer.parseInt(prop.getProperty("celdas"));
			pozos = Integer.parseInt(prop.getProperty("pozos"));
			flechas = Integer.parseInt(prop.getProperty("flechas"));						
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void generarMapa(){
		escenario = new Celda [celdas][celdas];		
		for(int x=0; x<celdas; x++)
		    for(int y=0; y<celdas; y++)
		        escenario[x][y] = new Celda();
		
		//Calculo Wumpus
		calculoWumpus();
		//Calculo Pozos - Vigilar colision con Wumpus
		for(int i = 0; i < pozos; i++){
			calculoPozos();		
		}
		//Calculo Lingote - Vigilar colision con Wumpus y Pozos
		calculoLingote();
		//Calculo Cazador - Vigilar colision con Wumpus, Pozos y Lingote
		calculoCazador(); 
		//Calculo Salida - Vigilar colision con Pozos
		calculoSalida();		
		
	}
	
	private int celdaAleatoria(){
		Random r = new Random();
		int low = 0;
		int high = celdas;
		int result = r.nextInt(high - low) + low;
		
		return result;
	}
	
	private void calculoWumpus(){
		int x = celdaAleatoria();
		int y = celdaAleatoria();	
		escenario[x][y].setWumpus(true);
		System.out.println("Wumpus está en "+"["+x+"],"+"["+y+"]");
		if (Utilidades.dentroLimites(x-1, y, celdas)) {
			escenario[x-1][y].setHedorWumpus(true);
		}
		if (Utilidades.dentroLimites(x, y-1, celdas)) {
			escenario[x][y-1].setHedorWumpus(true);
		}
		if (Utilidades.dentroLimites(x+1, y, celdas)) {
			escenario[x+1][y].setHedorWumpus(true);
		}
		if (Utilidades.dentroLimites(x, y+1, celdas)) {
			escenario[x][y+1].setHedorWumpus(true);
		}	
	}	
	private void calculoPozos(){
		int x,y;
		
		do {
			x = celdaAleatoria();
			y = celdaAleatoria();					
		} while (escenario[x][y].isWumpus() || escenario[x][y].isPozo());
		
		escenario[x][y].setPozo(true);
		
		if (Utilidades.dentroLimites(x-1, y, celdas)) {
			escenario[x-1][y].setBrisaPozo(true);
		}
		if (Utilidades.dentroLimites(x, y-1, celdas)) {
			escenario[x][y-1].setBrisaPozo(true);
		}
		if (Utilidades.dentroLimites(x+1, y, celdas)) {
			escenario[x+1][y].setBrisaPozo(true);
		}
		if (Utilidades.dentroLimites(x, y+1, celdas)) {
			escenario[x][y+1].setBrisaPozo(true);
		}	
	}
	private void calculoLingote(){
		int x,y;
		
		do {
			x = celdaAleatoria();
			y = celdaAleatoria();					
		} while (escenario[x][y].isWumpus() || escenario[x][y].isPozo());
		
		escenario[x][y].setLingote(true);
		
		if (Utilidades.dentroLimites(x-1, y, celdas)) {
			escenario[x-1][y].setBrilloLingote(true);
		}
		if (Utilidades.dentroLimites(x, y-1, celdas)) {
			escenario[x][y-1].setBrilloLingote(true);
		}
		if (Utilidades.dentroLimites(x+1, y, celdas)) {
			escenario[x+1][y].setBrilloLingote(true);
		}
		if (Utilidades.dentroLimites(x, y+1, celdas)) {
			escenario[x][y+1].setBrilloLingote(true);
		}	
	}
	private void calculoCazador(){
		int x,y;
		
		do {
			x = celdaAleatoria();
			y = celdaAleatoria();					
		} while (escenario[x][y].isWumpus() || 
				escenario[x][y].isPozo() ||
				escenario[x][y].isLingote());
		
		escenario[x][y].getCazador().setEnCasilla(true);		
		escenario[x][y].getCazador().setFlechas(flechas);
		posxCaz = x;
		posyCaz = y;
	}
	private void calculoSalida(){
		int x,y;
		
		do {
			x = celdaAleatoria();
			y = celdaAleatoria();					
		} while (escenario[x][y].isPozo());
		
		escenario[x][y].setSalida(true);		
	}
	
	private void opcionesCazador(){
		System.out.println("POSICION: ["+posxCaz+"]["+posyCaz+"]");
		System.out.println("DIRECCION: ["+escenario[posxCaz][posyCaz].getCazador().getDireccion()+"]");
		System.out.println("1) Avanzar");
		System.out.println("2) Girar 90º a la izquierda");
		System.out.println("3) Girar 90º a la derecha");
		System.out.println("4) Lanzar flecha");
		
		if(escenario[posxCaz][posyCaz].isSalida() && escenario[posxCaz][posyCaz].getCazador().isLingote()){
			System.out.println("5) Salir");
		}		
	}
	
	private void avanzarCazador(String direccion){
		Cazador cazAnterior = escenario[posxCaz][posyCaz].getCazador();	
		if("Norte".equals(direccion)){
			if(Utilidades.dentroLimites(posxCaz, posyCaz-1, celdas)){				
				Cazador cazSig = escenario[posxCaz][posyCaz-1].getCazador();
				cazSig.setFlechas(cazAnterior.getFlechas()); 
				cazSig.setLingote(cazAnterior.isLingote());
				cazSig.setEnCasilla(true);
				cazSig.setDireccion("Norte");
				cazAnterior.setEnCasilla(false);
				cazAnterior.setFlechas(0);
				posyCaz = posyCaz-1;
			}
			else{
				System.out.println("[INFO] Colision con la pared");
			}
			
		}
		if("Sur".equals(direccion)){
			if(Utilidades.dentroLimites(posxCaz, posyCaz+1, celdas)){				
				Cazador cazSig = escenario[posxCaz][posyCaz+1].getCazador();
				cazSig.setFlechas(cazAnterior.getFlechas()); 
				cazSig.setLingote(cazAnterior.isLingote());
				cazSig.setEnCasilla(true);
				cazSig.setDireccion("Sur");
				cazAnterior.setEnCasilla(false);
				cazAnterior.setFlechas(0);
				posyCaz = posyCaz+1;
			}
			else{
				System.out.println("[INFO] Colision con la pared");
			}
			
		}
		if("Este".equals(direccion)){
			if(Utilidades.dentroLimites(posxCaz+1, posyCaz, celdas)){				
				Cazador cazSig = escenario[posxCaz+1][posyCaz].getCazador();
				cazSig.setFlechas(cazAnterior.getFlechas()); 
				cazSig.setLingote(cazAnterior.isLingote());
				cazSig.setEnCasilla(true);
				cazSig.setDireccion("Este");
				cazAnterior.setEnCasilla(false);
				cazAnterior.setFlechas(0);
				posxCaz = posxCaz+1;
			}
			else{
				System.out.println("[INFO] Colision con la pared");
			}
			
		}
		if("Oeste".equals(direccion)){
			if(Utilidades.dentroLimites(posxCaz-1, posyCaz, celdas)){				
				Cazador cazSig = escenario[posxCaz-1][posyCaz].getCazador();
				cazSig.setFlechas(cazAnterior.getFlechas()); 
				cazSig.setLingote(cazAnterior.isLingote());
				cazSig.setEnCasilla(true);
				cazSig.setDireccion("Oeste");
				cazAnterior.setEnCasilla(false);
				cazAnterior.setFlechas(0);
				posxCaz = posxCaz-1;
			}
			else{
				System.out.println("[INFO] Colision con la pared");
			}			
		}		
	}
	private void girarCazadorIzq(Cazador cazador){
		if("Norte".equals(cazador.getDireccion())){
			cazador.setDireccion("Oeste");
		}else if("Sur".equals(cazador.getDireccion())){
			cazador.setDireccion("Este");
		}else if("Este".equals(cazador.getDireccion())){
			cazador.setDireccion("Norte");
		}else if("Oeste".equals(cazador.getDireccion())){
			cazador.setDireccion("Sur");	
		}
	}
	private void girarCazadorDer(Cazador cazador){
		if("Norte".equals(cazador.getDireccion())){
			cazador.setDireccion("Este");
		}else if("Sur".equals(cazador.getDireccion())){
			cazador.setDireccion("Oeste");
		}else if("Este".equals(cazador.getDireccion())){
			cazador.setDireccion("Sur");
		}else if("Oeste".equals(cazador.getDireccion())){
			cazador.setDireccion("Norte");	
		}
	}
	
	private void calcularColisiones(){
		if(escenario[posxCaz][posyCaz].getCazador().isEnCasilla() && 
				(escenario[posxCaz][posyCaz].isWumpus() || escenario[posxCaz][posyCaz].isPozo())){
			System.out.println("[INFO] Has muerto");
			escenario[posxCaz][posyCaz].getCazador().setVivo(false);
		}
		else if(escenario[posxCaz][posyCaz].getCazador().isEnCasilla() &&
				escenario[posxCaz][posyCaz].isLingote()){
			System.out.println("[INFO] Coges el lingote");
			
			//El cazador recoge el lingote
			escenario[posxCaz][posyCaz].getCazador().setLingote(true);
			
			//Quitamos el lingote del escenario
			escenario[posxCaz][posyCaz].setLingote(false);			
			if (Utilidades.dentroLimites(posxCaz-1, posyCaz, celdas)) {
				escenario[posxCaz-1][posyCaz].setBrilloLingote(false);
			}
			if (Utilidades.dentroLimites(posxCaz, posyCaz-1, celdas)) {
				escenario[posxCaz][posyCaz-1].setBrilloLingote(false);
			}
			if (Utilidades.dentroLimites(posxCaz+1, posyCaz, celdas)) {
				escenario[posxCaz+1][posyCaz].setBrilloLingote(false);
			}
			if (Utilidades.dentroLimites(posxCaz, posyCaz+1, celdas)) {
				escenario[posxCaz][posyCaz+1].setBrilloLingote(false);
			}				
		}		
	}
	
	private void lanzarFlechaCazador(){
		if(escenario[posxCaz][posyCaz].getCazador().getFlechas()>0){
			if("Norte".equals(escenario[posxCaz][posyCaz].getCazador().getDireccion())){
				//posxCaz, posyCaz-1
				for(int i = posyCaz; i >= 0 && i < celdas ; i--){
					if(escenario[posxCaz][i].isWumpus()){
						System.out.println("[INFO] Escuchas un grito");
						escenario[posxCaz][i].setWumpus(false);
						break;
					}					
				}
			}
			if("Sur".equals(escenario[posxCaz][posyCaz].getCazador().getDireccion())){
				//posxCaz, posyCaz+1
				for(int i = posyCaz; i >= 0 && i < celdas; i++){
					if(escenario[posxCaz][i].isWumpus()){
						System.out.println("[INFO] Escuchas un grito");
						escenario[posxCaz][i].setWumpus(false);
						break;
					}
					
					
				}
			}
			if("Este".equals(escenario[posxCaz][posyCaz].getCazador().getDireccion())){
				//posxCaz+1, posyCaz
				for(int i = posxCaz; i >= 0 && i < celdas; i++){
					if(escenario[i][posyCaz].isWumpus()){
						System.out.println("[INFO] Escuchas un grito");
						escenario[i][posyCaz].setWumpus(false);
						break;
					}					
				}
			}
			if("Oeste".equals(escenario[posxCaz][posyCaz].getCazador().getDireccion())){
				//posxCaz-1, posyCaz
				for(int i = posxCaz; i >= 0 && i < celdas; i--){
					if(escenario[i][posyCaz].isWumpus()){
						System.out.println("[INFO] Escuchas un grito");
						escenario[i][posyCaz].setWumpus(false);
						break;
					}										
				}
			}
			escenario[posxCaz][posyCaz].getCazador().gastarFlecha();
			System.out.println("[INFO] Te quedan "+escenario[posxCaz][posyCaz].getCazador().getFlechas()+" flechas");
		}
		else{
			System.out.println("[INFO] No te quedan flechas!");
		}
	}
}
