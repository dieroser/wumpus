package game;

public class Celda {
	// 0 Wumpus
	// 1 Hedor Wumpus
	// 2 Pozo
	// 3 Brisa Pozo
	// 4 Lingote de oro
	// 5 Brillo del oro
	// 6 Salida
	// 7 Cazador
	// Pared y Vacio ojo
	
	private boolean wumpus;
	private boolean hedorWumpus;
	private boolean pozo;
	private boolean brisaPozo;
	private boolean lingote;
	private boolean brilloLingote;
	private boolean salida;
	//private boolean cazador;
	private Cazador cazador;
	
	public Celda() {
		wumpus = false;
		hedorWumpus = false;
		pozo = false;
		brisaPozo = false;
		lingote = false;
		brilloLingote = false;
		salida = false;
		
		cazador = new Cazador();
		//cazador = false;
		
	}
	
	public void estadoCelda(){
		if(wumpus){
			System.out.println("[INFO] Ves a Wumpus");			
		}
		if(hedorWumpus){
			System.out.println("[INFO] Hueles un hedor insoportable");
		}
		if(pozo){
			System.out.println("[INFO] Caes por un pozo");
		}
		if(brisaPozo){
			System.out.println("[INFO] Sientes una brisa");
		}
		if(lingote){
			System.out.println("[INFO] Has encontrado un lingote");
		}
		if(brilloLingote){
			System.out.println("[INFO] Te das cuenta de un brillo poco usual");
		}
		if(salida){
			System.out.println("[INFO] Estas frente a la salida");
		}		
	}
	
	public boolean isWumpus() {
		return wumpus;
	}
	public void setWumpus(boolean wumpus) {
		this.wumpus = wumpus;
	}
	public boolean isHedorWumpus() {
		return hedorWumpus;
	}
	public void setHedorWumpus(boolean hedorWumpus) {
		this.hedorWumpus = hedorWumpus;
	}
	public boolean isPozo() {
		return pozo;
	}
	public void setPozo(boolean pozo) {
		this.pozo = pozo;
	}
	public boolean isBrisaPozo() {
		return brisaPozo;
	}
	public void setBrisaPozo(boolean brisaPozo) {
		this.brisaPozo = brisaPozo;
	}
	public boolean isLingote() {
		return lingote;
	}
	public void setLingote(boolean lingote) {
		this.lingote = lingote;
	}
	public boolean isBrilloLingote() {
		return brilloLingote;
	}
	public void setBrilloLingote(boolean brilloLingote) {
		this.brilloLingote = brilloLingote;
	}
	public boolean isSalida() {
		return salida;
	}
	public void setSalida(boolean salida) {
		this.salida = salida;
	}

	public Cazador getCazador() {
		return cazador;
	}

	public void setCazador(Cazador cazador) {
		this.cazador = cazador;
	}
	
}
