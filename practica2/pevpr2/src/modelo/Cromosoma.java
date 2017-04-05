package modelo;

import java.util.Random;


public abstract class Cromosoma {	
    	protected double aptitud;
        protected double aptitudReal;
	protected double puntuacion;
        protected double puntAcomulada;
        protected int tam;
        public abstract void inicializa(Random r);//inicializa el cromosoma
        protected abstract Object[] toArray();
        protected abstract void setGenes(Object[] array);
	/**
         * calcula la puntuacion utilizando la suma de puntuaciones 
         * de la poblacion y la guarda en el atributo puntuacion
         * @param suma 
         */
        protected abstract Cromosoma copy(); //devuelve una copia del individuo
        /**
         * calcula la longitud del cromosoma utilizando la precision
         * @param p
         * @return 
         */
        public abstract int longCromosoma(double p); 
        
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
