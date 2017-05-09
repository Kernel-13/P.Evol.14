package modelo;

import java.util.Random;
import util.Nodo;


public class Cromosoma {	
    	protected double aptitud;
        protected double aptitudReal;
	protected double puntuacion;
        protected double puntAcomulada;
        protected Nodo arbol;
        protected int tam;
        
        //PRACTICA 3
        public Cromosoma(){}
        public Cromosoma(Nodo n){
            arbol = n;
        }
        
        protected Nodo getArbol(){
            return arbol;
        }
        
        //PRACTICA 3
        
        public void inicializa(Random r){}//inicializa el cromosoma
        protected Object[] toArray(){return null;}
        protected void setGenes(Object[] array){}
        
        
        
        /**
         * calcula la puntuacion utilizando la suma de puntuaciones 
         * de la poblacion y la guarda en el atributo puntuacion
         * @param suma 
         */
        protected Cromosoma copy(){return this;} //devuelve una copia del individuo
        
        
        /**
         * calcula la longitud del cromosoma utilizando la precision
         * @param p
         * @return 
         */
        public int longCromosoma(double p){return 0;} 
        
        protected void setPuntAcomulada(double puntuacion) {
            puntAcomulada = puntuacion;
        }

        protected void setAptitudDesplazada(double aptR) {
            this.aptitudReal = aptR;
        }
        
        public void setPuntuacion(double suma) {
            this.puntuacion = aptitudReal / suma;
        }

        protected double getAptitudDesplazada() {
            return this.aptitudReal;
        }
        
        public double getAptitud() {
            return aptitud;
        }

        public double getPuntuacion() {
            return puntuacion;
        }
        
        /**
         * devuelve el tamanio del array de genes
         * @return 
         */
        protected int getTamanio() {
            return tam;
        }
        
        protected double getPuntAcomulada() {
            return puntAcomulada;
        }
}
