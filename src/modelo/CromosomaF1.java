package modelo;

import java.util.Random;
import util.Functions;

public abstract class CromosomaF1 extends Cromosoma {
	private boolean[] genes;
	private double fenotipo;
	private double aptitud;
	private double puntuacion;
	
        
        public void inicializa(){
            Random r = new Random(); 
            for(int i = 0; i < genes.length;i++){
                genes[i] =  r.nextBoolean();
            }
        }

	public double evalua(double max){
                double result = (max*0.5) - Functions.f1(fenotipo);
                if(result < 0)
                    aptitud = Functions.f1(fenotipo);
                else
                    aptitud = 0;
		return aptitud;
	}
	
	public double getFenotipo() {
		return fenotipo;
	}
        
	public double getAptitud() {
		return aptitud;
	}
        
	public double getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion (double suma) {
		this.puntuacion = aptitud/suma;
	}
        
	private int bin2dec(){
            int ret = 0;
            for(int i = 0; i < genes.length;i++){
                if(genes[i]){
                    ret += Math.pow(2, i);
                }
            }
            return ret;
        } 
        
	
}
