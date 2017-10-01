package utils;

import java.util.Scanner;

public class Utilidades {

	public static  boolean dentroLimites(int x, int y, int celdas){
		if(x >= 0 && x < celdas &&
			y >= 0 && y < celdas){
			return true;
		}
		else{
			return false;		
		}
	}
	
	public static int recuperarValorNumerico() {
		Scanner reader = new Scanner(System.in);
		if(reader.hasNextInt()){
			int num = reader.nextInt();		
			return num;
		}
		else{
			System.out.println("Introduce un número");
			return 0;
		}		
		
	}
	
}
