package modelo;

public abstract class Cromosoma {
	
	
	abstract double evalua();
	abstract double fenotipoenotipo();
	abstract double getAptitud();
	abstract double getPuntuacion();
	abstract void setPuntuacion(double puntuacion);
}
