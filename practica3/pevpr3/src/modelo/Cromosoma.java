package modelo;

import java.util.ArrayList;
import java.util.Random;
import util.Functions;
import util.Nodo;


public class Cromosoma {	
    	protected double aptitud;
	protected double puntuacion;
        protected double puntAcomulada;
        protected Nodo arbol;
        protected int tam;
        
        public Cromosoma(){}
        public Cromosoma(Nodo n,ArrayList<ArrayList<Boolean>> casos,int tam){
            this.tam = tam;
            arbol = n;
            aptitud = Functions.calculoAptitud(this, casos, tam);
        }
        
        
        public Nodo getArbol(){
            return arbol;
        }
        
        /**
         * calcula la puntuacion utilizando la suma de puntuaciones 
         * de la poblacion y la guarda en el atributo puntuacion
         * @param suma 
         */
        protected Cromosoma copy(){return this;} //devuelve una copia del individuo
        
        
        
        protected void setPuntAcomulada(double puntuacion) {
            puntAcomulada = puntuacion;
        }

        
        public void setPuntuacion(double suma) {
            this.puntuacion = aptitud / suma;
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
        
        public String toString(String []tabla){
            return arbol.toString(tabla);
        }
        

}
