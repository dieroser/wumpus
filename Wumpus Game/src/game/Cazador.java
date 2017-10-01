package game;

public class Cazador {
	private int flechas;
	private String direccion;
	private boolean enCasilla;
	private boolean lingote;
	
	private boolean vivo;
	
	
	public Cazador(){
		flechas = 0;
		direccion = "Norte";
		enCasilla = false;
		lingote = false;
		vivo = true;
	}
	
	public Cazador(int flechas, String direccion){
		this.flechas = flechas;
		this.direccion = direccion;
	}
	
	public void gastarFlecha(){
		if(flechas != 0){
			flechas--;
		}
	}
	
	public int getFlechas() {
		return flechas;
	}

	public void setFlechas(int flechas) {
		this.flechas = flechas;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public boolean isEnCasilla() {
		return enCasilla;
	}

	public void setEnCasilla(boolean enCasilla) {
		this.enCasilla = enCasilla;
	}
	
	public boolean isLingote() {
		return lingote;
	}
	public void setLingote(boolean lingote) {
		this.lingote = lingote;
	}
	public boolean isVivo() {
		return vivo;
	}
	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}
}
