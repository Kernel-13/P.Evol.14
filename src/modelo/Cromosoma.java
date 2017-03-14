package modelo;

import java.util.Random;


public abstract class Cromosoma {
	public abstract void inicializa(Random r);
        abstract void fenotipo();
	abstract double getAptitud();
	abstract double getPuntuacion();
	abstract void setPuntuacion(double suma);
        abstract double getPuntAcomulada();
	abstract void setPuntAcomulada(double puntuacion);
        abstract void setAptitudReal(double aptR);
        abstract double getAptitudReal();
        abstract int getTamanio();
        abstract Cromosoma copy();
        public abstract int longCromosoma(double p);
}
