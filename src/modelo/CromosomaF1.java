package modelo;

import java.util.Random;
import util.Functions;

public class CromosomaF1 extends Cromosoma {
	private boolean[] genes;
	private double fenotipo;
	private double aptitud;
	private double puntuacion;
        private double max = 250;
        private double min = -250;
        
        public CromosomaF1() {}
	public CromosomaF1(boolean[] genes) {
            this.genes = genes;
        }
        public void inicializa(){
            Random r = new Random(); 
            for(int i = 0; i < genes.length;i++){
                genes[i] =  r.nextBoolean();
            }
            fenotipo();
            aptitud = Functions.f1(fenotipo);
        }
        
	public double fenotipo() {
                fenotipo = Functions.fenotipoBin(max, min, this.bin2dec(), genes.length); 
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
