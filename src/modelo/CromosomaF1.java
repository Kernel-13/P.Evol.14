package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;

public class CromosomaF1 extends Cromosoma {
	private ArrayList<Boolean> genes;
	private double fenotipo;
	private double aptitud;
	private double puntuacion;
        private double max = 250;
        private double min = -250;
        
        public CromosomaF1() {}
        
	public CromosomaF1(ArrayList<Boolean> genes) {
            this.genes = genes;
            fenotipo(); // Necesitamos calcularlo cada vez que cambiemos los genes
            aptitud = Functions.f1(fenotipo); // idem
        }
        
        public ArrayList<Boolean> getGenes(){
            return this.genes;
        }
        
        public void inicializa(){
            Random r = new Random(); 
            for(int i = 0; i < genes.size();i++){
                genes.add(r.nextBoolean());
            }
            fenotipo();
            aptitud = Functions.f1(fenotipo);
        }
        
	public double fenotipo() {
                fenotipo = Functions.fenotipoBin(max, min, this.bin2dec(), genes.size()); 
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
            for(int i = 0; i < genes.size();i++){
                if(genes.get(i)){
                    ret += Math.pow(2, i);
                }
            }
            return ret;
        } 
        
	
}
