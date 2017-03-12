package modelo;

public abstract class Cromosoma {
	public abstract void inicializa();
        abstract double fenotipo();
	abstract double getAptitud();
	abstract double getPuntuacion();
	abstract void setPuntuacion(double suma);
        abstract double getPuntAcomulada();
	abstract void setPuntAcomulada(double puntuacion);
        abstract void setAptitudReal(double aptR);
        abstract double getAptitudReal();
        abstract int getTamanio();
}
