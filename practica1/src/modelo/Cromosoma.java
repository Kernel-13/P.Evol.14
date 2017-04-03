package modelo;

import java.util.Random;


public abstract class Cromosoma {	
    	protected double aptitud;
        protected double aptitudReal;
	protected double puntuacion;
        protected double puntAcomulada;
        public abstract void inicializa(Random r);//inicializa el cromosoma
        protected abstract void fenotipo(); //calcula el fenotipo
	protected abstract double getAptitud(); //devuelve la aptitud del individuo
	protected abstract double getPuntuacion(); //devuelve la puntuacion del individuo
	/**
         * calcula la puntuacion utilizando la suma de puntuaciones 
         * de la poblacion y la guarda en el atributo puntuacion
         * @param suma 
         */
        protected abstract void setPuntuacion(double suma); 
        protected abstract double getPuntAcomulada(); //devuelve la puntuacion acomulada
	protected abstract void setPuntAcomulada(double puntuacion); 
        protected abstract void setAptitudDesplazada(double aptR); 
        protected abstract double getAptitudDesplazada();
        protected abstract int getTamanio(); //devuelve el tamanio del array de genes
        protected abstract Cromosoma copy(); //devuelve una copia del individuo
        /**
         * calcula la longitud del cromosoma utilizando la precision
         * @param p
         * @return 
         */
        public abstract int longCromosoma(double p); 
}
