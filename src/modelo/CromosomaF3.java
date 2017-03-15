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
 * @author josemanuel
 */
public class CromosomaF3 extends Cromosoma {
    	private ArrayList<Boolean> genes;
	private double fenotipox1;
        private double fenotipox2;
	private double aptitud;
        private double aptitudReal;
	private double puntuacion;
        private double puntAcomulada;
        public static double max1 = 12.1;
        public static double max2 = 5.8;
        public static double min1 = -3;
        public static double min2 = 4.1;
        private int tam;
        private int tam1;
        private int tam2;
        
        public CromosomaF3() {
            genes = new ArrayList<Boolean>();
        }
        
        public CromosomaF3(double precision) {
            tam = this.longCromosoma(precision);
            genes = new ArrayList<Boolean>();
        }
        
	public CromosomaF3(ArrayList<Boolean> genes, int tam1, int tam2) {
            this.genes = genes;
            this.tam = genes.size();
            this.tam1 = tam1;
            this.tam2 = tam2;
            fenotipo(); // Necesitamos calcularlo cada vez que cambiemos los genes
            aptitud = Functions.f3(fenotipox1,fenotipox2);
        }
        
        public ArrayList<Boolean> getGenes(){
            return this.genes;
        }
        
        public void inicializa(Random r){
            for(int i = 0; i < tam;i++){
                genes.add(r.nextBoolean());
            }
            fenotipo();
            aptitud = Functions.f3(fenotipox1,fenotipox2);
        }
        
	public void fenotipo() {
                fenotipox1 = Functions.fenotipoBin(max1, min1, this.bin2dec(0,tam1), tam1);
                fenotipox2 = Functions.fenotipoBin(max2, min2, this.bin2dec(tam1,tam1+tam2), tam2);
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
            return longx1(p)+longx2(p);
        }

        private int longx1(double p){
            tam1 = Functions.long_cromosoma(CromosomaF3.min1, CromosomaF3.max1, p);
            return tam1;
        }


        private int longx2(double p){
            tam2 = Functions.long_cromosoma(CromosomaF3.min2, CromosomaF3.max2, p);
            return tam2;
        }

        @Override
        protected Cromosoma copy() {
            CromosomaF3 copia = new CromosomaF3();
            copia.aptitud = this.aptitud;
            copia.aptitudReal = this.aptitudReal;
            copia.fenotipox1 = this.fenotipox1;
            copia.fenotipox2 = this.fenotipox2;
            copia.genes = this.genes;
            copia.puntAcomulada = this.puntAcomulada;
            copia.puntuacion = this.puntuacion;
            copia.tam = this.tam;
            copia.tam1 = this.tam1;
            copia.tam2 = this.tam2;
            return copia;
        }
        
        int getTam1(){
            return this.tam1;
        }
        
        int getTam2(){
            return this.tam2;
        }
        
        
}
