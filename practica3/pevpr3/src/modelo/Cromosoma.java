package modelo;

import java.util.ArrayList;
import util.Functions;
import util.Nodo;


public class Cromosoma {	
    	protected double aptitud;
	protected double puntuacion;
        protected double puntAcomulada;
        protected Nodo arbol;
        protected int nEntradas;
        
        public Cromosoma(){}
        public Cromosoma(Nodo n,ArrayList<ArrayList<Boolean>> casos,int nEntradas){
            this.nEntradas = nEntradas;
            arbol = n;
            aptitud = Functions.calculoAptitud(this, casos, nEntradas);
        }
        
        /**
         * funcion que calcula la apritud del
         * @param casos
         * @param nEntradas 
         */
        public void calculoAptitud (ArrayList<ArrayList<Boolean>> casos,int nEntradas){
            aptitud = Functions.calculoAptitud(this, casos, nEntradas);
        }
        
        
        /**
         * calcula la puntuacion utilizando la suma de puntuaciones 
         * de la poblacion y la guarda en el atributo puntuacion
         * @param suma 
         */
        protected Cromosoma copy(int nIn,ArrayList<ArrayList<Boolean>> casos){
                Nodo n = this.arbol.copy();
                Cromosoma ret = new Cromosoma(n,casos,nEntradas);
                return ret;
        } //devuelve una copia del individuo
        
      
        protected void setPuntAcomulada(double puntuacion) {
            puntAcomulada = puntuacion;
        }

        
        public void setPuntuacion(double suma) {
            this.puntuacion = aptitud / suma;
        }
        
        public Nodo getArbol(){
            return arbol;
        }
        
        public double getAptitud() {
            return aptitud;
        }

        public double getPuntuacion() {
            return puntuacion;
        }
       
        
        /**
         * devuelve el numero de entradas en la parte de 
         * evaluacion que entran en el multiplexor
         * ejemplo: 
         * para un multiplexor de 6 entradas nentradas = 2
         * @return 
         */
        protected int getNEntradas() {
            return nEntradas;
        }
        
        protected double getPuntAcomulada() {
            return puntAcomulada;
        }
        
        public String toString(String []tabla){
            return arbol.toString(tabla);
        }
        

}
