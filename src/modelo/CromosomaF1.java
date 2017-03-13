package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;

public class CromosomaF1 extends Cromosoma {
	private ArrayList<Boolean> genes;
	private double fenotipo;
	private double aptitud;
        private double aptitudReal;
	private double puntuacion;
        private double puntAcomulada;
        public static double max = 250;
        public static double min = -250;
        private int tam;
        
        public CromosomaF1() {
            genes = new ArrayList<Boolean>();
        }
        
        public CromosomaF1(int tamanio) {
            tam = tamanio;
            genes = new ArrayList<Boolean>();
        }
        
	public CromosomaF1(ArrayList<Boolean> genes) {
            this.genes = genes;
            this.tam = genes.size();
            fenotipo(); // Necesitamos calcularlo cada vez que cambiemos los genes
            aptitud = Functions.f1(fenotipo); // idem
        }
        
        public ArrayList<Boolean> getGenes(){
            return this.genes;
        }
        
        public void inicializa(int semilla){
            Random r = new Random(semilla); 
            for(int i = 0; i < tam;i++){
                genes.add(r.nextBoolean());
            }
            fenotipo();
            aptitud = Functions.f1(fenotipo);
        }
        
	public double fenotipo() {
                fenotipo = Functions.fenotipoBin(max, min, this.bin2dec(), tam); 
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
            for(int i = 0; i < tam;i++){
                if(genes.get(i)){
                    ret += Math.pow(2, i);
                }
            }
            return ret;
        } 
        
        public String toString(){
            return genes.toString() + " " + this.fenotipo + " " + this.aptitud;
        }

        int getTamanio() {
            return tam;
        }

        @Override
        double getPuntAcomulada() {
           return puntAcomulada;
        }

        @Override
        void setPuntAcomulada(double puntuacion) {
            puntAcomulada = puntuacion;
        }

        @Override
        void setAptitudReal(double aptR) {
            this.aptitudReal = aptR;
        }

        @Override
        double getAptitudReal() {
            return this.aptitudReal;
        }

    @Override
    Cromosoma copy() {
        CromosomaF1 copia = new CromosomaF1();
        copia.aptitud = this.aptitud;
        copia.aptitudReal = this.aptitudReal;
        copia.fenotipo = this.fenotipo;
        copia.genes = this.genes;
        copia.puntAcomulada = this.puntAcomulada;
        copia.puntuacion = this.puntuacion;
        copia.tam = this.tam;
        return copia;
    }
}
