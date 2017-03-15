/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;

/**
 *
 * @author Ederson
 */
public class CromosomaF5 extends Cromosoma{
    private ArrayList<Boolean> genes;
	private double fenotipox1;
        private double fenotipox2;
	private double aptitud;
        private double aptitudReal;
	private double puntuacion;
        private double puntAcomulada;
        public static double max = 512;
        public static double min = -512;
        private int tam;
        
        public CromosomaF5() {
            genes = new ArrayList<Boolean>();
        }
        
        public CromosomaF5(double precision) {
            tam = this.longCromosoma(precision);
            genes = new ArrayList<Boolean>();
        }
        
	public CromosomaF5(ArrayList<Boolean> genes) {
            this.genes = genes;
            this.tam = genes.size();
            fenotipo(); // Necesitamos calcularlo cada vez que cambiemos los genes
            aptitud = Functions.f5(fenotipox1,fenotipox2);
        }
        
        public ArrayList<Boolean> getGenes(){
            return this.genes;
        }
        
        public void inicializa(Random r){
            for(int i = 0; i < tam;i++){
                genes.add(r.nextBoolean());
            }
            fenotipo();
            aptitud = Functions.f5(fenotipox1,fenotipox2);
        }
        
	public void fenotipo() {
                fenotipox1 = Functions.fenotipoBin(max, min, this.bin2dec(0,tam/2), tam/2);
                fenotipox2 = Functions.fenotipoBin(max, min, this.bin2dec(tam/2,tam), tam/2);
	}
        
	public double getAptitud() {
		return aptitud;
	}
        
	public double getPuntuacion() {
		return puntuacion;
	}
        
	public void setPuntuacion (double suma) {
		this.puntuacion = aptitudReal/suma;
	}
        
	private int bin2dec(int y , int x){
            int ret = 0;
            int j = 0; 
            for(int i = y; i < x;i++){ 
                if(genes.get(i)){
                    ret += Math.pow(2, j);
                }
                j++;
            }
            return ret;
        } 
        
        public String toString(){
            return genes.toString() + " " + this.fenotipox1 + " " + this.fenotipox2 + " " + this.aptitud;
        }

        protected int getTamanio() {
            return tam;
        }

        @Override
        protected double getPuntAcomulada() {
           return puntAcomulada;
        }

        @Override
        protected void setPuntAcomulada(double puntuacion) {
            puntAcomulada = puntuacion;
        }

        @Override
        protected void setAptitudDesplazada(double aptR) {
            this.aptitudReal = aptR;
        }

        @Override
        protected double getAptitudReal() {
            return this.aptitudReal;
        }

        public int longCromosoma(double p) {
            return Functions.long_cromosoma(CromosomaF5.min, CromosomaF5.max, p)*2;
        }
        
        @Override
        protected Cromosoma copy() {
            CromosomaF5 copia = new CromosomaF5();
            copia.aptitud = this.aptitud;
            copia.aptitudReal = this.aptitudReal;
            copia.fenotipox1 = this.fenotipox1;
            copia.fenotipox2 = this.fenotipox2;
            copia.genes = this.genes;
            copia.puntAcomulada = this.puntAcomulada;
            copia.puntuacion = this.puntuacion;
            copia.tam = this.tam;
            return copia;
        }
}
