package modelo;

import java.util.Random;


public abstract class Cromosoma {
	public abstract void inicializa(Random r);
        protected abstract void fenotipo();
	protected abstract double getAptitud();
	protected abstract double getPuntuacion();
	protected abstract void setPuntuacion(double suma);
        protected abstract double getPuntAcomulada();
	protected abstract void setPuntAcomulada(double puntuacion);
        protected abstract void setAptitudDesplazada(double aptR);
        protected abstract double getAptitudReal();
        protected abstract int getTamanio();
        protected abstract Cromosoma copy();
        public abstract int longCromosoma(double p);
}
